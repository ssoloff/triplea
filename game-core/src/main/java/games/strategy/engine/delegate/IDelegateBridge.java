package games.strategy.engine.delegate;

import games.strategy.engine.data.Change;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerId;
import games.strategy.engine.display.IDisplay;
import games.strategy.engine.history.IDelegateHistoryWriter;
import games.strategy.engine.player.Player;
import games.strategy.engine.random.IRandomStats.DiceType;
import games.strategy.sound.ISound;
import java.util.Properties;

/**
 * A class that communicates with the Delegate. DelegateBridge coordinates communication between the
 * Delegate and both the players and the game data. The reason for communicating through a
 * DelegateBridge is to achieve network transparency. The delegateBridge allows the Delegate to talk
 * to the player in a safe manner.
 */
public interface IDelegateBridge {
  /**
   * Equivalent to getRemotePlayer(getPlayerId()).
   *
   * @return remote for the current player.
   */
  Player getRemotePlayer();

  /** Get a remote reference to the given player. */
  Player getRemotePlayer(PlayerId id);

  PlayerId getPlayerId();

  /** Returns the current step name. */
  String getStepName();

  /**
   * Add a change to game data. Use this rather than changing gameData directly since this method
   * allows us to send the changes to other machines.
   */
  void addChange(Change change);

  /** equivalent to getRandom(max,1,annotation)[0]. */
  int getRandom(int max, PlayerId player, DiceType diceType, String annotation);

  /**
   * Return a random value to be used by the delegate.
   *
   * <p>Delegates should not use random data that comes from any other source.
   *
   * <p>
   *
   * @param annotation a string used to describe the random event.
   */
  int[] getRandom(int max, int count, PlayerId player, DiceType diceType, String annotation);

  /**
   * return the delegate history writer for this game.
   *
   * <p>The delegate history writer allows writing to the game history.
   */
  IDelegateHistoryWriter getHistoryWriter();

  /**
   * Return an object that implements the IDisplay interface for the game.
   *
   * <p>Methods called on this returned object will be invoked on all displays in the game,
   * including those on remote machines
   */
  IDisplay getDisplayChannelBroadcaster();

  /**
   * Return an object that implements the ISound interface for the game.
   *
   * <p>Methods called on this returned object will be invoked on all sound channels in the game,
   * including those on remote machines
   */
  ISound getSoundChannelBroadcaster();

  /** Returns the properties for this step. */
  Properties getStepProperties();

  /**
   * After this step finishes executing, the next delegate will not be called.
   *
   * <p>This method allows the delegate to signal that the game is over, but does not force the ui
   * or the display to shutdown.
   */
  void stopGameSequence();

  void leaveDelegateExecution();

  void enterDelegateExecution();

  GameData getData();
}
