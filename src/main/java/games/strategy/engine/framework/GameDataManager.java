package games.strategy.engine.framework;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.output.CloseShieldOutputStream;

import games.strategy.debug.ClientLogger;
import games.strategy.engine.ClientContext;
import games.strategy.engine.ClientFileSystemHelper;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.GameDataMemento;
import games.strategy.engine.delegate.IDelegate;
import games.strategy.engine.framework.headlessGameServer.HeadlessGameServer;
import games.strategy.engine.persistence.serializable.PersistenceDelegateRegistryFactory;
import games.strategy.persistence.memento.serializable.SerializableMementoExportException;
import games.strategy.persistence.memento.serializable.SerializableMementoExporter;
import games.strategy.persistence.memento.serializable.SerializableMementoImportException;
import games.strategy.persistence.memento.serializable.SerializableMementoImporter;
import games.strategy.triplea.UrlConstants;
import games.strategy.util.ThreadUtil;
import games.strategy.util.Version;
import games.strategy.util.memento.Memento;
import games.strategy.util.memento.MementoExportException;
import games.strategy.util.memento.MementoExporter;
import games.strategy.util.memento.MementoImportException;
import games.strategy.util.memento.MementoImporter;

/**
 * <p>
 * Title: TripleA
 * </p>
 * <p>
 * Description: Responsible for loading saved games, new games from xml, and saving games.
 * </p>
 */
public class GameDataManager {
  private static final String DELEGATE_START = "<DelegateStart>";
  private static final String DELEGATE_DATA_NEXT = "<DelegateData>";
  private static final String DELEGATE_LIST_END = "<EndDelegateList>";

  public GameDataManager() {}

  public GameData loadGame(final File savedGameFile) throws IOException {
    try (
        FileInputStream fileInputStream = new FileInputStream(savedGameFile);
        InputStream input = new BufferedInputStream(fileInputStream)) {
      String path;
      try {
        path = savedGameFile.getCanonicalPath();
      } catch (final IOException e) {
        path = savedGameFile.getPath();
      }
      return loadGame(input, path);
    }
  }

