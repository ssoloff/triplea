package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.SuperSubsAdvance;

/**
 * A serializable proxy for the {@link SuperSubsAdvance} class.
 */
@Immutable
public final class SuperSubsAdvanceProxy extends AbstractTechAdvanceProxy<SuperSubsAdvance> {
  public SuperSubsAdvanceProxy() {
    super(SuperSubsAdvance.class, SuperSubsAdvance::new);
  }
}
