package games.strategy.persistence.memento.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

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
    final Map<String, Object> propertiesByName = new HashMap<>();
    propertiesByName.put("property1", 42);
    propertiesByName.put("property2", "2112");
    return new PropertyBagMemento("id", 1L, propertiesByName);
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
