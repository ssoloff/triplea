package games.strategy.util.memento;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link MementoImporter} for instances of {@link PropertyBagMemento}.
 *
 * @param <T> The type of the memento originator.
 */
public final class PropertyBagMementoImporter<T> implements MementoImporter<T> {
  private final HandlerSupplier<T> handlerSupplier;

  private final String id;

  /**
   * Initializes a new instance of the {@code PropertyBagMementoImporter} class.
   *
   * @param id The identifier of the memento to import; must not be {@code null}.
   * @param handlerSupplier A supplier of import handlers for each supported version; must not be {@code null}.
   */
  public PropertyBagMementoImporter(final String id, final HandlerSupplier<T> handlerSupplier) {
    checkNotNull(id);
    checkNotNull(handlerSupplier);

    this.handlerSupplier = handlerSupplier;
    this.id = id;
  }

  @Override
  public T importMemento(final Memento memento) throws MementoImportException {
    checkNotNull(memento);

    if (!(memento instanceof PropertyBagMemento)) {
      throw newUnsupportedMementoException(memento);
    }

    return importMemento((PropertyBagMemento) memento);
  }

  /**
   * Imports the specified memento and returns the originator.
   *
   * @param memento The memento to be imported; must not be {@code null}.
   *
   * @return The memento originator; never {@code null}.
   *
   * @throws MementoImportException If an error occurs while importing the memento.
   */
  public T importMemento(final PropertyBagMemento memento) throws MementoImportException {
    checkNotNull(memento);

    if (!id.equals(memento.getId())) {
      throw newUnsupportedIdException(memento.getId());
    }

    final Handler<T> handler = handlerSupplier.getHandler(memento.getVersion())
        .orElseThrow(() -> newUnsupportedVersionException(memento.getVersion()));
    return handler.importProperties(memento.getPropertiesByName());
  }

  private static MementoImportException newUnsupportedMementoException(final Memento memento) {
    return new MementoImportException(String.format("memento has wrong type: expected '%s' but was '%s'",
        PropertyBagMemento.class.getName(), memento.getClass().getName()));
  }

  private static MementoImportException newUnsupportedIdException(final String id) {
    return new MementoImportException(String.format("ID '%s' is unsupported", id));
  }

  private static MementoImportException newUnsupportedVersionException(final long version) {
    return new MementoImportException(String.format("version %d is unsupported", version));
  }

  /**
   * A memento import handler.
   *
   * @param <T> The type of the memento originator.
   */
  @FunctionalInterface
  public interface Handler<T> {
    /**
     * Imports the specified originator properties and creates a new originator instance.
     *
     * @param propertiesByName The collection of originator properties; must not be {@code null}. The key
     *        is the property name. The value is the property value.
     *
     * @return A new originator instance created from the properties; never {@code null}.
     *
     * @throws MementoImportException If an error occurs while importing the originator properties.
     */
    T importProperties(Map<String, Object> propertiesByName) throws MementoImportException;
  }

  /**
   * Supplies memento import handlers for each supported version.
   *
   * @param <T> The type of the memento originator.
   */
  @FunctionalInterface
  public interface HandlerSupplier<T> {
    /**
     * Gets the memento import handler for the specified version.
     *
     * @param version The memento version to import.
     *
     * @return The memento import handler for the specified version or empty if no handler is available; never
     *         {@code null}.
     */
    Optional<Handler<T>> getHandler(long version);
  }
}
