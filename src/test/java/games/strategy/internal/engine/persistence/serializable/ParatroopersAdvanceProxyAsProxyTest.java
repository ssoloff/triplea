package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.ParatroopersAdvance;

public final class ParatroopersAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<ParatroopersAdvance> {
  public ParatroopersAdvanceProxyAsProxyTest() {
    super(
        ParatroopersAdvance.class,
        new ParatroopersAdvanceProxy(),
        ParatroopersAdvance.TECH_NAME_PARATROOPERS,
        ParatroopersAdvance::new);
  }
}