  public GameData loadGame(final InputStream inputStream, final String savegamePath) throws IOException {
    ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(inputStream));
    try {
      final Version readVersion = (Version) input.readObject();
      final boolean headless = HeadlessGameServer.headless();
      if (!readVersion.equals(ClientContext.engineVersion().getVersion(), true)) {
        // a hack for now, but a headless server should not try to open any savegame that is not its version
        if (headless) {
          final String message = "Incompatible game save, we are: " + ClientContext.engineVersion().getVersion()
              + "  Trying to load game created with: " + readVersion;
          HeadlessGameServer.sendChat(message);
          System.out.println(message);
          return null;
        }
        final String error = "<html>Incompatible engine versions, and no old engine found. We are: "
            + ClientContext.engineVersion().getVersion() + " . Trying to load game created with: " + readVersion
            + "<br>To download the latest version of TripleA, Please visit "
            + UrlConstants.LATEST_GAME_DOWNLOAD_WEBSITE + "</html>";
        if (savegamePath == null) {
          throw new IOException(error);
        }
        // so, what we do here is try to see if our installed copy of triplea includes older jars with it that are the
        // same engine as was
        // used for this savegame, and if so try to run it
        try {
          final String newClassPath = GameRunner.findOldJar(readVersion, true);
          // ask user if we really want to do this?
          final String messageString = "<html>This TripleA engine is version "
              + ClientContext.engineVersion().getVersion()
              + " and you are trying to open a savegame made with version " + readVersion.toString()
              + "<br>However, this TripleA cannot open any savegame made by any engine other than engines with the "
              + "same first three version numbers as it (x_x_x_x)."
              + "<br><br>TripleA now comes with older engines included with it, and has found the engine to run this "
              + "savegame. This is a new feature and is in 'beta' stage."
              + "<br>It will attempt to run a new instance of TripleA using the older engine jar file, and this "
              + "instance will only be able to play this savegame."
              + "<br><b>You may choose to either Close or Keep the current instance of TripleA!</b> (If hosting, you "
              + "must close it). Please report any bugs or issues."
              + "<br><br>Do you wish to continue?</html>";
          final String yesClose = "Yes & Close Current";
          final String yesOpen = "Yes & Do Not Close";
          final String cancel = "Cancel";
          final Object[] options = new Object[] {yesClose, yesOpen, cancel};
          final JOptionPane pane = new JOptionPane(messageString, JOptionPane.PLAIN_MESSAGE,
              JOptionPane.YES_NO_CANCEL_OPTION, null, options, yesClose);
          final JDialog window = pane.createDialog(null, "Run old jar to open old Save Game?");
          window.setVisible(true);
          final Object buttonPressed = pane.getValue();
          if (buttonPressed == null || buttonPressed.equals(cancel)) {
            return null;
          }
          final boolean closeCurrentInstance = buttonPressed.equals(yesClose);
          GameRunner.startGame(savegamePath, newClassPath, null);
          if (closeCurrentInstance) {
            ThreadUtil.sleep(1000);
            System.exit(0);
          }
        } catch (final IOException e) {
          if (ClientFileSystemHelper.areWeOldExtraJar()) {
            throw new IOException("<html>Please run the default TripleA and try to open this game again. "
                + "<br>This TripleA engine is old and kept only for backwards compatibility and can only open "
                + "savegames created by engines with these first 3 version digits: "
                + ClientContext.engineVersion().getVersion().toStringFull("_", true) + "</html>");
          } else {
            throw new IOException(error);
          }
        }
        return null;
      } else if (!headless && readVersion.isGreaterThan(ClientContext.engineVersion().getVersion(), false)) {
        // we can still load it because first 3 numbers of the version are the same, however this save was made by a
        // newer engine, so prompt
        // the user to upgrade
        final String messageString =
            "<html>Your TripleA engine is OUT OF DATE.  This save was made by a newer version of TripleA."
                + "<br>However, because the first 3 version numbers are the same as your current version, we can "
                + "still open the savegame."
                + "<br><br>This TripleA engine is version "
                + ClientContext.engineVersion().getVersion().toStringFull("_")
                + " and you are trying to open a savegame made with version " + readVersion.toStringFull("_")
                + "<br><br>To download the latest version of TripleA, Please visit "
                + UrlConstants.LATEST_GAME_DOWNLOAD_WEBSITE
                + "<br><br>It is recommended that you upgrade to the latest version of TripleA before playing this "
                + "savegame."
                + "<br><br>Do you wish to continue and open this save with your current 'old' version?</html>";
        final int answer =
            JOptionPane.showConfirmDialog(null, messageString, "Open Newer Save Game?", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
          return null;
        }
      }
      final GameData data = (GameData) input.readObject();
      // TODO: expand this functionality (and keep it updated)
      updateDataToBeCompatibleWithNewEngine(readVersion, data);
      loadDelegates(input, data);
      data.postDeSerialize();
      return data;
    } catch (final ClassNotFoundException cnfe) {
      throw new IOException(cnfe.getMessage());
    }
  }

  /**
   * Use this to keep compatibility between savegames when it is easy to do so.
   * When it is not easy to do so, just make sure to include the last release's .jar file in the "old" folder for
   * triplea.
   * FYI: Engine version numbers work like this with regards to savegames:
   * Any changes to the first 3 digits means that the savegame is not compatible between different engines.
   * While any change only to the 4th (last) digit means that the savegame must be compatible between different engines.
   */
  private void updateDataToBeCompatibleWithNewEngine(final Version originalEngineVersion, final GameData data) {
    // whenever this gets out of date, just comment out (but keep as an example, by commenting out)
    /*
     * example1:
     * final Version v1610 = new Version(1, 6, 1, 0);
     * final Version v1620 = new Version(1, 6, 2, 0);
     * if (originalEngineVersion.equals(v1610, false)
     * && ClientContext.engineVersion().getVersion().isGreaterThan(v1610, false)
     * && ClientContext.engineVersion().getVersion().isLessThan(v1620, true))
     * {
     * // if original save was done under 1.6.1.0, and new engine is greater than 1.6.1.0 and less than 1.6.2.0
     * try
     * {
     * if (TechAdvance.getTechAdvances(data).isEmpty())
     * {
     * System.out.println("Adding tech to be compatible with 1.6.1.x");
     * TechAdvance.createDefaultTechAdvances(data);
     * TechAbilityAttachment.setDefaultTechnologyAttachments(data);
     * }
     * } catch (final Exception e)
     * {
     * ClientLogger.logQuietly(e);
     * }
     * }
     */
  }

  private static void loadDelegates(final ObjectInputStream input, final GameData data)
      throws ClassNotFoundException, IOException {
    for (Object endMarker = input.readObject(); !endMarker.equals(DELEGATE_LIST_END); endMarker = input.readObject()) {
      final String name = (String) input.readObject();
      final String displayName = (String) input.readObject();
      final String className = (String) input.readObject();
      IDelegate instance;
      try {
        instance = (IDelegate) Class.forName(className).getDeclaredConstructor().newInstance();
        instance.initialize(name, displayName);
        data.getDelegateList().addDelegate(instance);
      } catch (final Exception e) {
        ClientLogger.logQuietly(e);
        throw new IOException(e.getMessage());
      }
      final String next = (String) input.readObject();
      if (next.equals(DELEGATE_DATA_NEXT)) {
        instance.loadState((Serializable) input.readObject());
      }
    }
  }

  public void saveGame(final OutputStream sink, final GameData data) throws IOException {
    saveGame(sink, data, true);
  }

  void saveGame(final OutputStream sink, final GameData data, final boolean saveDelegateInfo) throws IOException {
    // write internally first in case of error
    final ByteArrayOutputStream bytes = new ByteArrayOutputStream(25000);
    final ObjectOutputStream outStream = new ObjectOutputStream(bytes);
    outStream.writeObject(games.strategy.engine.ClientContext.engineVersion().getVersion());
    data.acquireReadLock();
    try {
      outStream.writeObject(data);
      if (saveDelegateInfo) {
        writeDelegates(data, outStream);
      } else {
        outStream.writeObject(DELEGATE_LIST_END);
      }
    } finally {
      data.releaseReadLock();
    }
    try (final GZIPOutputStream zippedOut = new GZIPOutputStream(sink)) {
      // now write to file
      zippedOut.write(bytes.toByteArray());
    }
  }

  private static void writeDelegates(final GameData data, final ObjectOutputStream out) throws IOException {
    final Iterator<IDelegate> iter = data.getDelegateList().iterator();
    while (iter.hasNext()) {
      out.writeObject(DELEGATE_START);
      final IDelegate delegate = iter.next();
      // write out the delegate info
      out.writeObject(delegate.getName());
      out.writeObject(delegate.getDisplayName());
      out.writeObject(delegate.getClass().getName());
      out.writeObject(DELEGATE_DATA_NEXT);
      out.writeObject(delegate.saveState());
    }
    // mark end of delegate section
    out.writeObject(DELEGATE_LIST_END);
  }

  /**
   * Loads game data from the specified stream, which is expected to be in serializable format.
   *
   * @param is The stream from which the game data will be loaded; must not be {@code null}. The caller is responsible
   *        for closing this stream; it will not be closed when this method returns.
   *
   * @return The loaded game data; never {@code null}.
   *
   * @throws IOException If an error occurs while loading the game.
   */
  public static GameData loadSerializableGame(final InputStream is) throws IOException {
    checkNotNull(is);

    return fromMemento(loadMemento(new CloseShieldInputStream(is)));
  }

  private static Memento loadMemento(final InputStream is) throws IOException {
    try (final GZIPInputStream gzipis = new GZIPInputStream(is)) {
      final SerializableMementoImporter mementoImporter =
          new SerializableMementoImporter(PersistenceDelegateRegistryFactory.newPlatformPersistenceDelegateRegistry());
      return mementoImporter.importMemento(gzipis);
    } catch (final SerializableMementoImportException e) {
      throw new IOException(e);
    }
  }

  private static GameData fromMemento(final Memento memento) throws IOException {
    try {
      final MementoImporter<GameData> mementoImporter = GameDataMemento.newImporter();
      return mementoImporter.importMemento(memento);
    } catch (final MementoImportException e) {
      throw new IOException(e);
    }
  }

  /**
   * Saves the specified game data to the specified stream in serializable format.
   *
   * @param os The stream to which the game data will be saved; must not be {@code null}. The caller is responsible for
   *        closing this stream; it will not be closed when this method returns.
   * @param gameData The game data to save; must not be {@code null}.
   *
   * @throws IOException If an error occurs while saving the game.
   */
  public static void saveSerializableGame(final OutputStream os, final GameData gameData) throws IOException {
    checkNotNull(os);
    checkNotNull(gameData);

    saveMemento(new CloseShieldOutputStream(os), toMemento(gameData));
  }

  private static Memento toMemento(final GameData gameData) throws IOException {
    try {
      final MementoExporter<GameData> mementoExporter = GameDataMemento.newExporter();
      return mementoExporter.exportMemento(gameData);
    } catch (final MementoExportException e) {
      throw new IOException(e);
    }
  }

  private static void saveMemento(final OutputStream os, final Memento memento) throws IOException {
    try (final GZIPOutputStream gzipos = new GZIPOutputStream(os)) {
      final SerializableMementoExporter mementoExporter = new SerializableMementoExporter(
          PersistenceDelegateRegistryFactory.newPlatformPersistenceDelegateRegistry());
      mementoExporter.exportMemento(memento, gzipos);
    } catch (final SerializableMementoExportException e) {
      throw new IOException(e);
    }
  }
}
