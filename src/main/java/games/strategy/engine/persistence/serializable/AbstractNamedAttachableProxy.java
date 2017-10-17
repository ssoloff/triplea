package games.strategy.engine.persistence.serializable;

import java.io.IOException;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.IAttachment;
import games.strategy.engine.data.NamedAttachable;

/**
 * Superclass for serializable proxies whose principal is of type {@link NamedAttachable}.
 *
 * @param <T> The type of the principal to be proxied.
 */
@Immutable
public abstract class AbstractNamedAttachableProxy<T extends NamedAttachable> extends AbstractDefaultNamedProxy<T> {
  protected AbstractNamedAttachableProxy(final Class<T> principalType) {
    super(principalType);
  }

  /**
   * The header for proxies whose principal is of type {@link NamedAttachable}.
   */
  protected static class NamedAttachableHeader extends DefaultNamedHeader {
  }

  /**
   * Superclass for serializable proxy readers whose principal is of type {@link NamedAttachable}.
   *
   * @param <HeaderT> The type of the proxy header.
   */
  protected abstract class AbstractReader<HeaderT extends NamedAttachableHeader>
      extends AbstractDefaultNamedProxy<T>.AbstractReader<HeaderT> {
    /**
     * This constructor uses proxy headers of type {@link NamedAttachableHeader}. If a subclass specifies a different
     * proxy header type, a {@link ClassCastException} will be thrown at runtime.
     */
    @SuppressWarnings("unchecked")
    protected AbstractReader() {
      this(() -> (HeaderT) new NamedAttachableHeader());
    }

    protected AbstractReader(final Supplier<HeaderT> newHeader) {
      super(newHeader);
    }

    @Override
    protected void readBody(final Context context, final T principal) throws IOException, ClassNotFoundException {
      readAttachments(context, principal);
    }

    private void readAttachments(final Context context, final T principal) throws IOException, ClassNotFoundException {
      ProxyUtils.readProxyBodyCollection(context, IAttachment.class).forEach(attachment -> {
        attachment.setAttachedTo(principal);
        principal.addAttachment(attachment.getName(), attachment);
        // TODO: need to also register with GameData just as GameParser does??
        //
        // it may be necessary to ensure attachment instances with the same name don't get duplicated
        // in that case, we may have to register attachments like any other principal so they are stored
        // in the GameData attachment->option map like they would be after freshly parsing XML. not sure
        // if that means attachments really do need to be proxied with both a header and a body.
      });
    }
  }

  /**
   * Superclass for serializable proxy writers whose principal is of type {@link NamedAttachable}.
   */
  protected abstract class AbstractWriter extends AbstractDefaultNamedProxy<T>.AbstractWriter {
    protected AbstractWriter(final T principal) {
      super(principal);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      super.writeBody(context);

      writeAttachments(context);
    }

    private void writeAttachments(final Context context) throws IOException {
      ProxyUtils.writeProxyBodyCollection(context, getPrincipal().getAttachments().values());
    }
  }
}
