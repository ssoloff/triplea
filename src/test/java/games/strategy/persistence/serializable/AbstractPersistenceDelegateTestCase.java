package games.strategy.persistence.serializable;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * A fixture for testing the basic aspects of classes that implement the {@link PersistenceDelegate} interface.
 */
public abstract class AbstractPersistenceDelegateTestCase {
  private final PersistenceDelegateRegistry persistenceDelegateRegistry = new FakePersistenceDelegateRegistry();

  /**
   * Initializes a new instance of the {@code AbstractPersistenceDelegateTestCase} class.
   */
  protected AbstractPersistenceDelegateTestCase() {}

  /**
   * Asserts that the specified subjects are equal.
   *
   * <p>
   * This implementation compares the two objects using the {@code equals} method.
   * </p>
   *
   * @param expected The expected subject; must not be {@code null}.
   * @param actual The actual subject; may be {@code null}.
   *
   * @throws AssertionError If the two subjects are not equal.
   */
  protected void assertSubjectEquals(final Object expected, final Object actual) {
    assertThat(actual, is(expected));
  }

  /**
   * Creates the subject to be persisted.
   *
   * @return The subject to be persisted; never {@code null}.
   */
  protected abstract Object createSubject();

  /**
   * Registers the persistence delegates required for the subject to be persisted.
   *
   * @param persistenceDelegateRegistry The persistence delegate registry for use in the fixture; must not be
   *        {@code null}.
   */
  protected abstract void registerPersistenceDelegates(PersistenceDelegateRegistry persistenceDelegateRegistry);

  private Object readObject(final ByteArrayOutputStream baos) throws Exception {
    try (final InputStream is = new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(is, persistenceDelegateRegistry)) {
      return ois.readObject();
    }
  }

  private void writeObject(final ByteArrayOutputStream baos, final Object obj) throws Exception {
    try (final ObjectOutputStream oos = new ObjectOutputStream(baos, persistenceDelegateRegistry)) {
      oos.writeObject(obj);
    }
  }

  /**
   * Sets up the test fixture.
   *
   * <p>
   * Subclasses may override and must call the superclass implementation.
   * </p>
   *
   * @throws Exception If an error occurs.
   */
  @Before
  public void setUp() throws Exception {
    registerPersistenceDelegates(persistenceDelegateRegistry);
  }

  @Test
  public void persistenceDelegate_ShouldBeAbleToRoundTripSubject() throws Exception {
    final Object obj = createSubject();
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    writeObject(baos, obj);
    final Object deserializedObj = readObject(baos);

    assertSubjectEquals(obj, deserializedObj);
  }
}
