package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;

import javax.annotation.Nullable;

import games.strategy.engine.data.GameData;

final class DefaultProxyReaderContext implements Proxy.Reader.Context {
  private final GameData gameData;
  private final ObjectInputStream ois;
  private final PrincipalRegistry principalRegistry;
  private final ProxyRegistry proxyRegistry;

  DefaultProxyReaderContext(final ProxyRegistry proxyRegistry, final ObjectInputStream ois, final GameData gameData) {
    this.gameData = gameData;
    this.ois = ois;
    this.principalRegistry = new PrincipalRegistry(gameData);
    this.proxyRegistry = proxyRegistry;
  }

  @Override
  public GameData getGameData() {
    return gameData;
  }

  @Override
  public ObjectInputStream getInputStream() {
    return ois;
  }

  @Override
  public <T> T getPrincipal(final String name, final Collection<Class<? extends T>> types) throws IOException {
    checkNotNull(name);
    checkNotNull(types);

    return principalRegistry.getPrincipal(name, types);
  }

  @Override
  public Object readProxyBody() throws IOException, ClassNotFoundException {
    final Class<?> principalType = readPrincipalType();
    return newProxyReaderFor(principalType).readBody(this);
  }

  private Class<?> readPrincipalType() throws IOException, ClassNotFoundException {
    final @Nullable Class<?> principalType = (Class<?>) ois.readObject();
    if (principalType == null) {
      throw new IOException("malformed input stream: expected principal type but was null");
    }
    return principalType;
  }

  private Proxy.Reader newProxyReaderFor(final Class<?> principalType) {
    return proxyRegistry.getProxyFor(principalType).newReader();
  }

  @Override
  public void registerPrincipal(final Object principal) throws IOException {
    checkNotNull(principal);

    principalRegistry.registerPrincipal(principal);
  }
}
