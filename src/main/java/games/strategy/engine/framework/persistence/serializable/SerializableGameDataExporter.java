package games.strategy.engine.framework.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.ObjectOutputStream;

/**
 * A game data exporter for the Java object serialization format.
 *
 * <p>
 * Instances of this class are not thread safe.
 * </p>
 */
public final class SerializableGameDataExporter {
  /**
   * Exports the specified game data to the specified stream.
   *
   * @param gameData The game data; must not be {@code null}.
   * @param os The stream to which the game data will be exported; must not be {@code null}. The caller is responsible
   *        for closing this stream; it will not be closed when this method returns.
   *
   * @throws SerializableGameDataExportException If an error occurs.
   */
  public void exportGameData(final GameData gameData, final OutputStream os)
      throws SerializableGameDataExportException {
    checkNotNull(gameData);
    checkNotNull(os);

    gameData.acquireReadLock();
    try {
      try (final ObjectOutputStream oos = ObjectStreams.newObjectOutputStreamWithCloseShield(os)) {
        writeMetadata(oos);
        writeGameData(oos, gameData);
      } catch (final IOException ex) { // TODO: rename to "e"
        throw new SerializableGameDataExportException(ex);
      }
    } finally {
      gameData.releaseReadLock();
    }
  }

  private static void writeMetadata(final ObjectOutputStream oos) throws IOException {
    oos.writeUTF(SerializableConstants.MIME_TYPE);
    oos.writeLong(SerializableConstants.CURRENT_VERSION);
  }

  private static void writeGameData(final ObjectOutputStream oos, final GameData gameData) throws IOException {
    oos.writeObject(gameData);
  }
}
