package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import games.strategy.engine.data.DefaultNamed;

/**
 * A fixture for testing the basic aspects of proxies whose principal is of type {@link DefaultNamed}.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractDefaultNamedProxyTestCase<T extends DefaultNamed>
    extends AbstractHeaderedProxyTestCase<T> {
  protected AbstractDefaultNamedProxyTestCase(final Class<T> principalType, final Proxy proxy) {
    super(principalType, proxy);
  }

  /**
   * Subclasses may override and must call the superclass implementation.
   */
  @Override
  protected void assertPrincipalHeaderEquals(final T expected, final T actual) {
    assertThat(actual.getName(), is(expected.getName()));
  }
}
