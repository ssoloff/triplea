package games.strategy.engine.framework.persistence.serializable;

/**
 * A collection of constants for the Java object serialization layer for game data.
 */
final class SerializableConstants {
  /** The current version of the serializable stream format. */
  static final long CURRENT_VERSION = 1L;

  /** The MIME type of the serializable stream format. */
  static final String MIME_TYPE = "application/x.triplea.game+serializable";

  private SerializableConstants() {}
}
