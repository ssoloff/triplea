package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.MechanizedInfantryAdvance;

public final class MechanizedInfantryAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<MechanizedInfantryAdvance> {
  public MechanizedInfantryAdvanceProxyAsProxyTest() {
    super(
        MechanizedInfantryAdvance.class,
        new MechanizedInfantryAdvanceProxy(),
        MechanizedInfantryAdvance.TECH_NAME_MECHANIZED_INFANTRY,
        MechanizedInfantryAdvance::new);
  }
}
