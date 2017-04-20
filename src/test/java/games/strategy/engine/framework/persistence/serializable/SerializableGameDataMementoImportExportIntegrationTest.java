package games.strategy.engine.framework.persistence.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import games.strategy.engine.data.TestGameDataMementoFactory;

/**
 * A fixture for testing the integration between the {@link SerializableGameDataMementoImporter} and
 * {@link SerializableGameDataMementoExporter} classes.
 */
public final class SerializableGameDataMementoImportExportIntegrationTest {
  @Test
  public void importerAndExporter_ShouldBeAbleToRoundTripGameDataMemento() throws Exception {
    final Object expected = TestGameDataMementoFactory.newValidGameDataMemento();

    final byte[] bytes = exportGameDataMemento(expected);
    final Object actual = importGameDataMemento(bytes);

    assertThat(actual, is(expected));
  }

  private static byte[] exportGameDataMemento(final Object gameDataMemento) throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final SerializableGameDataMementoExporter exporter = new SerializableGameDataMementoExporter();
      exporter.exportGameDataMemento(gameDataMemento, baos);
      return baos.toByteArray();
    }
  }

  private static Object importGameDataMemento(final byte[] bytes) throws Exception {
    try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
      final SerializableGameDataMementoImporter importer = new SerializableGameDataMementoImporter();
      return importer.importGameDataMemento(bais);
    }
  }
}
