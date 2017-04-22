package games.strategy.persistence.memento.serializable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import games.strategy.internal.persistence.serializable.PropertyBagMementoPersistenceDelegate;
import games.strategy.persistence.serializable.DefaultPersistenceDelegateRegistry;
import games.strategy.persistence.serializable.PersistenceDelegateRegistry;
import games.strategy.util.memento.Memento;
import games.strategy.util.memento.PropertyBagMemento;

@RunWith(MockitoJUnitRunner.class)
public final class SerializableMementoExporterTest {
  private final Memento memento = newMemento();

  private final SerializableMementoExporter mementoExporter = newMementoExporter();

  @Mock
  private OutputStream os;

  private static Memento newMemento() {
    return new PropertyBagMemento("schema-id", 1L, Collections.emptyMap());
  }

  private static SerializableMementoExporter newMementoExporter() {
    final PersistenceDelegateRegistry persistenceDelegateRegistry = new DefaultPersistenceDelegateRegistry();
    persistenceDelegateRegistry.registerPersistenceDelegate(
        PropertyBagMemento.class, new PropertyBagMementoPersistenceDelegate());
    return new SerializableMementoExporter(persistenceDelegateRegistry);
  }

  @Test
  public void exportMemento_ShouldNotCloseOutputStream() throws Exception {
    mementoExporter.exportMemento(memento, os);

    verify(os, never()).close();
  }
}
