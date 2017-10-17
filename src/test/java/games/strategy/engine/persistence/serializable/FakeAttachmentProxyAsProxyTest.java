package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import games.strategy.engine.data.FakeAttachment;
import games.strategy.engine.data.GameData;

public final class FakeAttachmentProxyAsProxyTest extends AbstractHeaderlessProxyTestCase<FakeAttachment> {
  public FakeAttachmentProxyAsProxyTest() {
    super(FakeAttachment.class, new FakeAttachmentProxy());
  }

  @Override
  protected FakeAttachment createPrincipal(final GameData gameData) {
    return new FakeAttachment("name");
  }

  @Override
  protected void assertPrincipalEquals(final FakeAttachment expected, final FakeAttachment actual) {
    assertThat(actual.getName(), is(expected.getName()));
  }
}
