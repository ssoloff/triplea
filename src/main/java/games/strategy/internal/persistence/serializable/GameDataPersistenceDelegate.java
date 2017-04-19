package games.strategy.internal.persistence.serializable;

import java.io.IOException;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.AbstractPersistenceDelegate;

/**
 * A persistence delegate for the {@link GameData} class.
 *
 * <p>
 * Instances of this class are immutable.
 * </p>
 */
public final class GameDataPersistenceDelegate extends AbstractPersistenceDelegate {
  @Override
  public Object replaceObject(final Object obj) throws IOException {
    return (obj instanceof GameData)
        ? new GameDataProxy((GameData) obj)
        : super.replaceObject(obj);
  }
}
