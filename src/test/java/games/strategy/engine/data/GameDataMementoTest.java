package games.strategy.engine.data;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionHamcrestMatchers.hasMessageThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

public final class GameDataMementoTest {
  @Test
  public void shouldBeAbleToRoundTripValidMemento() throws Exception {
    final GameData expected = TestGameDataFactory.newValidGameData();

    final Object memento = GameDataMemento.fromGameData(expected);
    final GameData actual = GameDataMemento.toGameData(memento);

    assertGameDataEquals(expected, actual);
  }

  private static void assertGameDataEquals(final GameData expected, final GameData actual) {
    assert expected != null;
    assert actual != null;

    assertThat(actual.getGameName(), is(expected.getGameName()));
    assertThat(actual.getGameVersion(), is(expected.getGameVersion()));
  }

  @Test
  public void fromGameData_ShouldThrowExceptionWhenGameDataIsNull() {
    catchException(() -> GameDataMemento.fromGameData(null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void fromGameDataWithOptions_ShouldThrowExceptionWhenGameDataIsNull() {
    catchException(() -> GameDataMemento.fromGameData(null, GameDataMemento.DEFAULT_OPTIONS));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void fromGameDataWithOptions_ShouldThrowExceptionWhenOptionsIsNull() {
    catchException(() -> GameDataMemento.fromGameData(TestGameDataFactory.newValidGameData(), null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenMementoIsNull() {
    catchException(() -> GameDataMemento.toGameData(null));

    assertThat(caughtException(), is(instanceOf(NullPointerException.class)));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenMementoHasWrongType() {
    catchException(() -> GameDataMemento.toGameData(new Object()));

    assertThat(caughtException(), allOf(
        is(instanceOf(GameDataMementoException.class)),
        hasMessageThat(containsString("wrong type"))));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenMementoMetadataMimeTypeIsIllegal() {
    final Map<String, Object> memento = TestGameDataMementoFactory.newValidGameDataMemento();
    memento.put(GameDataMemento.AttributeNames.META_MIME_TYPE, "other");

    catchException(() -> GameDataMemento.toGameData(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(GameDataMementoException.class)),
        hasMessageThat(containsString("illegal MIME type"))));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenMementoMetadataVersionIsIncompatible() {
    final Map<String, Object> memento = TestGameDataMementoFactory.newValidGameDataMemento();
    memento.put(GameDataMemento.AttributeNames.META_VERSION, -1L);

    catchException(() -> GameDataMemento.toGameData(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(GameDataMementoException.class)),
        hasMessageThat(containsString("incompatible version"))));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenRequiredAttributeIsMissing() {
    final Map<String, Object> memento = TestGameDataMementoFactory.newValidGameDataMemento();
    memento.remove(GameDataMemento.AttributeNames.VERSION);

    catchException(() -> GameDataMemento.toGameData(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(GameDataMementoException.class)),
        hasMessageThat(containsString(String.format(
            "missing required attribute '%s'",
            GameDataMemento.AttributeNames.VERSION)))));
  }

  @Test
  public void toGameData_ShouldThrowExceptionWhenAttributeValueHasWrongType() {
    final Map<String, Object> memento = TestGameDataMementoFactory.newValidGameDataMemento();
    memento.put(GameDataMemento.AttributeNames.VERSION, "1.2.3.4");

    catchException(() -> GameDataMemento.toGameData(memento));

    assertThat(caughtException(), allOf(
        is(instanceOf(GameDataMementoException.class)),
        hasMessageThat(containsString(String.format(
            "attribute '%s' has wrong type",
            GameDataMemento.AttributeNames.VERSION)))));
  }
}
