package games.strategy.engine.persistence.serializable;

import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

@Immutable
final class DefaultProxyFactory implements ProxyFactory {
  private final Supplier<Proxy> newProxy;
  private final Class<?> principalType;

  DefaultProxyFactory(final Class<?> principalType, final Supplier<Proxy> newProxy) {
    this.newProxy = newProxy;
    this.principalType = principalType;
  }

  @Override
  public Class<?> getPrincipalType() {
    return principalType;
  }

  @Override
  public Proxy newProxy() {
    return newProxy.get();
  }
}
