package games.strategy.engine.framework.persistence.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import games.strategy.engine.data.GameData;
import games.strategy.util.Version;

/**
 * A fixture for testing the integration between the {@link SerializableGameDataImporter} and
 * {@link SerializableGameDataExporter} classes.
 */
public final class SerializableGameDataImportExportIntegrationTest {
  @Test
  public void importerAndExporter_ShouldBeAbleToRoundTripGameData() throws Exception {
    final GameData expected = newGameData();

    final byte[] bytes = exportGameData(expected);
    final GameData actual = importGameData(bytes);

    assertGameDataEquals(expected, actual);
  }

  private static GameData newGameData() {
    final GameData gameData = new GameData();
    gameData.setGameName("name");
    gameData.setGameVersion(new Version(1, 2, 3, 4));
    return gameData;
  }

  private static byte[] exportGameData(final GameData gameData) throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final SerializableGameDataExporter exporter = new SerializableGameDataExporter();
      exporter.exportGameData(gameData, baos);
      return baos.toByteArray();
    }
  }

  private static GameData importGameData(final byte[] bytes) throws Exception {
    try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
      final SerializableGameDataImporter importer = new SerializableGameDataImporter();
      return importer.importGameData(bais);
    }
  }

  private static void assertGameDataEquals(final GameData expected, final GameData actual) {
    assertThat(actual.getGameName(), is(expected.getGameName()));
    assertThat(actual.getGameVersion(), is(expected.getGameVersion()));
  }
}
