package games.strategy.engine.framework.persistence.serializable;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import games.strategy.engine.data.GameData;

@RunWith(MockitoJUnitRunner.class)
public final class SerializableGameDataImporterTest {
  private final GameData gameData = new GameData();

  private final SerializableGameDataImporter importer = new SerializableGameDataImporter();

  @Test
  public void importGameData_ShouldThrowExceptionWhenInputStreamIsNull() {
    catchException(() -> importer.importGameData(null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void importGameData_ShouldNotCloseInputStream() throws Exception {
    try (final InputStream is = spy(newInputStreamWithValidContent())) {
      importer.importGameData(is);

      verify(is, never()).close();
    }
  }

  private InputStream newInputStreamWithValidContent() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final SerializableGameDataExporter exporter = new SerializableGameDataExporter();
      exporter.exportGameData(gameData, baos);
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }

  @Test
  public void importGameData_ShouldThrowExceptionWhenMetadataMimeTypeIllegal() throws Exception {
    try (final InputStream is = spy(newInputStreamWithIllegalMetadataMimeType())) {
      catchException(() -> importer.importGameData(is));

      assertThat(caughtException(), allOf(
          is(instanceOf(SerializableGameDataImportException.class)),
          hasMessageThat(containsString("illegal MIME type"))));
    }
  }

  private InputStream newInputStreamWithIllegalMetadataMimeType() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (final ObjectOutputStream oos = ObjectStreams.newObjectOutputStream(baos)) {
        oos.writeUTF("application/octet-stream");
        oos.writeLong(SerializableConstants.CURRENT_VERSION);
        oos.writeObject(gameData);
      }
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }

  @Test
  public void importGameData_ShouldThrowExceptionWhenMetadataVersionIncompatible() throws Exception {
    try (final InputStream is = spy(newInputStreamWithIncompatibleMetadataVersion())) {
      catchException(() -> importer.importGameData(is));

      assertThat(caughtException(), allOf(
          is(instanceOf(SerializableGameDataImportException.class)),
          hasMessageThat(containsString("incompatible version"))));
    }
  }

  private InputStream newInputStreamWithIncompatibleMetadataVersion() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (final ObjectOutputStream oos = ObjectStreams.newObjectOutputStream(baos)) {
        oos.writeUTF(SerializableConstants.MIME_TYPE);
        oos.writeLong(-1L);
        oos.writeObject(gameData);
      }
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }
}
