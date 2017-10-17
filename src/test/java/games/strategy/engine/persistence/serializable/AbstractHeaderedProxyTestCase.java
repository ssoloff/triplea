package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.GameData;

/**
 * A fixture for testing the basic aspects of proxy classes that have a header.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractHeaderedProxyTestCase<T> extends AbstractProxyTestCase<T> {
  /** The name that should be used for the principal under test. */
  protected static final String PRINCIPAL_NAME = "principalUnderTestName";

  protected AbstractHeaderedProxyTestCase(final Class<T> principalType, final Proxy proxy) {
    super(principalType, proxy);
  }

  /**
   * Gets the name of the principal under test.
   *
   * <p>
   * The default implementation returns {@link #PRINCIPAL_NAME}. Subclasses may override and are not required to call
   * the superclass implementation.
   * </p>
   *
   * @return The name of the principal under test.
   */
  protected String getPrincipalName() {
    return PRINCIPAL_NAME;
  }

  @Test
  public void shouldBeAbleToRoundTripPrincipalHeader() throws Exception {
    final GameData gameData1 = new GameData();
    final T principal1 = createPrincipalHeader(gameData1);
    final byte[] bytes = writePrincipalHeader(principal1);

    final GameData gameData2 = new GameData();
    readPrincipalHeader(bytes, gameData2);
    final PrincipalRegistry principalRegistry2 = new PrincipalRegistry(gameData2);
    final T principal2 = principalRegistry2.getPrincipal(getPrincipalName(), Arrays.asList(getPrincipalType()));

    assertThat(principal2, is(notNullValue()));
    assertPrincipalHeaderEquals(principal1, principal2);
  }

  /**
   * Creates a new principal with all header fields initialized.
   *
   * @param gameData The game data that owns the principal.
   *
   * @return A new principal.
   */
  protected abstract T createPrincipalHeader(GameData gameData);

  /**
   * Asserts that the specified principal headers are equal.
   *
   * @param expected The expected principal header.
   * @param actual The actual principal header.
   *
   * @throws AssertionError If the two principal headers are not equal.
   */
  protected abstract void assertPrincipalHeaderEquals(T expected, T actual);

  @Test
  public final void shouldBeAbleToRoundTripPrincipalBody() throws Exception {
    final GameData gameData1 = new GameData();
    final T principal1 = createPrincipalBody(gameData1);
    final byte[] bytes = writePrincipalBody(principal1);

    final GameData gameData2 = new GameData();
    final T principal2 = createPrincipalHeader(gameData2);
    final PrincipalRegistry principalRegistry2 = new PrincipalRegistry(gameData2);
    principalRegistry2.registerPrincipal(principal2);
    for (final Object principalDependency : getPrincipalDependencies(gameData2)) {
      principalRegistry2.registerPrincipal(principalDependency);
    }
    readPrincipalBody(bytes, gameData2);

    assertPrincipalBodyEquals(principal1, principal2);
  }

  /**
   * Creates a new principal with all body fields initialized.
   *
   * @param gameData The game data that owns the principal.
   *
   * @return A new principal.
   */
  protected abstract T createPrincipalBody(GameData gameData);

  /**
   * Asserts that the specified principal bodies are equal.
   *
   * @param expected The expected principal body.
   * @param actual The actual principal body.
   *
   * @throws AssertionError If the two principal bodies are not equal.
   */
  protected abstract void assertPrincipalBodyEquals(T expected, T actual);
}
