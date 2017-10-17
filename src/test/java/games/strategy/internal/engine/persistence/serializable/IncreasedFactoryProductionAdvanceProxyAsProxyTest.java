package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.IncreasedFactoryProductionAdvance;

public final class IncreasedFactoryProductionAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<IncreasedFactoryProductionAdvance> {
  public IncreasedFactoryProductionAdvanceProxyAsProxyTest() {
    super(
        IncreasedFactoryProductionAdvance.class,
        new IncreasedFactoryProductionAdvanceProxy(),
        IncreasedFactoryProductionAdvance.TECH_NAME_INCREASED_FACTORY_PRODUCTION,
        IncreasedFactoryProductionAdvance::new);
  }
}
