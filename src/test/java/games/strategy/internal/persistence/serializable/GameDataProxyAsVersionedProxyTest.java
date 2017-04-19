package games.strategy.internal.persistence.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.AbstractVersionedProxyTestCase;
import games.strategy.util.Version;

public final class GameDataProxyAsVersionedProxyTest extends AbstractVersionedProxyTestCase<GameData> {
  public GameDataProxyAsVersionedProxyTest() {
    super(GameData.class);
  }

  @Override
  protected void assertSubjectEquals(final GameData expected, final GameData actual) {
    assertThat(actual.getGameName(), is(expected.getGameName()));
    assertThat(actual.getGameVersion(), is(expected.getGameVersion()));
  }

  @Override
  protected List<SupportedVersion<GameData>> getSupportedVersions() {
    return Arrays.asList(v1());
  }

  private static SupportedVersion<GameData> v1() {
    final String base16EncodedBytes = ""
        + "ACED00057372003E67616D65732E73747261746567792E696E7465726E616C2E" // ....sr.>games.strategy.internal.
        + "70657273697374656E63652E73657269616C697A61626C652E47616D65446174" // persistence.serializable.GameDat
        + "6150726F787997A14E957A1779E00C0000787077080000000000000001740004" // aProxy..N.z.y....xpw.........t..
        + "6E616D657372001B67616D65732E73747261746567792E7574696C2E56657273" // namesr..games.strategy.util.Vers
        + "696F6EBDCCCE80308907DB0200044900076D5F6D616A6F724900076D5F6D6963" // ion....0......I..m_majorI..m_mic
        + "726F4900076D5F6D696E6F724900076D5F706F696E7478700000000100000004" // roI..m_minorI..m_pointxp........
        + "000000020000000378" //////////////////////////////////////////////// ........x
        + "";
    final GameData expected = new GameData();
    expected.setGameName("name");
    expected.setGameVersion(new Version(1, 2, 3, 4));
    return new SupportedVersion<>(expected, base16EncodedBytes);
  }
}
