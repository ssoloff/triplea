package games.strategy.engine.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import games.strategy.util.memento.Memento;
import games.strategy.util.memento.PropertyBagMemento;

/**
 * A factory for creating game data mementos for use in tests.
 */
public final class TestGameDataMementoFactory {
  private TestGameDataMementoFactory() {}

  /**
   * Creates a new game data memento that is valid in all respects.
   *
   * @return A new game data memento; never {@code null}.
   */
  public static Memento newValidMemento() {
    return newMementoWithProperties(newValidMementoPropertiesByName());
  }

  private static Map<String, Object> newValidMementoPropertiesByName() {
    final GameData gameData = TestGameDataFactory.newValidGameData();
    final Map<String, Object> propertiesByName = new HashMap<>();
    propertiesByName.put(GameDataMemento.PropertyNames.NAME, gameData.getGameName());
    propertiesByName.put(GameDataMemento.PropertyNames.VERSION, gameData.getGameVersion());
    // TODO: handle other properties
    return propertiesByName;
  }

  private static PropertyBagMemento newMementoWithProperties(final Map<String, Object> propertiesByName) {
    return new PropertyBagMemento(GameDataMemento.ID, GameDataMemento.CURRENT_VERSION, propertiesByName);
  }

  /**
   * Creates a new game data memento that is valid in all respects but includes (or overrides) the specified property.
   *
   * @param name The property name; must not be {@code null}.
   * @param value The property value; may be {@code null}.
   *
   * @return A new game data memento; never {@code null}.
   */
  public static Memento newMementoWithProperty(final String name, final Object value) {
    checkNotNull(name);

    final Map<String, Object> propertiesByName = newValidMementoPropertiesByName();
    propertiesByName.put(name, value);
    return newMementoWithProperties(propertiesByName);
  }

  /**
   * Creates a new game data memento that is valid in all respects but does not include the specified property.
   *
   * @param name The property name; must not be {@code null}.
   *
   * @return A new game data memento; never {@code null}.
   */
  public static Memento newMementoWithoutProperty(final String name) {
    checkNotNull(name);

    final Map<String, Object> propertiesByName = newValidMementoPropertiesByName();
    propertiesByName.remove(name);
    return newMementoWithProperties(propertiesByName);
  }
}
