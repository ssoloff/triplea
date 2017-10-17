package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.HeavyBomberAdvance;

/**
 * A serializable proxy for the {@link HeavyBomberAdvance} class.
 */
@Immutable
public final class HeavyBomberAdvanceProxy extends AbstractTechAdvanceProxy<HeavyBomberAdvance> {
  public HeavyBomberAdvanceProxy() {
    super(HeavyBomberAdvance.class, HeavyBomberAdvance::new);
  }
}
