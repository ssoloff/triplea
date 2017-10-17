package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.RocketsAdvance;

/**
 * A serializable proxy for the {@link RocketsAdvance} class.
 */
@Immutable
public final class RocketsAdvanceProxy extends AbstractTechAdvanceProxy<RocketsAdvance> {
  public RocketsAdvanceProxy() {
    super(RocketsAdvance.class, RocketsAdvance::new);
  }
}
