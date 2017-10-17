package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

/**
 * Superclass for serializable proxies.
 *
 * @param <T> The type of the principal to be proxied.
 */
@Immutable
public abstract class AbstractProxy<T> implements Proxy {
  private final Class<T> principalType;

  protected AbstractProxy(final Class<T> principalType) {
    checkNotNull(principalType);

    this.principalType = principalType;
  }

  protected final Class<T> getPrincipalType() {
    return principalType;
  }

  @Override
  public final Reader newReader() {
    return newTypeSafeReader();
  }

  /**
   * Creates a new proxy reader.
   *
   * @return A new proxy reader.
   */
  protected abstract Reader newTypeSafeReader();

  /**
   * Superclass for serializable proxy readers.
   */
  protected abstract class AbstractReader implements Reader {
    protected AbstractReader() {}
  }

  @Override
  public final Writer newWriterFor(final Object principal) {
    checkNotNull(principal);

    return newTypeSafeWriterFor(principalType.cast(principal));
  }

  /**
   * Creates a new proxy writer for the specified principal.
   *
   * @param principal The principal whose proxy is to be written.
   *
   * @return A new proxy writer.
   */
  protected abstract Writer newTypeSafeWriterFor(final T principal);

  /**
   * Superclass for serializable proxy writers.
   */
  protected abstract class AbstractWriter implements Writer {
    private final T principal;

    protected AbstractWriter(final T principal) {
      checkNotNull(principal);

      this.principal = principal;
    }

    protected final T getPrincipal() {
      return principal;
    }
  }
}
