package games.strategy.util.memento;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link MementoExporter} for instances of {@link PropertyBagMemento}.
 *
 * @param <T> The type of the memento originator.
 */
public final class PropertyBagMementoExporter<T> implements MementoExporter<T> {
  private final long defaultVersion;

  private final HandlerSupplier<T> handlerSupplier;

  private final String id;

  /**
   * Initializes a new instance of the {@code PropertyBagMementoExporter} class.
   *
   * @param id The identifier of the memento to export; must not be {@code null}.
   * @param defaultVersion The default version of the memento to export when the version is not explicitly specified.
   * @param handlerSupplier A supplier of export handlers for each supported version; must not be {@code null}.
   */
  public PropertyBagMementoExporter(
      final String id,
      final long defaultVersion,
      final HandlerSupplier<T> handlerSupplier) {
    checkNotNull(id);
    checkNotNull(handlerSupplier);

    this.defaultVersion = defaultVersion;
    this.handlerSupplier = handlerSupplier;
    this.id = id;
  }

  @Override
  public PropertyBagMemento exportMemento(final T originator) throws MementoExportException {
    return exportMemento(originator, defaultVersion);
  }

  /**
   * Exports a memento from the specified originator using the specified version of the memento.
   *
   * @param originator The memento originator; must not be {@code null}.
   * @param version The version of the memento to export.
   *
   * @return The exported memento; never {@code null}.
   *
   * @throws MementoExportException If an error occurs while exporting the memento.
   */
  public PropertyBagMemento exportMemento(final T originator, final long version) throws MementoExportException {
    checkNotNull(originator);

    final Handler<T> handler = handlerSupplier.getHandler(version)
        .orElseThrow(() -> newUnsupportedVersionException(version));
    final Map<String, Object> propertiesByName = new HashMap<>();
    handler.exportProperties(originator, propertiesByName);
    return new PropertyBagMemento(id, version, propertiesByName);
  }

  private static MementoExportException newUnsupportedVersionException(final long version) {
    return new MementoExportException(String.format("version %d is unsupported", version));
  }

  /**
   * A memento export handler.
   *
   * @param <T> The type of the memento originator.
   */
  @FunctionalInterface
  public interface Handler<T> {
    /**
     * Exports the properties of the specified originator to the specified collection.
     *
     * @param originator The memento originator; must not be {@code null}.
     * @param propertiesByName The collection that receives the originator properties; must not be {@code null}. The key
     *        is the property name. The value is the property value.
     *
     * @throws MementoExportException If an error occurs while exporting the originator properties.
     */
    void exportProperties(T originator, Map<String, Object> propertiesByName) throws MementoExportException;
  }

  /**
   * Supplies memento export handlers for each supported version.
   *
   * @param <T> The type of the memento originator.
   */
  @FunctionalInterface
  public interface HandlerSupplier<T> {
    /**
     * Gets the memento export handler for the specified version.
     *
     * @param version The memento version to export.
     *
     * @return The memento export handler for the specified version or empty if no handler is available; never
     *         {@code null}.
     */
    Optional<Handler<T>> getHandler(long version);
  }
}
