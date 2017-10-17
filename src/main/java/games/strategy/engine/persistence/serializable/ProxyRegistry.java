package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * A service for obtaining a proxy that can be persisted in place of another object (the principal) within the Java
 * object serialization framework.
 */
public interface ProxyRegistry {
  /**
   * Gets a proxy for the specified type of principal.
   *
   * @param principalType The type of principal to be proxied.
   *
   * @return A proxy for the specified type of principal. If a proxy factory has been registered for the principal type,
   *         a proxy registry will be returned whose operations throw {@link UnsupportedOperationException}.
   */
  Proxy getProxyFor(Class<?> principalType);

  /**
   * Creates a new proxy registry for the specified array of proxy factories using the default implementation.
   *
   * @param proxyFactories The array of proxy factories to associate with the registry.
   *
   * @return A new proxy registry. The returned registry is immutable.
   */
  static ProxyRegistry newInstance(final ProxyFactory... proxyFactories) {
    checkNotNull(proxyFactories);

    return newInstance(Arrays.asList(proxyFactories));
  }

  /**
   * Creates a new proxy registry for the specified collection of proxy factories using the default implementation.
   *
   * @param proxyFactories The collection of proxy factories to associate with the registry.
   *
   * @return A new proxy registry. The returned registry is immutable.
   */
  static ProxyRegistry newInstance(final Collection<ProxyFactory> proxyFactories) {
    checkNotNull(proxyFactories);

    return new DefaultProxyRegistry(proxyFactories);
  }
}
