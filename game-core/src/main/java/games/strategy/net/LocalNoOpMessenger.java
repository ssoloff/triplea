package games.strategy.net;

import games.strategy.engine.lobby.PlayerName;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.triplea.http.client.ApiKey;

/** Implementation of {@link IServerMessenger} for a local game server. */
public class LocalNoOpMessenger implements IServerMessenger {

  private final INode node;

  public LocalNoOpMessenger() {
    try {
      node = new Node("dummy", Node.getLocalHost(), 0);
    } catch (final UnknownHostException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void send(final Serializable msg, final INode to) {}

  @Override
  public void addMessageListener(final IMessageListener listener) {}

  @Override
  public INode getLocalNode() {
    return node;
  }

  @Override
  public boolean isConnected() {
    return false;
  }

  @Override
  public void shutDown() {}

  @Override
  public boolean isServer() {
    return true;
  }

  @Override
  public INode getServerNode() {
    return node;
  }

  @Override
  public void setAcceptNewConnections(final boolean accept) {}

  @Override
  public void setLoginValidator(final ILoginValidator loginValidator) {}

  @Override
  public ILoginValidator getLoginValidator() {
    return null;
  }

  @Override
  public void addConnectionChangeListener(final IConnectionChangeListener listener) {}

  @Override
  public void removeConnectionChangeListener(final IConnectionChangeListener listener) {}

  @Override
  public void removeConnection(final INode node) {}

  @Override
  public Set<INode> getNodes() {
    return null;
  }

  @Override
  public void banPlayer(final String ip, final String mac) {}

  @Override
  public @Nullable String getPlayerMac(final PlayerName name) {
    return null;
  }

  @Override
  public boolean isPlayerBanned(final String ip, final String mac) {
    return false;
  }

  @Override
  public void setApiKeyGenerator(final Function<PlayerName, ApiKey> apiKeyGenerator) {}
}
