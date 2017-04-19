package games.strategy.engine.framework.persistence.serializable;

/**
 * A checked exception that indicates an error occurred while importing game data in Java object serialization format.
 *
 * <p>
 * Instances of this class are thread safe.
 * </p>
 */
public final class SerializableGameDataImportException extends Exception {
  private static final long serialVersionUID = 4879139397562705403L;

  /**
   * Initializes a new instance of the {@code SerializableGameDataImportException} class with no detail message and no
   * cause.
   */
  public SerializableGameDataImportException() {}

  /**
   * Initializes a new instance of the {@code SerializableGameDataImportException} class with the specified detail
   * message and no cause.
   *
   * @param message The detail message; may be {@code null}.
   */
  public SerializableGameDataImportException(final String message) {
    super(message);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataImportException} class with no detail message and the
   * specified cause.
   *
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataImportException(final Throwable cause) {
    super(cause);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataImportException} class with the specified detail
   * message and the specified cause.
   *
   * @param message The detail message; may be {@code null}.
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataImportException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
