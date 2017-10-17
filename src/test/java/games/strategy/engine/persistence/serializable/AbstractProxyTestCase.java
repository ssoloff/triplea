package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.GameData;
import games.strategy.io.IoUtils;

/**
 * A fixture for testing the basic aspects of proxy classes.
 *
 * @param <T> The type of the principal to be proxied.
 */
public abstract class AbstractProxyTestCase<T> {
  private final Class<T> principalType;
  private final Proxy proxy;
  private final ProxyRegistry proxyRegistry = ProxyRegistry.newInstance(getProxyFactories());

  protected AbstractProxyTestCase(final Class<T> principalType, final Proxy proxy) {
    checkNotNull(principalType);
    checkNotNull(proxy);

    this.principalType = principalType;
    this.proxy = proxy;
  }

  /**
   * Gets the collection of dependencies that must be registered with the game data in order to deserialize the
   * principal.
   *
   * <p>
   * This implementation returns an empty collection. Subclasses may override and must include the collection
   * returned by the superclass implementation.
   * </p>
   *
   * @param gameData The game data that owns the principal dependencies.
   *
   * @return The collection of dependencies that must be registered with the game data in order to deserialize the
   *         principal.
   */
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    return Collections.emptyList();
  }

  /**
   * Gets the type of the principal to be proxied.
   *
   * @return The type of the principal to be proxied.
   */
  protected final Class<T> getPrincipalType() {
    return principalType;
  }

  /**
   * Gets the collection of proxy factories required for the principal to be persisted.
   *
   * <p>
   * This implementation returns an empty collection. Subclasses may override and must include the collection
   * returned by the superclass implementation.
   * </p>
   *
   * @return The collection of proxy factories required for the principal to be persisted.
   */
  protected Collection<ProxyFactory> getProxyFactories() {
    return Collections.emptyList();
  }

  /**
   * Writes the proxy header for the specified principal.
   *
   * @param principal The principal.
   *
   * @return The binary representation of the proxy header for the specified principal.
   *
   * @throws IOException If an I/O error occurs while writing the proxy header.
   */
  protected final byte[] writePrincipalHeader(final T principal) throws IOException {
    return IoUtils.writeToMemory(os -> {
      try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
        proxy.newWriterFor(principal).writeHeader(newProxyWriterContext(oos));
      }
    });
  }

  /**
   * Writes the proxy body for the specified principal.
   *
   * @param principal The principal.
   *
   * @return The binary representation of the proxy body for the specified principal.
   *
   * @throws IOException If an I/O error occurs while writing the proxy body.
   */
  protected final byte[] writePrincipalBody(final T principal) throws IOException {
    return IoUtils.writeToMemory(os -> {
      try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
        proxy.newWriterFor(principal).writeBody(newProxyWriterContext(oos));
      }
    });
  }

  private Proxy.Writer.Context newProxyWriterContext(final ObjectOutputStream oos) {
    return new DefaultProxyWriterContext(proxyRegistry, oos);
  }

  /**
   * Reads the proxy header for the principal contained in the specified binary representation.
   *
   * @param bytes The binary representation of the proxy header.
   * @param gameData The game data into which the principal should be placed.
   *
   * @throws IOException If an error occurs while reading the proxy header.
   */
  protected final void readPrincipalHeader(final byte[] bytes, final GameData gameData) throws IOException {
    IoUtils.consumeFromMemory(bytes, is -> {
      try (ObjectInputStream ois = new ObjectInputStream(is)) {
        proxy.newReader().readHeader(newProxyReaderContext(ois, gameData));
      } catch (final ClassNotFoundException e) {
        throw new IOException(e);
      }
    });
  }

  /**
   * Reads the proxy body for the principal contained in the specified binary representation.
   *
   * @param bytes The binary representation of the proxy body.
   * @param gameData The game data into which the principal should be placed.
   *
   * @return The principal.
   *
   * @throws IOException If an error occurs while reading the proxy body.
   */
  protected final T readPrincipalBody(final byte[] bytes, final GameData gameData) throws IOException {
    return principalType.cast(IoUtils.readFromMemory(bytes, is -> {
      try (ObjectInputStream ois = new ObjectInputStream(is)) {
        return proxy.newReader().readBody(newProxyReaderContext(ois, gameData));
      } catch (final ClassNotFoundException e) {
        throw new IOException(e);
      }
    }));
  }

  private Proxy.Reader.Context newProxyReaderContext(final ObjectInputStream ois, final GameData gameData) {
    return new DefaultProxyReaderContext(proxyRegistry, ois, gameData);
  }

  @Test
  public final void newWriterFor_ShouldThrowExceptionWhenPrincipalHasWrongType() {
    assertThrows(ClassCastException.class, () -> proxy.newWriterFor(new Object()));
  }
}
