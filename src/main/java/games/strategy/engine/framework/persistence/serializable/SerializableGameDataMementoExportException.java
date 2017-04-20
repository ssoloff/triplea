package games.strategy.engine.framework.persistence.serializable;

/**
 * A checked exception that indicates an error occurred while exporting a game data memento in Java object serialization
 * format.
 *
 * <p>
 * Instances of this class are thread safe.
 * </p>
 */
public final class SerializableGameDataMementoExportException extends Exception {
  private static final long serialVersionUID = 3845727791305899485L;

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoExportException} class with no detail message
   * and no cause.
   */
  public SerializableGameDataMementoExportException() {}

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoExportException} class with the specified
   * detail message and no cause.
   *
   * @param message The detail message; may be {@code null}.
   */
  public SerializableGameDataMementoExportException(final String message) {
    super(message);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoExportException} class with no detail message
   * and the specified cause.
   *
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataMementoExportException(final Throwable cause) {
    super(cause);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoExportException} class with the specified
   * detail message and the specified cause.
   *
   * @param message The detail message; may be {@code null}.
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataMementoExportException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
