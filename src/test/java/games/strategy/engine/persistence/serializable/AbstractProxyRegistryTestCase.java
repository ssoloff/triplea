package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the {@link ProxyRegistry} interface.
 */
public abstract class AbstractProxyRegistryTestCase {
  protected AbstractProxyRegistryTestCase() {}

  /**
   * Creates the proxy registry to be tested.
   *
   * @param proxyFactories The collection of proxy factories to associate with the registry.
   *
   * @return The proxy registry to be tested.
   */
  protected abstract ProxyRegistry createProxyRegistry(Collection<ProxyFactory> proxyFactories);

  @Test
  public final void getProxyFor_ShouldDelegateToProxyFactoryWhenProxyFactoryRegisteredForPrincipalType() {
    final Class<?> principalType = Integer.class;
    final Proxy expectedProxy = mock(Proxy.class);
    final ProxyFactory proxyFactory = givenProxyFactoryFor(principalType, expectedProxy);
    final ProxyRegistry proxyRegistry = newProxyRegistry(proxyFactory);

    final Proxy actualProxy = proxyRegistry.getProxyFor(principalType);

    verify(proxyFactory).newProxy();
    assertThat(actualProxy, is(expectedProxy));
  }

  private static ProxyFactory givenProxyFactoryFor(final Class<?> principalType, final Proxy proxy) {
    final ProxyFactory proxyFactory = mock(ProxyFactory.class);
    doReturn(principalType).when(proxyFactory).getPrincipalType();
    doReturn(proxy).when(proxyFactory).newProxy();
    return proxyFactory;
  }

  private ProxyRegistry newProxyRegistry(final ProxyFactory... proxyFactories) {
    return createProxyRegistry(Arrays.asList(proxyFactories));
  }

  @Test
  public final void getProxyFor_ShouldReturnDefaultProxyWhenNoProxyFactoryRegisteredForPrincipalType() {
    final ProxyRegistry proxyRegistry = newProxyRegistry();

    final Proxy proxy = proxyRegistry.getProxyFor(Object.class);

    assertThrows(UnsupportedOperationException.class, () -> proxy.newReader());
    assertThrows(UnsupportedOperationException.class, () -> proxy.newWriterFor(new Object()));
  }
}
