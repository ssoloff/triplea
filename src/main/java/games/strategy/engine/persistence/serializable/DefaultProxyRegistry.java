package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.concurrent.Immutable;

@Immutable
final class DefaultProxyRegistry implements ProxyRegistry {
  private final Map<Class<?>, ProxyFactory> proxyFactoriesByPrincipalType;

  DefaultProxyRegistry(final Collection<ProxyFactory> proxyFactories) {
    proxyFactoriesByPrincipalType = proxyFactories.stream()
        .collect(Collectors.toMap(ProxyFactory::getPrincipalType, Function.identity()));
  }

  @Override
  public Proxy getProxyFor(final Class<?> principalType) {
    checkNotNull(principalType);

    return getProxyFactory(principalType).newProxy();
  }

  private ProxyFactory getProxyFactory(final Class<?> principalType) {
    return proxyFactoriesByPrincipalType.getOrDefault(principalType, new ProxyFactory() {
      @Override
      public Class<?> getPrincipalType() {
        return principalType;
      }

      @Override
      public Proxy newProxy() {
        return new Proxy() {
          @Override
          public Reader newReader() {
            throw new UnsupportedOperationException(String.format("no proxy for principal type '%s'", principalType));
          }

          @Override
          public Writer newWriterFor(final Object principal) {
            throw new UnsupportedOperationException(String.format("no proxy for principal type '%s'", principalType));
          }
        };
      }
    });
  }
}
