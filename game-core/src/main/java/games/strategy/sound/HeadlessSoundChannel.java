package games.strategy.sound;

import games.strategy.engine.data.PlayerId;
import java.util.Collection;

/** Implementation of {@link ISound} that does nothing (i.e. no sounds will be played). */
public class HeadlessSoundChannel implements ISound {

  @Override
  public void playSoundForAll(final String clipName, final PlayerId playerId) {}

  @Override
  public void playSoundToPlayers(
      final String clipName,
      final Collection<PlayerId> playersToSendTo,
      final Collection<PlayerId> butNotThesePlayers,
      final boolean includeObservers) {}
}
