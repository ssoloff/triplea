package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.GameData;
import games.strategy.io.IoUtils;

public final class DefaultProxyReaderContextTest {
  @Test
  public void readProxyBody_ShouldThrowExceptionWhenPrincipalTypeIsNull() throws Exception {
    final byte[] bytes = IoUtils.writeToMemory(os -> {
      try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
        oos.writeObject(null);
      }
    });
    final DefaultProxyReaderContext context = givenContextForMemoryStream(bytes);

    final Exception e = assertThrows(IOException.class, () -> context.readProxyBody());

    assertThat(e.getMessage(), containsString("malformed"));
  }

  private static DefaultProxyReaderContext givenContextForMemoryStream(final byte[] bytes) throws Exception {
    return new DefaultProxyReaderContext(
        ProxyRegistry.newInstance(),
        new ObjectInputStream(new ByteArrayInputStream(bytes)),
        new GameData());
  }
}
