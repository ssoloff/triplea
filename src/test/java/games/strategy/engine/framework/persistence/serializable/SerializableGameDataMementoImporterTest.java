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
import java.util.zip.GZIPOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import games.strategy.engine.data.TestGameDataMementoFactory;

@RunWith(MockitoJUnitRunner.class)
public final class SerializableGameDataMementoImporterTest {
  private final Object gameDataMemento = TestGameDataMementoFactory.newValidGameDataMemento();

  private final SerializableGameDataMementoImporter importer = new SerializableGameDataMementoImporter();

  @Test
  public void importGameDataMemento_ShouldThrowExceptionWhenInputStreamIsNull() {
    catchException(() -> importer.importGameDataMemento(null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void importGameDataMemento_ShouldNotCloseInputStream() throws Exception {
    try (final InputStream is = spy(newInputStreamWithValidContent())) {
      importer.importGameDataMemento(is);

      verify(is, never()).close();
    }
  }

  private InputStream newInputStreamWithValidContent() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final SerializableGameDataMementoExporter exporter = new SerializableGameDataMementoExporter();
      exporter.exportGameDataMemento(gameDataMemento, baos);
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }

  @Test
  public void importGameDataMemento_ShouldThrowExceptionWhenMetadataMimeTypeIllegal() throws Exception {
    try (final InputStream is = spy(newInputStreamWithIllegalMetadataMimeType())) {
      catchException(() -> importer.importGameDataMemento(is));

      assertThat(caughtException(), allOf(
          is(instanceOf(SerializableGameDataMementoImportException.class)),
          hasMessageThat(containsString("illegal MIME type"))));
    }
  }

  private InputStream newInputStreamWithIllegalMetadataMimeType() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (final GZIPOutputStream gzipos = new GZIPOutputStream(baos);
          final ObjectOutputStream oos = ObjectStreams.newObjectOutputStream(gzipos)) {
        oos.writeUTF("application/octet-stream");
        oos.writeLong(SerializableConstants.CURRENT_VERSION);
        oos.writeObject(gameDataMemento);
      }
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }

  @Test
  public void importGameDataMemento_ShouldThrowExceptionWhenMetadataVersionIncompatible() throws Exception {
    try (final InputStream is = spy(newInputStreamWithIncompatibleMetadataVersion())) {
      catchException(() -> importer.importGameDataMemento(is));

      assertThat(caughtException(), allOf(
          is(instanceOf(SerializableGameDataMementoImportException.class)),
          hasMessageThat(containsString("incompatible version"))));
    }
  }

  private InputStream newInputStreamWithIncompatibleMetadataVersion() throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (final GZIPOutputStream gzipos = new GZIPOutputStream(baos);
          final ObjectOutputStream oos = ObjectStreams.newObjectOutputStream(gzipos)) {
        oos.writeUTF(SerializableConstants.MIME_TYPE);
        oos.writeLong(-1L);
        oos.writeObject(gameDataMemento);
      }
      return new ByteArrayInputStream(baos.toByteArray());
    }
  }
}
