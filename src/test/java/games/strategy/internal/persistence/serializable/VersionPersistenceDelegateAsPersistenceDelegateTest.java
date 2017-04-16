package games.strategy.internal.persistence.serializable;

import games.strategy.persistence.serializable.AbstractPersistenceDelegateTestCase;
import games.strategy.persistence.serializable.PersistenceDelegate;
import games.strategy.persistence.serializable.PersistenceDelegateRegistry;
import games.strategy.util.Version;

/**
 * A fixture for testing the {@link VersionPersistenceDelegate} class to ensure it does not violate the
 * contract of the {@link PersistenceDelegate} interface.
 */
public final class VersionPersistenceDelegateAsPersistenceDelegateTest extends AbstractPersistenceDelegateTestCase {
  @Override
  protected Object createSubject() {
    return new Version(1, 2, 3, 4);
  }

  @Override
  protected void registerPersistenceDelegates(final PersistenceDelegateRegistry persistenceDelegateRegistry) {
    persistenceDelegateRegistry.registerPersistenceDelegate(Version.class, new VersionPersistenceDelegate());
  }
}
