package games.strategy.engine.data.changefactory;

import games.strategy.engine.data.Change;
import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerId;
import games.strategy.engine.data.ProductionFrontier;

/** Change a players production frontier. */
class ProductionFrontierChange extends Change {
  private static final long serialVersionUID = 3336145814067456701L;

  private final String startFrontierName;
  private final String endFrontierName;
  private final String playerName;

  ProductionFrontierChange(final ProductionFrontier newFrontier, final PlayerId player) {
    startFrontierName = player.getProductionFrontier().getName();
    endFrontierName = newFrontier.getName();
    playerName = player.getName();
  }

  ProductionFrontierChange(
      final String startFrontierName, final String endFrontierName, final String playerName) {
    this.startFrontierName = startFrontierName;
    this.endFrontierName = endFrontierName;
    this.playerName = playerName;
  }

  @Override
  protected void perform(final GameData data) {
    final PlayerId player = data.getPlayerList().getPlayerId(playerName);
    final ProductionFrontier frontier =
        data.getProductionFrontierList().getProductionFrontier(endFrontierName);
    player.setProductionFrontier(frontier);
  }

  @Override
  public Change invert() {
    return new ProductionFrontierChange(endFrontierName, startFrontierName, playerName);
  }
}
