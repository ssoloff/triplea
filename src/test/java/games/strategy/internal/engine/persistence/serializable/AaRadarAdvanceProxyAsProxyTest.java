package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.AARadarAdvance;

public final class AaRadarAdvanceProxyAsProxyTest extends AbstractTechAdvanceProxyTestCase<AARadarAdvance> {
  public AaRadarAdvanceProxyAsProxyTest() {
    super(
        AARadarAdvance.class,
        new AaRadarAdvanceProxy(),
        AARadarAdvance.TECH_NAME_AA_RADAR,
        AARadarAdvance::new);
  }
}
