package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.WarBondsAdvance;

/**
 * A serializable proxy for the {@link WarBondsAdvance} class.
 */
@Immutable
public final class WarBondsAdvanceProxy extends AbstractTechAdvanceProxy<WarBondsAdvance> {
  public WarBondsAdvanceProxy() {
    super(WarBondsAdvance.class, WarBondsAdvance::new);
  }
}
