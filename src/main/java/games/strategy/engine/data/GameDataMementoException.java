package games.strategy.engine.data;

/**
 * A checked exception that indicates a game data memento could not be restored.
 *
 * <p>
 * Instances of this class are thread safe.
 * </p>
 */
public final class GameDataMementoException extends Exception {
  private static final long serialVersionUID = 6840662926677527135L;

  /**
   * Initializes a new instance of the {@code GameDataMementoException} class with no detail message and no cause.
   */
  public GameDataMementoException() {}

  /**
   * Initializes a new instance of the {@code GameDataMementoException} class with the specified detail message and no
   * cause.
   *
   * @param message The detail message; may be {@code null}.
   */
  public GameDataMementoException(final String message) {
    super(message);
  }

  /**
   * Initializes a new instance of the {@code GameDataMementoException} class with no detail message and the specified
   * cause.
   *
   * @param cause The cause; may be {@code null}.
   */
  public GameDataMementoException(final Throwable cause) {
    super(cause);
  }

  /**
   * Initializes a new instance of the {@code GameDataMementoException} class with the specified detail message and the
   * specified cause.
   *
   * @param message The detail message; may be {@code null}.
   * @param cause The cause; may be {@code null}.
   */
  public GameDataMementoException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
