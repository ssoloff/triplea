package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.SuperSubsAdvance;

public final class SuperSubsAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<SuperSubsAdvance> {
  public SuperSubsAdvanceProxyAsProxyTest() {
    super(
        SuperSubsAdvance.class,
        new SuperSubsAdvanceProxy(),
        SuperSubsAdvance.TECH_NAME_SUPER_SUBS,
        SuperSubsAdvance::new);
  }
}
