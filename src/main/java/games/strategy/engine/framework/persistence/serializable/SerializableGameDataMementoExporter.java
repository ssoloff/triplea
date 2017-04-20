package games.strategy.engine.framework.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.output.CloseShieldOutputStream;

import games.strategy.persistence.serializable.ObjectOutputStream;

/**
 * A game data memento exporter for the Java object serialization format.
 *
 * <p>
 * Instances of this class are not thread safe.
 * </p>
 */
public final class SerializableGameDataMementoExporter {
  /**
   * Exports the specified game data memento to the specified stream.
   *
   * @param gameDataMemento The game data memento; must not be {@code null}.
   * @param os The stream to which the game data memento will be exported; must not be {@code null}. The caller is
   *        responsible for closing this stream; it will not be closed when this method returns.
   *
   * @throws SerializableGameDataMementoExportException If an error occurs.
   */
  public void exportGameDataMemento(final Object gameDataMemento, final OutputStream os)
      throws SerializableGameDataMementoExportException {
    checkNotNull(gameDataMemento);
    checkNotNull(os);

    try (final GZIPOutputStream gzipos = new GZIPOutputStream(new CloseShieldOutputStream(os));
        final ObjectOutputStream oos = ObjectStreams.newObjectOutputStream(gzipos)) {
      writeMetadata(oos);
      writeGameDataMemento(oos, gameDataMemento);
    } catch (final IOException e) {
      throw new SerializableGameDataMementoExportException(e);
    }
  }

  private static void writeMetadata(final ObjectOutputStream oos) throws IOException {
    oos.writeUTF(SerializableConstants.MIME_TYPE);
    oos.writeLong(SerializableConstants.CURRENT_VERSION);
  }

  private static void writeGameDataMemento(final ObjectOutputStream oos, final Object gameDataMemento)
      throws IOException {
    oos.writeObject(gameDataMemento);
  }
}
