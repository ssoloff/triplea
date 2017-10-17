package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.FakeAttachment;
import games.strategy.engine.data.IAttachment;
import games.strategy.engine.data.NamedAttachable;

/**
 * A fixture for testing the basic aspects of proxies whose principal is of type {@link NamedAttachable}.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractNamedAttachableProxyTestCase<T extends NamedAttachable>
    extends AbstractDefaultNamedProxyTestCase<T> {
  protected AbstractNamedAttachableProxyTestCase(final Class<T> principalType, final Proxy proxy) {
    super(principalType, proxy);
  }

  /**
   * Subclasses may override and must include the collection returned by the superclass implementation.
   */
  @Override
  protected Collection<ProxyFactory> getProxyFactories() {
    return ImmutableList.<ProxyFactory>builder()
        .addAll(super.getProxyFactories())
        .add(FakeAttachmentProxy.FACTORY)
        .build();
  }

  /**
   * Subclasses may override and must call the superclass implementation.
   */
  @Override
  protected void assertPrincipalBodyEquals(final T expected, final T actual) {
    assertAttachmentsEquals(expected, actual);
  }

  private void assertAttachmentsEquals(final T expected, final T actual) {
    // TODO: can we replace this with just equals() ?
    // if we do that, might need an additional check that attachedTo was set correctly...
    final Map<String, IAttachment> expectedAttachments = expected.getAttachments();
    final Map<String, IAttachment> actualAttachments = actual.getAttachments();
    assertThat(actualAttachments.size(), is(expectedAttachments.size()));
    expectedAttachments.forEach((name, expectedAttachment) -> {
      final @Nullable IAttachment actualAttachment = actualAttachments.get(name);
      assertThat(actualAttachment, is(notNullValue()));
      assertThat(actualAttachment.getClass(), is(expectedAttachment.getClass()));
      assertThat(actualAttachment.getName(), is(expectedAttachment.getName()));
      assertThat(actualAttachment.getAttachedTo(), is(sameInstance(actual)));
    });
  }

  // TODO: refactor this class so that subclasses don't have to call this method explicitly.
  // create a new template method if necessary.
  /**
   * Adds a collection of attachments to the specified principal.
   *
   * @param principal The principal that will receive the attachments.
   */
  protected final void addAttachments(final T principal) {
    Arrays.asList(
        new FakeAttachment("attachmentName1"),
        new FakeAttachment("attachmentName2"))
        .forEach(it -> principal.addAttachment(it.getName(), it));
  }

  // TODO:
  // - need to consider adding a test that ensures the attachments are added to the global state in GameData
  // (i.e. see what GameParser does)
}
