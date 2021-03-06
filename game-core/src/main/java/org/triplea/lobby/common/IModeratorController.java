package org.triplea.lobby.common;

import games.strategy.engine.lobby.PlayerName;
import games.strategy.engine.message.IRemote;
import games.strategy.engine.message.RemoteName;
import games.strategy.net.INode;
import java.time.Instant;
import javax.annotation.Nullable;

/**
 * A service that provides lobby management operations whose use is restricted to lobby moderators.
 */
public interface IModeratorController extends IRemote {
  RemoteName REMOTE_NAME =
      new RemoteName(
          "games.strategy.engine.lobby.server.ModeratorController:Global",
          IModeratorController.class);

  // TODO: The methods accepting a Date parameter can be converted to Instant after the next
  // lobby-incompatible release.

  /**
   * Boot the given INode from the network.
   *
   * <p>This method can only be called by admin users.
   */
  void boot(PlayerName playerName);

  /**
   * Ban the mac of the given INode.
   *
   * @param banExpires {@code null} for a permanent ban.
   */
  void banUser(PlayerName node, @Nullable Instant banExpires);

  /** Get list of people in the game. */
  String getHostConnections(INode node);

  /** Remote get chat log of a headless host bot. */
  String getChatLogHeadlessHostBot(INode node, String hashedPassword, String salt);

  /** Remote boot player in a headless host bot. */
  String bootPlayerHeadlessHostBot(
      INode node, String playerNameToBeBooted, String hashedPassword, String salt);

  /** Remote ban player in a headless host bot. */
  String banPlayerHeadlessHostBot(
      INode node, String playerNameToBeBanned, String hashedPassword, String salt);

  /** Remote stop game of a headless host bot. */
  String stopGameHeadlessHostBot(INode node, String hashedPassword, String salt);

  /** Remote shutdown of a headless host bot. */
  String shutDownHeadlessHostBot(INode node, String hashedPassword, String salt);

  /** For use with a password for the bot. */
  String getHeadlessHostBotSalt(INode node);

  /**
   * Reset the password of the given user. Returns null if the password was updated without error.
   *
   * <p>You cannot change the password of an anonymous node, and you cannot change the password for
   * an admin user.
   *
   * @param node The node whose password is to be set.
   * @param hashedPassword The new hashed password.
   * @deprecated Remove usages of this. Does not make sense for moderators to be able to reset
   *     passwords of logged in users.
   */
  @Deprecated
  default @Nullable String setPassword(final INode node, final String hashedPassword) {
    return "";
  }

  String getInformationOn(INode node);

  /** Is the current user an admin. */
  boolean isAdmin();

  /** Is this node an admin. */
  boolean isPlayerAdmin(INode node);
}
