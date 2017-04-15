package games.strategy.persistence.serializable;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A fixture for testing the basic aspects of the {@link ObjectOutputStream} class.
 */
@RunWith(MockitoJUnitRunner.class)
public final class ObjectOutputStreamTest {
  private ObjectOutputStream oos;

  @Mock
  private PersistenceDelegate persistenceDelegate;

  private final PersistenceDelegateRegistry persistenceDelegateRegistry = new FakePersistenceDelegateRegistry();

  @Before
  public void setUp() throws Exception {
    oos = new ObjectOutputStream(newEmptyOutputStream(), persistenceDelegateRegistry);
  }

  private static OutputStream newEmptyOutputStream() {
    return new ByteArrayOutputStream();
  }

  @After
  public void tearDown() throws Exception {
    oos.close();
  }

  @Test
  public void constructor_ShouldThrowExceptionWhenOutputStreamIsNull() {
    catchException(() -> new ObjectOutputStream(null, persistenceDelegateRegistry));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void constructor_ShouldThrowExceptionWhenPersistenceDelegateRegistryIsNull() {
    catchException(() -> new ObjectOutputStream(newEmptyOutputStream(), null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void annotateClass_ShouldDelegateToPersistenceDelegateWhenPersistenceDelegateAvailable() throws Exception {
    final Class<?> cl = Integer.class;
    persistenceDelegateRegistry.registerPersistenceDelegate(cl, persistenceDelegate);

    oos.annotateClass(cl);

    verify(persistenceDelegate).annotateClass(oos, cl);
  }

  @Test
  public void annotateClass_ShouldNotThrowExceptionWhenPersistenceDelegateUnavailable() throws Exception {
    oos.annotateClass(Integer.class);
  }

  @Test
  public void annotateClass_ShouldNotThrowExceptionWhenClassIsNull() throws Exception {
    oos.annotateClass(null);
  }

  @Test
  public void replaceObject_ShouldDelegateToPersistenceDelegateWhenPersistenceDelegateAvailable() throws Exception {
    final Object obj = Integer.valueOf(42);
    persistenceDelegateRegistry.registerPersistenceDelegate(Integer.class, persistenceDelegate);

    oos.replaceObject(obj);

    verify(persistenceDelegate).replaceObject(obj);
  }

  @Test
  public void replaceObject_ShouldNotThrowExceptionWhenPersistenceDelegateUnavailable() throws Exception {
    oos.replaceObject(Integer.valueOf(42));
  }

  @Test
  public void replaceObject_ShouldReturnNullWhenObjectIsNull() throws Exception {
    assertThat(oos.replaceObject(null), is(nullValue()));
  }
}
