package games.strategy.engine.framework.persistence.serializable;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.OutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import games.strategy.engine.data.TestGameDataMementoFactory;

@RunWith(MockitoJUnitRunner.class)
public final class SerializableGameDataMementoExporterTest {
  private final SerializableGameDataMementoExporter exporter = new SerializableGameDataMementoExporter();

  private final Object gameDataMemento = TestGameDataMementoFactory.newValidGameDataMemento();

  @Mock
  private OutputStream os;

  @Test
  public void exportGameDataMemento_ShouldThrowExceptionWhenGameDataMementoIsNull() {
    catchException(() -> exporter.exportGameDataMemento(null, os));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void exportGameDataMemento_ShouldThrowExceptionWhenOutputStreamIsNull() {
    catchException(() -> exporter.exportGameDataMemento(gameDataMemento, null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void exportGameDataMemento_ShouldNotCloseOutputStream() throws Exception {
    exporter.exportGameDataMemento(gameDataMemento, os);

    verify(os, never()).close();
  }
}
