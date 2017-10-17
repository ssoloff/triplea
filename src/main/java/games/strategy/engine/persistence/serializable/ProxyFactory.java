package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Supplier;

/**
 * A factory for creating a serializable proxy for a non-serializable principal.
 */
public interface ProxyFactory {
  /**
   * Gets the type of the principal to be proxied.
   *
   * @return The type of the principal to be proxied.
   */
  Class<?> getPrincipalType();

  /**
   * Creates a new proxy for principals of the supported type.
   *
   * @return A new proxy.
   */
  Proxy newProxy();

  /**
   * Creates a new proxy factory using the default implementation.
   *
   * @param principalType The type of the principal to be proxied.
   * @param newProxy The factory method used to create a new proxy.
   *
   * @return A new proxy factory. The returned factory is immutable.
   */
  static ProxyFactory newInstance(final Class<?> principalType, final Supplier<Proxy> newProxy) {
    checkNotNull(principalType);
    checkNotNull(newProxy);

    return new DefaultProxyFactory(principalType, newProxy);
  }
}
