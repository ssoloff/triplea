package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.DestroyerBombardTechAdvance;

public final class DestroyerBombardTechAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<DestroyerBombardTechAdvance> {
  public DestroyerBombardTechAdvanceProxyAsProxyTest() {
    super(
        DestroyerBombardTechAdvance.class,
        new DestroyerBombardTechAdvanceProxy(),
        DestroyerBombardTechAdvance.TECH_NAME_DESTROYER_BOMBARD,
        DestroyerBombardTechAdvance::new);
  }
}
