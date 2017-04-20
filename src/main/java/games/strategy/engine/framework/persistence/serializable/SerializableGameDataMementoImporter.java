package games.strategy.engine.framework.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.input.CloseShieldInputStream;

import games.strategy.persistence.serializable.ObjectInputStream;

/**
 * A game data memento importer for the Java object serialization format.
 *
 * <p>
 * Instances of this class are not thread safe.
 * </p>
 */
public final class SerializableGameDataMementoImporter {
  /**
   * Imports a game data memento from the specified stream.
   *
   * @param is The stream from which the game data memento will be imported; must not be {@code null}. The caller is
   *        responsible for closing this stream; it will not be closed when this method returns.
   *
   * @return The imported game data memento; never {@code null}.
   *
   * @throws SerializableGameDataMementoImportException If an error occurs.
   */
  public Object importGameDataMemento(final InputStream is) throws SerializableGameDataMementoImportException {
    checkNotNull(is);

    try (final GZIPInputStream gzipis = new GZIPInputStream(new CloseShieldInputStream(is));
        final ObjectInputStream ois = ObjectStreams.newObjectInputStream(gzipis)) {
      readMetadata(ois);
      return readGameDataMemento(ois);
    } catch (final IOException | ClassNotFoundException e) {
      throw new SerializableGameDataMementoImportException(e);
    }
  }

  private static void readMetadata(final ObjectInputStream ois)
      throws SerializableGameDataMementoImportException, IOException {
    final String mimeType = ois.readUTF();
    if (!SerializableConstants.MIME_TYPE.equals(mimeType)) {
      throw new SerializableGameDataMementoImportException("an illegal MIME type was specified in the stream");
    }

    final long version = ois.readLong();
    if (version != SerializableConstants.CURRENT_VERSION) {
      throw new SerializableGameDataMementoImportException("an incompatible version was specified in the stream");
    }
  }

  private static Object readGameDataMemento(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
    return ois.readObject();
  }
}
