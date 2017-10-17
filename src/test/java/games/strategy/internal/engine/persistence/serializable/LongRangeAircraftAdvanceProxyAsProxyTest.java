package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.LongRangeAircraftAdvance;

public final class LongRangeAircraftAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<LongRangeAircraftAdvance> {
  public LongRangeAircraftAdvanceProxyAsProxyTest() {
    super(
        LongRangeAircraftAdvance.class,
        new LongRangeAircraftAdvanceProxy(),
        LongRangeAircraftAdvance.TECH_NAME_LONG_RANGE_AIRCRAFT,
        LongRangeAircraftAdvance::new);
  }
}
