package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.ImprovedShipyardsAdvance;

public final class ImprovedShipyardsAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<ImprovedShipyardsAdvance> {
  public ImprovedShipyardsAdvanceProxyAsProxyTest() {
    super(
        ImprovedShipyardsAdvance.class,
        new ImprovedShipyardsAdvanceProxy(),
        ImprovedShipyardsAdvance.TECH_NAME_IMPROVED_SHIPYARDS,
        ImprovedShipyardsAdvance::new);
  }
}
