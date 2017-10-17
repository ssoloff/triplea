package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.HeavyBomberAdvance;

public final class HeavyBomberAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<HeavyBomberAdvance> {
  public HeavyBomberAdvanceProxyAsProxyTest() {
    super(
        HeavyBomberAdvance.class,
        new HeavyBomberAdvanceProxy(),
        HeavyBomberAdvance.TECH_NAME_HEAVY_BOMBER,
        HeavyBomberAdvance::new);
  }
}
