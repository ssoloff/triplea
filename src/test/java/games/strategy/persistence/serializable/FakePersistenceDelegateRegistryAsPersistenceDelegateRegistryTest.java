package games.strategy.persistence.serializable;

/**
 * A fixture for testing the {@link FakePersistenceDelegateRegistry} class to ensure it does not violate the contract of
 * the {@link PersistenceDelegateRegistry} interface.
 */
public final class FakePersistenceDelegateRegistryAsPersistenceDelegateRegistryTest
    extends AbstractPersistenceDelegateRegistryTestCase {
  @Override
  protected PersistenceDelegateRegistry createPersistenceDelegateRegistry() {
    return new FakePersistenceDelegateRegistry();
  }
}
