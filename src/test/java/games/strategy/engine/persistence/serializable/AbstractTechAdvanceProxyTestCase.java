package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Function;

import games.strategy.engine.data.GameData;
import games.strategy.triplea.delegate.TechAdvance;

/**
 * A fixture for testing the basic aspects of proxies whose principal is of type {@link TechAdvance}.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractTechAdvanceProxyTestCase<T extends TechAdvance>
    extends AbstractNamedAttachableProxyTestCase<T> {
  private final Function<GameData, T> newPrincipal;
  private final String principalName;

  protected AbstractTechAdvanceProxyTestCase(
      final Class<T> principalType,
      final Proxy proxy,
      final String principalName,
      final Function<GameData, T> nePrincipal) {
    super(principalType, proxy);

    checkNotNull(principalName);
    checkNotNull(nePrincipal);

    this.newPrincipal = nePrincipal;
    this.principalName = principalName;
  }

  @Override
  protected final String getPrincipalName() {
    return principalName;
  }

  @Override
  protected final T createPrincipalHeader(final GameData gameData) {
    return newPrincipal.apply(gameData);
  }

  @Override
  protected final T createPrincipalBody(final GameData gameData) {
    final T principal = newPrincipal.apply(gameData);
    addAttachments(principal);
    return principal;
  }
}
