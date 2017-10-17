package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.JetPowerAdvance;

/**
 * A serializable proxy for the {@link JetPowerAdvance} class.
 */
@Immutable
public final class JetPowerAdvanceProxy extends AbstractTechAdvanceProxy<JetPowerAdvance> {
  public JetPowerAdvanceProxy() {
    super(JetPowerAdvance.class, JetPowerAdvance::new);
  }
}
