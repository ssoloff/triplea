package games.strategy.persistence.serializable;

/**
 * A fixture for testing the {@link FakeNonSerializableClassPersistenceDelegate} class to ensure it does not violate the
 * contract of the {@link PersistenceDelegate} interface.
 */
public final class FakeNonSerializableClassPersistenceDelegateAsPersistenceDelegateTest
    extends AbstractPersistenceDelegateTestCase {
  @Override
  protected Object createSubject() {
    return new FakeNonSerializableClass(2112, "42");
  }

  @Override
  protected void registerPersistenceDelegates(final PersistenceDelegateRegistry persistenceDelegateRegistry) {
    persistenceDelegateRegistry.registerPersistenceDelegate(
        FakeNonSerializableClass.class, new FakeNonSerializableClassPersistenceDelegate());
  }
}
