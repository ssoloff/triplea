package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.RocketsAdvance;

public final class RocketsAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<RocketsAdvance> {
  public RocketsAdvanceProxyAsProxyTest() {
    super(
        RocketsAdvance.class,
        new RocketsAdvanceProxy(),
        RocketsAdvance.TECH_NAME_ROCKETS,
        RocketsAdvance::new);
  }
}
