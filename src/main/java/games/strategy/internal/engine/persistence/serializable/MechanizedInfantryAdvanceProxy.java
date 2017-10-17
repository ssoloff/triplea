package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.MechanizedInfantryAdvance;

/**
 * A serializable proxy for the {@link MechanizedInfantryAdvance} class.
 */
@Immutable
public final class MechanizedInfantryAdvanceProxy extends AbstractTechAdvanceProxy<MechanizedInfantryAdvance> {
  public MechanizedInfantryAdvanceProxy() {
    super(MechanizedInfantryAdvance.class, MechanizedInfantryAdvance::new);
  }
}
