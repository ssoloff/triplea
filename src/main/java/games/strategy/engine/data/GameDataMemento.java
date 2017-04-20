package games.strategy.engine.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;

import games.strategy.util.Version;

/**
 * Provides methods for capturing game data state in a memento and restoring game data state from a memento.
 */
public final class GameDataMemento {
  @VisibleForTesting
  static final class AttributeNames {
    private AttributeNames() {}

    static final String META_MIME_TYPE = "_meta:mimeType";
    static final String META_VERSION = "_meta:version";
    static final String NAME = "name";
    static final String VERSION = "version";
  }

  @VisibleForTesting
  static final long CURRENT_VERSION = 1L;

  @VisibleForTesting
  static final String MIME_TYPE = "x.triplea.game-data-memento";

  /** An immutable collection of default options for creating a new game data memento. */
  public static final Map<OptionName, Object> DEFAULT_OPTIONS = newDefaultOptions();

  private GameDataMemento() {}

  private static Map<OptionName, Object> newDefaultOptions() {
    final EnumMap<OptionName, Object> options = new EnumMap<>(OptionName.class);
    options.put(OptionName.EXCLUDE_DELEGATES, false);
    return Collections.unmodifiableMap(options);
  }

  /**
   * Creates a new memento for the specified game data using the default options.
   *
   * @param gameData The memento originator; must not be {@code null}.
   *
   * @return A game data memento; never {@code null}.
   */
  public static Object fromGameData(final GameData gameData) {
    checkNotNull(gameData);

    return fromGameData(gameData, DEFAULT_OPTIONS);
  }

  /**
   * Creates a new memento for the specified game data using the specified options.
   *
   * @param gameData The memento originator; must not be {@code null}.
   * @param options The memento creation options; must not be {@code null}.
   *
   * @return A game data memento; never {@code null}.
   *
   * @throws IllegalArgumentException If {@code options} contains an illegal value (e.g. the value is of the wrong
   *         type).
   */
  public static Object fromGameData(final GameData gameData, final Map<OptionName, Object> options) {
    checkNotNull(gameData);
    checkNotNull(options);

    // TODO: add support for options

    final Map<String, Object> attributes = new HashMap<>();

    gameData.acquireReadLock();
    try {
      attributes.put(AttributeNames.META_MIME_TYPE, MIME_TYPE);
      attributes.put(AttributeNames.META_VERSION, CURRENT_VERSION);
      attributes.put(AttributeNames.NAME, gameData.getGameName());
      attributes.put(AttributeNames.VERSION, gameData.getGameVersion());
      // TODO: add remaining attributes
    } finally {
      gameData.releaseReadLock();
    }

    return mementoFromAttributes(attributes);
  }

  private static Object mementoFromAttributes(final Map<String, Object> attributes) {
    assert attributes != null;

    return attributes;
  }

  /**
   * The names of the options that can be specified when creating a new game data memento.
   */
  public static enum OptionName {
    /**
     * Indicates delegates should be excluded from the memento (value type: {@code Boolean}).
     */
    EXCLUDE_DELEGATES
  }

  /**
   * Creates a new game data from the specified memento.
   *
   * @param memento The game data memento; must not be {@code null}.
   *
   * @return A new game data; never {@code null}.
   *
   * @throws GameDataMementoException If the memento does not represent a valid game data state.
   */
  public static GameData toGameData(final Object memento) throws GameDataMementoException {
    checkNotNull(memento);

    final Map<String, Object> attributes = mementoToAttributes(memento);
    verifyMetadata(attributes);

    final GameData gameData = new GameData();
    gameData.setGameName(getRequiredAttribute(attributes, AttributeNames.NAME, String.class));
    gameData.setGameVersion(getRequiredAttribute(attributes, AttributeNames.VERSION, Version.class));
    // TODO: add remaining attributes
    return gameData;
  }

  private static Map<String, Object> mementoToAttributes(final Object memento) throws GameDataMementoException {
    try {
      @SuppressWarnings("unchecked")
      final Map<String, Object> attributes = (Map<String, Object>) memento;
      return attributes;
    } catch (final ClassCastException ex) { // TODO: rename "e"
      throw new GameDataMementoException("memento has wrong type", ex);
    }
  }

  private static void verifyMetadata(final Map<String, Object> attributes) throws GameDataMementoException {
    final String mimeType = getRequiredAttribute(attributes, AttributeNames.META_MIME_TYPE, String.class);
    if (!MIME_TYPE.equals(mimeType)) {
      throw new GameDataMementoException("memento specifies an illegal MIME type");
    }

    final long version = getRequiredAttribute(attributes, AttributeNames.META_VERSION, Long.class);
    if (version != CURRENT_VERSION) {
      throw new GameDataMementoException("memento specifies an incompatible version");
    }
  }

  private static <T> T getRequiredAttribute(
      final Map<String, Object> attributes,
      final String name,
      final Class<T> type) throws GameDataMementoException {
    assert attributes != null;
    assert name != null;
    assert type != null;

    if (!attributes.containsKey(name)) {
      throw new GameDataMementoException(String.format("memento is missing required attribute '%s'", name));
    }

    try {
      return type.cast(attributes.get(name));
    } catch (final ClassCastException ex) { // TODO: rename "e"
      throw new GameDataMementoException(String.format("memento attribute '%s' has wrong type", name), ex);
    }
  }
}
