package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxy;
import games.strategy.triplea.delegate.ImprovedArtillerySupportAdvance;

/**
 * A serializable proxy for the {@link ImprovedArtillerySupportAdvance} class.
 */
@Immutable
public final class ImprovedArtillerySupportAdvanceProxy
    extends AbstractTechAdvanceProxy<ImprovedArtillerySupportAdvance> {
  public ImprovedArtillerySupportAdvanceProxy() {
    super(ImprovedArtillerySupportAdvance.class, ImprovedArtillerySupportAdvance::new);
  }
}
