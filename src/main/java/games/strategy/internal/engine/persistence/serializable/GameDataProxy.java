package games.strategy.internal.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.persistence.serializable.AbstractProxy;

/**
 * A serializable proxy for the {@link GameData} class.
 */
@Immutable
public final class GameDataProxy extends AbstractProxy<GameData> {
  public GameDataProxy() {
    super(GameData.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader {
    @Override
    public void readHeader(final Context context) throws IOException {
      checkNotNull(context);

      // TODO Auto-generated method stub
    }

    @Override
    public Object readBody(final Context context) throws IOException {
      checkNotNull(context);

      // TODO Auto-generated method stub
      return context.getGameData(); // XXX: does this make sense?
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final GameData principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final GameData gameData) {
      super(gameData);
    }

    @Override
    public void writeHeader(final Context context) throws IOException {
      checkNotNull(context);

      final GameData principal = getPrincipal();

      // TODO: write metadata
      final ObjectOutputStream oos = context.getOutputStream();
      oos.writeUTF(principal.getGameName());

      // TODO: writePlayerHeaders (use ProxyUtils)
      for (final PlayerID playerId : principal.getPlayerList()) {
        context.writeProxyHeader(playerId);
      }
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      checkNotNull(context);

      final GameData principal = getPrincipal();

      // TODO: writePlayerBodies (use ProxyUtils)
      for (final PlayerID playerId : principal.getPlayerList()) {
        context.writeProxyBody(playerId);
      }
    }
  }
}
