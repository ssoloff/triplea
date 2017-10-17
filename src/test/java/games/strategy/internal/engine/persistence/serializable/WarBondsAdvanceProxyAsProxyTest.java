package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.WarBondsAdvance;

public final class WarBondsAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<WarBondsAdvance> {
  public WarBondsAdvanceProxyAsProxyTest() {
    super(
        WarBondsAdvance.class,
        new WarBondsAdvanceProxy(),
        WarBondsAdvance.TECH_NAME_WAR_BONDS,
        WarBondsAdvance::new);
  }
}
