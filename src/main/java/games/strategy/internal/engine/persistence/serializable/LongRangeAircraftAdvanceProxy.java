package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.LongRangeAircraftAdvance;

/**
 * A serializable proxy for the {@link LongRangeAircraftAdvance} class.
 */
@Immutable
public final class LongRangeAircraftAdvanceProxy extends AbstractTechAdvanceProxy<LongRangeAircraftAdvance> {
  public LongRangeAircraftAdvanceProxy() {
    super(LongRangeAircraftAdvance.class, LongRangeAircraftAdvance::new);
  }
}
