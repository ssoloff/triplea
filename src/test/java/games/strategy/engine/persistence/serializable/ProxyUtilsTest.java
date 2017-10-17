package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.FakeAttachment;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.Named;
import games.strategy.engine.data.Resource;
import games.strategy.io.IoUtils;
import games.strategy.util.IntegerMap;

public final class ProxyUtilsTest {
  private static final Class<Resource> PRINCIPAL_TYPE = Resource.class;

  private final ProxyRegistry proxyRegistry = ProxyRegistry.newInstance(FakeAttachmentProxy.FACTORY);
  private final GameData sinkGameData = new GameData();
  private final GameData sourceGameData = new GameData();

  private byte[] writeToMemory(final ProxyWriteOperation operation) throws IOException {
    return IoUtils.writeToMemory(os -> {
      try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
        operation.execute(new DefaultProxyWriterContext(proxyRegistry, oos));
      }
    });
  }

  @FunctionalInterface
  private interface ProxyWriteOperation {
    void execute(Proxy.Writer.Context context) throws IOException;
  }

  private <T> T readFromMemory(final byte[] bytes, final ProxyReadOperation<T> operation) throws IOException {
    return IoUtils.readFromMemory(bytes, is -> {
      try (ObjectInputStream ois = new ObjectInputStream(is)) {
        return operation.execute(new DefaultProxyReaderContext(proxyRegistry, ois, sinkGameData));
      }
    });
  }

  @FunctionalInterface
  private interface ProxyReadOperation<T> {
    T execute(Proxy.Reader.Context context) throws IOException;
  }

  private Named newSourcePrincipal(final String name) {
    return new Resource(name, sourceGameData);
  }

  private Named newSinkPrincipal(final String name) throws IOException {
    final Named principal = new Resource(name, sinkGameData);
    new PrincipalRegistry(sinkGameData).registerPrincipal(principal);
    return principal;
  }

  @Test
  public void shouldBeAbleToRoundTripNullNamedReference() throws Exception {
    final byte[] bytes = writeToMemory(context -> {
      ProxyUtils.writeNamedReference(context, null);
    });
    final @Nullable Named principal = readFromMemory(bytes, context -> {
      return ProxyUtils.readNamedReference(context, PRINCIPAL_TYPE);
    });

    assertThat(principal, is(nullValue()));
  }

  @Test
  public void shouldBeAbleToRoundTripNonNullNamedReference() throws Exception {
    final byte[] bytes = writeToMemory(context -> {
      ProxyUtils.writeNamedReference(context, newSourcePrincipal("name"));
    });
    final Named sinkPrincipal = newSinkPrincipal("name");
    final @Nullable Named principal = readFromMemory(bytes, context -> {
      return ProxyUtils.readNamedReference(context, PRINCIPAL_TYPE);
    });

    assertThat(principal, is(sameInstance(sinkPrincipal)));
  }

  @Test
  public void shouldBeAbleToRoundTripNamedReferenceCollection() throws Exception {
    final byte[] bytes = writeToMemory(context -> {
      ProxyUtils.writeNamedReferenceCollection(context, Arrays.asList(
          newSourcePrincipal("name1"),
          newSourcePrincipal("name2")));
    });
    final Named sinkPrincipal1 = newSinkPrincipal("name1");
    final Named sinkPrincipal2 = newSinkPrincipal("name2");
    final Collection<? extends Named> principals = readFromMemory(bytes, context -> {
      return ProxyUtils.readNamedReferenceCollection(context, PRINCIPAL_TYPE);
    });

    assertThat(principals, contains(Arrays.asList(sameInstance(sinkPrincipal1), sameInstance(sinkPrincipal2))));
  }

  @Test
  public void shouldBeAbleToRoundTripNamedReferenceIntegerMap() throws Exception {
    final byte[] bytes = writeToMemory(context -> {
      final IntegerMap<Named> valuesBySourcePrincipal = new IntegerMap<>();
      valuesBySourcePrincipal.add(newSourcePrincipal("name1"), 11);
      valuesBySourcePrincipal.add(newSourcePrincipal("name2"), 22);
      ProxyUtils.writeNamedReferenceIntegerMap(context, valuesBySourcePrincipal);
    });
    final Named sinkPrincipal1 = newSinkPrincipal("name1");
    final Named sinkPrincipal2 = newSinkPrincipal("name2");
    final IntegerMap<? extends Named> valuesBySinkPrincipal = readFromMemory(bytes, context -> {
      return ProxyUtils.readNamedReferenceIntegerMap(context, PRINCIPAL_TYPE);
    });

    assertThat(valuesBySinkPrincipal.keySet(), containsInAnyOrder(Arrays.asList(
        sameInstance(sinkPrincipal1),
        sameInstance(sinkPrincipal2))));
    assertThat(valuesBySinkPrincipal.getInt(sinkPrincipal1), is(11));
    assertThat(valuesBySinkPrincipal.getInt(sinkPrincipal2), is(22));
  }

  @Test
  public void shouldBeAbleToRoundTripProxyBodyCollection() throws Exception {
    final Collection<?> expectedPrincipals = Arrays.asList(new FakeAttachment("name1"), new FakeAttachment("name2"));

    final byte[] bytes = writeToMemory(context -> {
      ProxyUtils.writeProxyBodyCollection(context, expectedPrincipals);
    });
    final Collection<?> actualPrincipals = readFromMemory(bytes, context -> {
      try {
        return ProxyUtils.readProxyBodyCollection(context, FakeAttachment.class);
      } catch (final ClassNotFoundException e) {
        throw new IOException(e);
      }
    });

    assertThat(actualPrincipals, is(expectedPrincipals));
  }
}
