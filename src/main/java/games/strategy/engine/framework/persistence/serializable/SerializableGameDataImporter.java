package games.strategy.engine.framework.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.ObjectInputStream;

/**
 * A game data importer for the Java object serialization format.
 *
 * <p>
 * Instances of this class are not thread safe.
 * </p>
 */
public final class SerializableGameDataImporter {
  /**
   * Imports game data from the specified stream.
   *
   * @param is The stream from which the game data will be imported; must not be {@code null}. The caller is responsible
   *        for closing this stream; it will not be closed when this method returns.
   *
   * @return The imported game data; never {@code null}.
   *
   * @throws SerializableGameDataImportException If an error occurs.
   */
  public GameData importGameData(final InputStream is) throws SerializableGameDataImportException {
    checkNotNull(is);

    try (final ObjectInputStream ois = ObjectStreams.newObjectInputStreamWithCloseShield(is)) {
      readMetadata(ois);
      return readGameData(ois);
    } catch (final IOException | ClassNotFoundException ex) { // TODO: rename "e"
      throw new SerializableGameDataImportException(ex);
    }
  }

  private static void readMetadata(final ObjectInputStream ois)
      throws SerializableGameDataImportException, IOException {
    final String mimeType = ois.readUTF();
    if (!SerializableConstants.MIME_TYPE.equals(mimeType)) {
      throw new SerializableGameDataImportException("an illegal MIME type was specified in the stream");
    }

    final long version = ois.readLong();
    if (version != SerializableConstants.CURRENT_VERSION) {
      throw new SerializableGameDataImportException("an incompatible version was specified in the stream");
    }
  }

  private static GameData readGameData(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
    return (GameData) ois.readObject();
  }
}
