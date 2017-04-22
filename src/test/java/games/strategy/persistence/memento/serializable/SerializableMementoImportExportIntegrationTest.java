package games.strategy.persistence.memento.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import games.strategy.internal.persistence.serializable.PropertyBagMementoPersistenceDelegate;
import games.strategy.persistence.serializable.DefaultPersistenceDelegateRegistry;
import games.strategy.persistence.serializable.PersistenceDelegateRegistry;
import games.strategy.util.memento.Memento;
import games.strategy.util.memento.PropertyBagMemento;

/**
 * A fixture for testing the integration between the {@link SerializableMementoImporter} and
 * {@link SerializableMementoExporter} classes.
 */
public final class SerializableMementoImportExportIntegrationTest {
  private final PersistenceDelegateRegistry persistenceDelegateRegistry = newPersistenceDelegateRegistry();

  private static PersistenceDelegateRegistry newPersistenceDelegateRegistry() {
    final PersistenceDelegateRegistry persistenceDelegateRegistry = new DefaultPersistenceDelegateRegistry();
    persistenceDelegateRegistry.registerPersistenceDelegate(
        PropertyBagMemento.class, new PropertyBagMementoPersistenceDelegate());
    return persistenceDelegateRegistry;
  }

  @Test
  public void shouldBeAbleToRoundTripMemento() throws Exception {
    final Memento expected = newMemento();

    final byte[] bytes = exportMemento(expected);
    final Memento actual = importMemento(bytes);

    assertThat(actual, is(expected));
  }

  private static Memento newMemento() {
    return new PropertyBagMemento("schema-id", 1L, ImmutableMap.<String, Object>of(
        "property1", 42,
        "property2", "2112"));
  }

  private byte[] exportMemento(final Memento memento) throws Exception {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final SerializableMementoExporter mementoExporter = new SerializableMementoExporter(persistenceDelegateRegistry);
      mementoExporter.exportMemento(memento, baos);
      return baos.toByteArray();
    }
  }

  private Memento importMemento(final byte[] bytes) throws Exception {
    try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
      final SerializableMementoImporter mementoImporter = new SerializableMementoImporter(persistenceDelegateRegistry);
      return mementoImporter.importMemento(bais);
    }
  }
}
