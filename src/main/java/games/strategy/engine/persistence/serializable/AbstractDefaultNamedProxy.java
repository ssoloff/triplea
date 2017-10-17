package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.DefaultNamed;
import games.strategy.engine.data.GameData;

/**
 * Superclass for serializable proxies whose principal is of type {@link DefaultNamed}.
 *
 * @param <T> The type of the principal to be proxied.
 */
@Immutable
public abstract class AbstractDefaultNamedProxy<T extends DefaultNamed> extends AbstractProxy<T> {
  protected AbstractDefaultNamedProxy(final Class<T> principalType) {
    super(principalType);
  }

  /**
   * The header for proxies whose principal is of type {@link DefaultNamed}.
   */
  protected static class DefaultNamedHeader {
    public String name;

    // TODO: consider adding a Builder subtype so we can create immutable instances of all header types
  }

  /**
   * Superclass for serializable proxy readers whose principal is of type {@link DefaultNamed}.
   *
   * @param <HeaderT> The type of the proxy header.
   */
  protected abstract class AbstractReader<HeaderT extends DefaultNamedHeader> extends AbstractProxy<T>.AbstractReader {
    private final Supplier<HeaderT> newHeader;

    /**
     * This constructor uses proxy headers of type {@link DefaultNamedHeader}. If a subclass specifies a different proxy
     * header type, a {@link ClassCastException} will be thrown at runtime.
     */
    @SuppressWarnings("unchecked")
    protected AbstractReader() {
      this(() -> (HeaderT) new DefaultNamedHeader());
    }

    protected AbstractReader(final Supplier<HeaderT> newHeader) {
      checkNotNull(newHeader);

      this.newHeader = newHeader;
    }

    @Override
    public final void readHeader(final Context context) throws IOException, ClassNotFoundException {
      checkNotNull(context);

      final HeaderT header = newHeader.get();
      readHeader(context, header);
      final T principal = createPrincipal(context.getGameData(), header);
      context.registerPrincipal(principal);
    }

    /**
     * Reads the proxy header.
     *
     * <p>
     * Subclasses may override and must call the superclass implementation.
     * </p>
     *
     * @param context The proxy reader context.
     * @param header The proxy header.
     *
     * @throws IOException If an I/O error occurs while reading the proxy header.
     * @throws ClassNotFoundException If a class could not be found while reading the proxy header.
     */
    protected void readHeader(final Context context, final HeaderT header) throws IOException, ClassNotFoundException {
      header.name = context.getInputStream().readUTF();
    }

    /**
     * Creates a new principal from the specified proxy header.
     *
     * @param gameData The game data to which the principal will be added.
     * @param header The proxy header containing information about the principal.
     *
     * @return The new principal.
     */
    protected abstract T createPrincipal(GameData gameData, HeaderT header);

    @Override
    public final T readBody(final Context context) throws IOException, ClassNotFoundException {
      checkNotNull(context);

      final String name = context.getInputStream().readUTF();

      final T principal = context.getPrincipal(name, getPrincipalType());
      readBody(context, principal);
      return principal;
    }

    /**
     * Reads the proxy body.
     *
     * <p>
     * Subclasses may override and must call the superclass implementation.
     * </p>
     *
     * @param context The proxy reader context.
     * @param principal The principal to initialize from the proxy body.
     *
     * @throws IOException If an I/O error occurs while reading the proxy body.
     * @throws ClassNotFoundException If a class could not be found while readying the proxy body.
     */
    protected abstract void readBody(Context context, T principal) throws IOException, ClassNotFoundException;
  }

  /**
   * Superclass for serializable proxy writers whose principal is of type {@link DefaultNamed}.
   */
  protected abstract class AbstractWriter extends AbstractProxy<T>.AbstractWriter {
    protected AbstractWriter(final T principal) {
      super(principal);
    }

    /**
     * Subclasses may override and must call the superclass implementation.
     */
    @Override
    public void writeHeader(final Context context) throws IOException {
      checkNotNull(context);

      context.getOutputStream().writeUTF(getPrincipal().getName());
    }

    /**
     * Subclasses may override and must call the superclass implementation.
     */
    @Override
    public void writeBody(final Context context) throws IOException {
      checkNotNull(context);

      context.getOutputStream().writeUTF(getPrincipal().getName());
    }
  }
}
