package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.DestroyerBombardTechAdvance;

/**
 * A serializable proxy for the {@link DestroyerBombardTechAdvance} class.
 */
@Immutable
public final class DestroyerBombardTechAdvanceProxy extends AbstractTechAdvanceProxy<DestroyerBombardTechAdvance> {
  public DestroyerBombardTechAdvanceProxy() {
    super(DestroyerBombardTechAdvance.class, DestroyerBombardTechAdvance::new);
  }
}
