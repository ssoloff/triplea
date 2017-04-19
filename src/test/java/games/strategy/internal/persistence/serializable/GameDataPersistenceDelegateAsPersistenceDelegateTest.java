package games.strategy.internal.persistence.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.AbstractPersistenceDelegateTestCase;
import games.strategy.persistence.serializable.PersistenceDelegateRegistry;
import games.strategy.util.Version;

public final class GameDataPersistenceDelegateAsPersistenceDelegateTest
    extends AbstractPersistenceDelegateTestCase<GameData> {
  public GameDataPersistenceDelegateAsPersistenceDelegateTest() {
    super(GameData.class);
  }

  @Override
  protected void assertSubjectEquals(final GameData expected, final GameData actual) {
    assertThat(actual.getGameName(), is(expected.getGameName()));
    assertThat(actual.getGameVersion(), is(expected.getGameVersion()));
  }

  @Override
  protected GameData createSubject() {
    final GameData gameData = new GameData();
    gameData.setGameName("name");
    gameData.setGameVersion(new Version(1, 2, 3, 4));
    return gameData;
  }

  @Override
  protected void registerPersistenceDelegates(final PersistenceDelegateRegistry persistenceDelegateRegistry) {
    persistenceDelegateRegistry.registerPersistenceDelegate(GameData.class, new GameDataPersistenceDelegate());
  }
}
