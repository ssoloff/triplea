package games.strategy.internal.persistence.serializable;

import java.util.HashMap;
import java.util.Map;

import games.strategy.persistence.serializable.AbstractPersistenceDelegateTestCase;
import games.strategy.persistence.serializable.PersistenceDelegateRegistry;
import games.strategy.util.memento.PropertyBagMemento;

public final class PropertyBagMementoPersistenceDelegateAsPersistenceDelegateTest
    extends AbstractPersistenceDelegateTestCase<PropertyBagMemento> {
  public PropertyBagMementoPersistenceDelegateAsPersistenceDelegateTest() {
    super(PropertyBagMemento.class);
  }

  @Override
  protected PropertyBagMemento createSubject() {
    final Map<String, Object> propertiesByName = new HashMap<>();
    propertiesByName.put("property1", 42L);
    propertiesByName.put("property2", "2112");
    return new PropertyBagMemento("id", 8L, propertiesByName);
  }

  @Override
  protected void registerPersistenceDelegates(final PersistenceDelegateRegistry persistenceDelegateRegistry) {
    persistenceDelegateRegistry.registerPersistenceDelegate(
        PropertyBagMemento.class, new PropertyBagMementoPersistenceDelegate());
  }
}
