package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.persistence.serializable.AbstractTechAdvanceProxyTestCase;
import games.strategy.triplea.delegate.ImprovedArtillerySupportAdvance;

public final class ImprovedArtillerySupportAdvanceProxyAsProxyTest
    extends AbstractTechAdvanceProxyTestCase<ImprovedArtillerySupportAdvance> {
  public ImprovedArtillerySupportAdvanceProxyAsProxyTest() {
    super(
        ImprovedArtillerySupportAdvance.class,
        new ImprovedArtillerySupportAdvanceProxy(),
        ImprovedArtillerySupportAdvance.TECH_NAME_IMPROVED_ARTILLERY_SUPPORT,
        ImprovedArtillerySupportAdvance::new);
  }
}
