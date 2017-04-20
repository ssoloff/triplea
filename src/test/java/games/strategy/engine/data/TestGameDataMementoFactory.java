package games.strategy.engine.data;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating game data mementos for use in tests.
 */
public final class TestGameDataMementoFactory {
  private TestGameDataMementoFactory() {}

  /**
   * Creates a new game data memento that is valid in all respects.
   *
   * @return A new valid game data memento; never {@code null}.
   */
  public static Map<String, Object> newValidGameDataMemento() {
    final GameData gameData = TestGameDataFactory.newValidGameData();
    final Map<String, Object> memento = new HashMap<>();
    memento.put(GameDataMemento.AttributeNames.META_MIME_TYPE, GameDataMemento.MIME_TYPE);
    memento.put(GameDataMemento.AttributeNames.META_VERSION, GameDataMemento.CURRENT_VERSION);
    memento.put(GameDataMemento.AttributeNames.NAME, gameData.getGameName());
    memento.put(GameDataMemento.AttributeNames.VERSION, gameData.getGameVersion());
    // TODO: add other attributes
    return memento;
  }
}
