package games.strategy.engine.framework.persistence.serializable;

/**
 * A checked exception that indicates an error occurred while importing a game data memento in Java object serialization
 * format.
 *
 * <p>
 * Instances of this class are thread safe.
 * </p>
 */
public final class SerializableGameDataMementoImportException extends Exception {
  private static final long serialVersionUID = -7287818574818441725L;

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoImportException} class with no detail message
   * and no cause.
   */
  public SerializableGameDataMementoImportException() {}

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoImportException} class with the specified
   * detail message and no cause.
   *
   * @param message The detail message; may be {@code null}.
   */
  public SerializableGameDataMementoImportException(final String message) {
    super(message);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoImportException} class with no detail message
   * and the specified cause.
   *
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataMementoImportException(final Throwable cause) {
    super(cause);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataMementoImportException} class with the specified
   * detail message and the specified cause.
   *
   * @param message The detail message; may be {@code null}.
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataMementoImportException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
