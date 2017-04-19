package games.strategy.engine.framework.persistence.serializable;

/**
 * A checked exception that indicates an error occurred while exporting game data in Java object serialization format.
 *
 * <p>
 * Instances of this class are thread safe.
 * </p>
 */
public final class SerializableGameDataExportException extends Exception {
  private static final long serialVersionUID = -4082693744154871283L;

  /**
   * Initializes a new instance of the {@code SerializableGameDataExportException} class with no detail message and no
   * cause.
   */
  public SerializableGameDataExportException() {}

  /**
   * Initializes a new instance of the {@code SerializableGameDataExportException} class with the specified detail
   * message and no cause.
   *
   * @param message The detail message; may be {@code null}.
   */
  public SerializableGameDataExportException(final String message) {
    super(message);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataExportException} class with no detail message and the
   * specified cause.
   *
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataExportException(final Throwable cause) {
    super(cause);
  }

  /**
   * Initializes a new instance of the {@code SerializableGameDataExportException} class with the specified detail
   * message and the specified cause.
   *
   * @param message The detail message; may be {@code null}.
   * @param cause The cause; may be {@code null}.
   */
  public SerializableGameDataExportException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
