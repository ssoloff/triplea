package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.IncreasedFactoryProductionAdvance;

/**
 * A serializable proxy for the {@link IncreasedFactoryProductionAdvance} class.
 */
@Immutable
public final class IncreasedFactoryProductionAdvanceProxy
    extends AbstractTechAdvanceProxy<IncreasedFactoryProductionAdvance> {
  public IncreasedFactoryProductionAdvanceProxy() {
    super(IncreasedFactoryProductionAdvance.class, IncreasedFactoryProductionAdvance::new);
  }
}
