package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.IndustrialTechnologyAdvance;

/**
 * A serializable proxy for the {@link IndustrialTechnologyAdvance} class.
 */
@Immutable
public final class IndustrialTechnologyAdvanceProxy extends AbstractTechAdvanceProxy<IndustrialTechnologyAdvance> {
  public IndustrialTechnologyAdvanceProxy() {
    super(IndustrialTechnologyAdvance.class, IndustrialTechnologyAdvance::new);
  }
}
