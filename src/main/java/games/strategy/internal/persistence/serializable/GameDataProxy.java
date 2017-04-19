package games.strategy.internal.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import games.strategy.engine.data.GameData;
import games.strategy.persistence.serializable.VersionedProxySupport;
import games.strategy.util.Version;

/**
 * A serializable proxy for the {@link GameData} class.
 *
 * <p>
 * Instances of this class are not thread safe.
 * </p>
 */
public final class GameDataProxy implements Externalizable {
  private static final long serialVersionUID = -7520643498826565152L;

  private static final long CURRENT_VERSION = 1L;

  private final VersionedProxySupport versionedProxySupport = new VersionedProxySupport(this);

  /**
   * @serial The game name; may be {@code null}.
   */
  private String name;

  /**
   * @serial The game version; may be {@code null}.
   */
  private Version version;

  /**
   * Initializes a new instance of the {@code GameDataProxy} class during deserialization.
   */
  public GameDataProxy() {}

  /**
   * Initializes a new instance of the {@code GameDataProxy} class from the specified {@code GameData} instance.
   *
   * @param gameData The {@code GameData} instance; must not be {@code null}.
   */
  public GameDataProxy(final GameData gameData) {
    checkNotNull(gameData);

    name = gameData.getGameName();
    version = gameData.getGameVersion();
  }

  private Object readResolve() {
    final GameData gameData = new GameData();
    gameData.setGameName(name);
    gameData.setGameVersion(version);
    return gameData;
  }

  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    versionedProxySupport.read(in);
  }

  @SuppressWarnings("unused")
  private void readExternalV1(final ObjectInput in) throws IOException, ClassNotFoundException {
    name = (String) in.readObject();
    version = (Version) in.readObject();
  }

  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    versionedProxySupport.write(out, CURRENT_VERSION);
  }

  @SuppressWarnings("unused")
  private void writeExternalV1(final ObjectOutput out) throws IOException {
    out.writeObject(name);
    out.writeObject(version);
  }
}
