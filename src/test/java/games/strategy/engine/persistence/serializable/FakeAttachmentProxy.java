package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import games.strategy.engine.data.FakeAttachment;

/**
 * A serializable proxy for the {@link FakeAttachment} class.
 */
public final class FakeAttachmentProxy extends AbstractHeaderlessProxy<FakeAttachment> {
  public static final ProxyFactory FACTORY = ProxyFactory.newInstance(FakeAttachment.class, FakeAttachmentProxy::new);

  public FakeAttachmentProxy() {
    super(FakeAttachment.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  @Override
  protected Writer newTypeSafeWriterFor(final FakeAttachment principal) {
    return new Writer(principal);
  }

  private final class Reader extends AbstractReader {
    @Override
    public Object readBody(final Context context) throws IOException {
      checkNotNull(context);

      final String name = context.getInputStream().readUTF();

      return new FakeAttachment(name);
    }
  }

  private final class Writer extends AbstractWriter {
    Writer(final FakeAttachment fakeAttachment) {
      super(fakeAttachment);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      checkNotNull(context);

      context.getOutputStream().writeUTF(getPrincipal().getName());
    }
  }
}
