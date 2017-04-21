package games.strategy.engine.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.annotations.VisibleForTesting;

import games.strategy.util.Version;
import games.strategy.util.memento.MementoExporter;
import games.strategy.util.memento.MementoImportException;
import games.strategy.util.memento.MementoImporter;
import games.strategy.util.memento.PropertyBagMementoExporter;
import games.strategy.util.memento.PropertyBagMementoImporter;

/**
 * Provides factory methods for creating objects that can import mementos to and export mementos from game data.
 */
public final class GameDataMemento {
  @VisibleForTesting
  static final class PropertyNames {
    private PropertyNames() {}

    static final String NAME = "name";
    static final String VERSION = "version";
  }

  @VisibleForTesting
  static final long CURRENT_VERSION = 1L;

  @VisibleForTesting
  static final String ID = "application/x.triplea.game-data-memento";

  /**
   * An immutable collection of default memento export options.
   *
   * <p>
   * The key is the option name. The value is the option value.
   * </p>
   */
  public static final Map<ExportOptionName, Object> DEFAULT_EXPORT_OPTIONS_BY_NAME = newDefaultExportOptionsByName();

  private GameDataMemento() {}

  private static Map<ExportOptionName, Object> newDefaultExportOptionsByName() {
    final EnumMap<ExportOptionName, Object> options = new EnumMap<>(ExportOptionName.class);
    options.put(ExportOptionName.EXCLUDE_DELEGATES, false);
    return Collections.unmodifiableMap(options);
  }

  /**
   * Creates a new game data memento exporter using the default export options.
   *
   * @return A new game data memento exporter; never {@code null}.
   */
  public static MementoExporter<GameData> newExporter() {
    return newExporter(DEFAULT_EXPORT_OPTIONS_BY_NAME);
  }

  /**
   * Creates a new game data memento exporter using the specified export options.
   *
   * @param optionsByName The memento export options; must not be {@code null}. The key is the option name. The value is
   *        the option value.
   *
   * @return A new game data memento exporter; never {@code null}.
   */
  public static MementoExporter<GameData> newExporter(final Map<ExportOptionName, Object> optionsByName) {
    checkNotNull(optionsByName);

    return new PropertyBagMementoExporter<>(ID, CURRENT_VERSION, new ExportHandlers(optionsByName));
  }

  /**
   * Names the options that can be specified when creating a new game data memento exporter.
   */
  public static enum ExportOptionName {
    /**
     * Indicates delegates should be excluded from the memento (value type: {@code Boolean}).
     */
    EXCLUDE_DELEGATES
  }

  private static final class ExportHandlers implements PropertyBagMementoExporter.HandlerSupplier<GameData> {
    private final Map<Long, PropertyBagMementoExporter.Handler<GameData>> handlersByVersion = getHandlersByVersion();

    // TODO: add support for options
    @SuppressWarnings("unused")
    private final Map<ExportOptionName, Object> optionsByName;

    ExportHandlers(final Map<ExportOptionName, Object> optionsByName) {
      this.optionsByName = new HashMap<>(optionsByName);
    }

    private Map<Long, PropertyBagMementoExporter.Handler<GameData>> getHandlersByVersion() {
      final Map<Long, PropertyBagMementoExporter.Handler<GameData>> handlersByVersion = new HashMap<>();
      handlersByVersion.put(1L, this::exportPropertiesV1);
      return Collections.unmodifiableMap(handlersByVersion);
    }

    private void exportPropertiesV1(final GameData gameData, final Map<String, Object> propertiesByName) {
      propertiesByName.put(PropertyNames.NAME, gameData.getGameName());
      propertiesByName.put(PropertyNames.VERSION, gameData.getGameVersion());
      // TODO: handle remaining properties
    }

    @Override
    public Optional<PropertyBagMementoExporter.Handler<GameData>> getHandler(final long version) {
      return Optional.ofNullable(handlersByVersion.get(version));
    }
  }

  /**
   * Creates a new game data memento importer.
   *
   * @return A new game data memento importer; never {@code null}.
   */
  public static MementoImporter<GameData> newImporter() {
    return new PropertyBagMementoImporter<>(ID, new ImportHandlers());
  }

  private static final class ImportHandlers implements PropertyBagMementoImporter.HandlerSupplier<GameData> {
    private final Map<Long, PropertyBagMementoImporter.Handler<GameData>> handlersByVersion = getHandlersByVersion();

    private Map<Long, PropertyBagMementoImporter.Handler<GameData>> getHandlersByVersion() {
      final Map<Long, PropertyBagMementoImporter.Handler<GameData>> handlersByVersion = new HashMap<>();
      handlersByVersion.put(1L, this::importPropertiesV1);
      return Collections.unmodifiableMap(handlersByVersion);
    }

    private GameData importPropertiesV1(final Map<String, Object> propertiesByName) throws MementoImportException {
      final GameData gameData = new GameData();
      gameData.setGameName(getRequiredProperty(propertiesByName, PropertyNames.NAME, String.class));
      gameData.setGameVersion(getRequiredProperty(propertiesByName, PropertyNames.VERSION, Version.class));
      // TODO: handle remaining properties
      return gameData;
    }

    private static <T> T getRequiredProperty(
        final Map<String, Object> propertiesByName,
        final String name,
        final Class<T> type) throws MementoImportException {
      assert propertiesByName != null;
      assert name != null;
      assert type != null;

      if (!propertiesByName.containsKey(name)) {
        throw new MementoImportException(String.format("memento is missing required property '%s'", name));
      }

      try {
        return type.cast(propertiesByName.get(name));
      } catch (final ClassCastException e) {
        throw new MementoImportException(String.format("memento property '%s' has wrong type", name), e);
      }
    }

    @Override
    public Optional<PropertyBagMementoImporter.Handler<GameData>> getHandler(final long version) {
      return Optional.ofNullable(handlersByVersion.get(version));
    }
  }
}
