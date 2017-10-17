package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.IndustrialTechnologyAdvance;

public final class IndustrialTechnologyAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<IndustrialTechnologyAdvance> {
  public IndustrialTechnologyAdvanceProxyAsProxyTest() {
    super(
        IndustrialTechnologyAdvance.class,
        new IndustrialTechnologyAdvanceProxy(),
        IndustrialTechnologyAdvance.TECH_NAME_INDUSTRIAL_TECHNOLOGY,
        IndustrialTechnologyAdvance::new);
  }
}
