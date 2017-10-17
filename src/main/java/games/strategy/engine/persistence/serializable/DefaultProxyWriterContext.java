package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;

final class DefaultProxyWriterContext implements Proxy.Writer.Context {
  private final ObjectOutputStream oos;
  private final ProxyRegistry proxyRegistry;

  DefaultProxyWriterContext(final ProxyRegistry proxyRegistry, final ObjectOutputStream oos) {
    this.oos = oos;
    this.proxyRegistry = proxyRegistry;
  }

  @Override
  public ObjectOutputStream getOutputStream() {
    return oos;
  }

  @Override
  public void writeProxyHeader(final Object principal) throws IOException {
    checkNotNull(principal);

    writePrincipalType(principal);
    newProxyWriterFor(principal).writeHeader(this);
  }

  @Override
  public void writeProxyBody(final Object principal) throws IOException {
    checkNotNull(principal);

    writePrincipalType(principal);
    newProxyWriterFor(principal).writeBody(this);
  }

  private void writePrincipalType(final Object principal) throws IOException {
    oos.writeObject(principal.getClass());
  }

  private Proxy.Writer newProxyWriterFor(final Object principal) {
    return proxyRegistry.getProxyFor(principal.getClass()).newWriterFor(principal);
  }
}
