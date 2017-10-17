package games.strategy.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

/**
 * Superclass for serializable proxies that do not have a header.
 *
 * @param <T> The type of the principal to be proxied.
 */
@Immutable
public abstract class AbstractHeaderlessProxy<T> extends AbstractProxy<T> {
  protected AbstractHeaderlessProxy(final Class<T> principalType) {
    super(principalType);
  }

  /**
   * Superclass for serializable proxy readers whose proxy does not have a header.
   */
  protected abstract class AbstractReader extends AbstractProxy<T>.AbstractReader {
    protected AbstractReader() {}

    @Override
    public final void readHeader(final Context context) {}
  }

  /**
   * Superclass for serializable proxy writers whose proxy does not have a header.
   */
  protected abstract class AbstractWriter extends AbstractProxy<T>.AbstractWriter {
    protected AbstractWriter(final T principal) {
      super(principal);
    }

    @Override
    public final void writeHeader(final Context context) {}
  }
}
