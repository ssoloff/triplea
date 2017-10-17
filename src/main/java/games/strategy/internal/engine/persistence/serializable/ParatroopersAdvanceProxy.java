package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.ParatroopersAdvance;

/**
 * A serializable proxy for the {@link ParatroopersAdvance} class.
 */
@Immutable
public final class ParatroopersAdvanceProxy extends AbstractTechAdvanceProxy<ParatroopersAdvance> {
  public ParatroopersAdvanceProxy() {
    super(ParatroopersAdvance.class, ParatroopersAdvance::new);
  }
}
