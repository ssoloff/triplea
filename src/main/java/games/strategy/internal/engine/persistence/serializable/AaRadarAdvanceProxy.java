package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.AARadarAdvance;

/**
 * A serializable proxy for the {@link AARadarAdvance} class.
 */
@Immutable
public final class AaRadarAdvanceProxy extends AbstractTechAdvanceProxy<AARadarAdvance> {
  public AaRadarAdvanceProxy() {
    super(AARadarAdvance.class, AARadarAdvance::new);
  }
}
