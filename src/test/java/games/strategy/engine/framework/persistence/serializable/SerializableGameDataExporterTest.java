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

import games.strategy.engine.data.GameData;

@RunWith(MockitoJUnitRunner.class)
public final class SerializableGameDataExporterTest {
  private final SerializableGameDataExporter exporter = new SerializableGameDataExporter();

  private final GameData gameData = new GameData();

  @Mock
  private OutputStream os;

  @Test
  public void exportGameData_ShouldThrowExceptionWhenGameDataIsNull() {
    catchException(() -> exporter.exportGameData(null, os));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void exportGameData_ShouldThrowExceptionWhenOutputStreamIsNull() {
    catchException(() -> exporter.exportGameData(gameData, null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void exportGameData_ShouldNotCloseOutputStream() throws Exception {
    exporter.exportGameData(gameData, os);

    verify(os, never()).close();
  }
}
