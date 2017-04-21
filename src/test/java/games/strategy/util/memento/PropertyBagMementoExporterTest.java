package games.strategy.util.memento;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import games.strategy.util.memento.PropertyBagMementoExporter.HandlerSupplier;

public final class PropertyBagMementoExporterTest {
  private static final long DEFAULT_VERSION = 42L;

  private static final String ID = "id";

  private final FakeOriginator originator = new FakeOriginator(42, "2112");

  @Test
  public void exportMemento_ShouldReturnMementoWithDefaultVersion() throws Exception {
    final PropertyBagMementoExporter<FakeOriginator> mementoExporter = newMementoExporterForAllVersions();

    final PropertyBagMemento memento = mementoExporter.exportMemento(originator);

    assertThat(memento.getVersion(), is(DEFAULT_VERSION));
  }

  private static PropertyBagMementoExporter<FakeOriginator> newMementoExporterForAllVersions() {
    return newMementoExporter(version -> Optional.of((originator, propertiesByName) -> {
      propertiesByName.put("field1", originator.field1);
      propertiesByName.put("field2", originator.field2);
    }));
  }

  private static PropertyBagMementoExporter<FakeOriginator> newMementoExporter(
      final HandlerSupplier<FakeOriginator> handlerSupplier) {
    return new PropertyBagMementoExporter<>(ID, DEFAULT_VERSION, handlerSupplier);
  }

  @Test
  public void exportMementoWithVersion_ShouldReturnMementoWithSpecifiedIdAndVersion() throws Exception {
    final long version = 2112L;
    final PropertyBagMementoExporter<FakeOriginator> mementoExporter = newMementoExporterForAllVersions();

    final PropertyBagMemento memento = mementoExporter.exportMemento(originator, version);

    assertThat(memento.getId(), is(ID));
    assertThat(memento.getVersion(), is(version));
  }

  @Test
  public void exportMementoWithVersion_ShouldReturnMementoWithOriginatorProperties() throws Exception {
    final PropertyBagMemento expected = newMemento();
    final PropertyBagMementoExporter<FakeOriginator> mementoExporter = newMementoExporterForAllVersions();

    final PropertyBagMemento memento = mementoExporter.exportMemento(originator, 1L);

    assertThat(memento.getPropertiesByName(), is(expected.getPropertiesByName()));
  }

  private PropertyBagMemento newMemento() {
    final Map<String, Object> propertiesByName = new HashMap<>();
    propertiesByName.put("field1", originator.field1);
    propertiesByName.put("field2", originator.field2);
    return new PropertyBagMemento(ID, DEFAULT_VERSION, propertiesByName);
  }

  @Test
  public void exportMementoWithVersion_ShouldThrowExceptionIfVersionIsUnsupported() {
    final long version = 2112L;
    final PropertyBagMementoExporter<FakeOriginator> mementoExporter = newMementoExporterForNoVersions();

    catchException(() -> mementoExporter.exportMemento(originator, version));

    assertThat(caughtException(), allOf(
        is(instanceOf(MementoExportException.class)),
        hasMessageThat(containsString(String.format("version %d is unsupported", version)))));
  }

  private static PropertyBagMementoExporter<FakeOriginator> newMementoExporterForNoVersions() {
    return newMementoExporter(version -> Optional.empty());
  }
}
