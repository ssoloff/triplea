package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.JetPowerAdvance;

public final class JetPowerAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<JetPowerAdvance> {
  public JetPowerAdvanceProxyAsProxyTest() {
    super(
        JetPowerAdvance.class,
        new JetPowerAdvanceProxy(),
        JetPowerAdvance.TECH_NAME_JET_POWER,
        JetPowerAdvance::new);
  }
}
