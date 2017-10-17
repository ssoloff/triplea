package games.strategy.engine.persistence.serializable;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.GameData;

/**
 * A fixture for testing the basic aspects of proxy classes that do not have a header.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractHeaderlessProxyTestCase<T> extends AbstractProxyTestCase<T> {
  protected AbstractHeaderlessProxyTestCase(final Class<T> principalType, final Proxy proxy) {
    super(principalType, proxy);
  }

  @Test
  public final void shouldBeAbleToRoundTripPrincipal() throws Exception {
    final GameData gameData1 = new GameData();
    final T principal1 = createPrincipal(gameData1);
    final byte[] bytes = writePrincipalBody(principal1);

    final GameData gameData2 = new GameData();
    final PrincipalRegistry principalRegistry2 = new PrincipalRegistry(gameData2);
    for (final Object principalDependency : getPrincipalDependencies(gameData2)) {
      principalRegistry2.registerPrincipal(principalDependency);
    }
    final T principal2 = readPrincipalBody(bytes, gameData2);

    assertPrincipalEquals(principal1, principal2);
  }

  /**
   * Creates a new principal with all fields initialized.
   *
   * @param gameData The game data that owns the principal.
   *
   * @return A new principal.
   */
  protected abstract T createPrincipal(GameData gameData);

  /**
   * Asserts that the specified principals are equal.
   *
   * @param expected The expected principal.
   * @param actual The actual principal.
   *
   * @throws AssertionError If the two principals are not equal.
   */
  protected abstract void assertPrincipalEquals(T expected, T actual);
}
