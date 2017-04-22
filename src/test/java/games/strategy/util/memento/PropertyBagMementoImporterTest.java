package games.strategy.util.memento;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import games.strategy.util.memento.PropertyBagMementoImporter.HandlerSupplier;

public final class PropertyBagMementoImporterTest {
  private static final String ID = "id";

  private static final long VERSION = 1L;

  private final FakeOriginator originator = new FakeOriginator(42, "2112");

  @Test
  public void importMemento_ShouldThrowExceptionIfMementoHasWrongType() {
    final Memento memento = mock(Memento.class);
    final PropertyBagMementoImporter<?> mementoImporter = newMementoImporterForAllVersions();

    catchException(() -> mementoImporter.importMemento(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(MementoImportException.class)),
        hasMessageThat(containsString("wrong type"))));
  }

  private static PropertyBagMementoImporter<FakeOriginator> newMementoImporterForAllVersions() {
    return newMementoImporter(version -> Optional.of(propertiesByName -> {
      return new FakeOriginator(
          (Integer) propertiesByName.get("field1"),
          (String) propertiesByName.get("field2"));
    }));
  }

  private static PropertyBagMementoImporter<FakeOriginator> newMementoImporter(
      final HandlerSupplier<FakeOriginator> handlerSupplier) {
    return new PropertyBagMementoImporter<>(ID, handlerSupplier);
  }

  @Test
  public void importMementoWithPropertyBagMemento_ShouldReturnOriginatorWithMementoProperties() throws Exception {
    final PropertyBagMemento memento = newMemento(ID, VERSION);
    final PropertyBagMementoImporter<FakeOriginator> mementoImporter = newMementoImporterForAllVersions();

    final FakeOriginator actual = mementoImporter.importMemento(memento);

    assertThat(actual, is(originator));
  }

  private PropertyBagMemento newMemento(final String id, final long version) {
    return new PropertyBagMemento(id, version, ImmutableMap.<String, Object>of(
        "field1", originator.field1,
        "field2", originator.field2));
  }

  @Test
  public void importMementoWithPropertyBagMemento_ShouldThrowExceptionIfIdIsUnsupported() {
    final String id = "other-id";
    final PropertyBagMemento memento = newMemento(id, VERSION);
    final PropertyBagMementoImporter<?> mementoImporter = newMementoImporterForAllVersions();

    catchException(() -> mementoImporter.importMemento(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(MementoImportException.class)),
        hasMessageThat(containsString(String.format("ID '%s' is unsupported", id)))));
  }

  @Test
  public void importMementoWithPropertyBagMemento_ShouldThrowExceptionIfVersionIsUnsupported() {
    final long version = 2112L;
    final PropertyBagMemento memento = newMemento(ID, version);
    final PropertyBagMementoImporter<?> mementoImporter = newMementoImporterForNoVersions();

    catchException(() -> mementoImporter.importMemento(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(MementoImportException.class)),
        hasMessageThat(containsString(String.format("version %d is unsupported", version)))));
  }

  private static PropertyBagMementoImporter<FakeOriginator> newMementoImporterForNoVersions() {
    return newMementoImporter(version -> Optional.empty());
  }
}
