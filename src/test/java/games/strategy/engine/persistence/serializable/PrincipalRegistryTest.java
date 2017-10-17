package games.strategy.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.Named;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.UnitType;
import games.strategy.triplea.delegate.AARadarAdvance;
import games.strategy.triplea.delegate.DestroyerBombardTechAdvance;
import games.strategy.triplea.delegate.FakeTechAdvance;
import games.strategy.triplea.delegate.HeavyBomberAdvance;
import games.strategy.triplea.delegate.ImprovedArtillerySupportAdvance;
import games.strategy.triplea.delegate.ImprovedShipyardsAdvance;
import games.strategy.triplea.delegate.IncreasedFactoryProductionAdvance;
import games.strategy.triplea.delegate.IndustrialTechnologyAdvance;
import games.strategy.triplea.delegate.JetPowerAdvance;
import games.strategy.triplea.delegate.LongRangeAircraftAdvance;
import games.strategy.triplea.delegate.MechanizedInfantryAdvance;
import games.strategy.triplea.delegate.ParatroopersAdvance;
import games.strategy.triplea.delegate.RocketsAdvance;
import games.strategy.triplea.delegate.SuperSubsAdvance;
import games.strategy.triplea.delegate.TechAdvance;
import games.strategy.triplea.delegate.WarBondsAdvance;

public final class PrincipalRegistryTest {
  private static final String PRINCIPAL_NAME = "name";

  private final GameData gameData = new GameData();

  private final PrincipalRegistry principalRegistry = new PrincipalRegistry(gameData);

  @Test
  public void getPrincipal_ShouldThrowExceptionWhenNameIsUnknown() {
    final Class<?> type1 = PlayerID.class;
    final Class<?> type2 = Resource.class;

    final Exception e = assertThrows(
        IOException.class,
        () -> principalRegistry.getPrincipal(PRINCIPAL_NAME, Arrays.asList(type1, type2)));

    assertThat(e.getMessage(), allOf(
        containsString("unknown principal"),
        containsString(PRINCIPAL_NAME),
        containsString(type1.getName()),
        containsString(type2.getName())));
  }

  @Test
  public void getPrincipal_ShouldThrowExceptionWhenTypeIsUnknown() {
    final Class<?> type1 = PlayerID.class;
    final Class<?> type2 = Object.class;

    final Exception e = assertThrows(
        IOException.class,
        () -> principalRegistry.getPrincipal(PRINCIPAL_NAME, Arrays.asList(type1, type2)));

    assertThat(e.getMessage(), allOf(
        containsString("unknown principal type"),
        containsString(type2.getName())));
  }

  @Test
  public void getPrincipal_ShouldReturnPrincipalWhenPrincipalIsPresentAndMultipleTypesSpecified() throws Exception {
    final Resource expectedPrincipal = new Resource(PRINCIPAL_NAME, gameData);
    gameData.getResourceList().addResource(expectedPrincipal);

    final Object actualPrincipal =
        principalRegistry.getPrincipal(PRINCIPAL_NAME, Arrays.asList(PlayerID.class, Resource.class));

    assertThat(actualPrincipal, is(sameInstance(expectedPrincipal)));
  }

  @Test
  public void getPrincipal_ShouldReturnPrincipalWhenPrincipalIsPresentAndAncestorClassIsRegistered() throws Exception {
    final TechAdvance expectedPrincipal = new FakeTechAdvance(gameData, PRINCIPAL_NAME);
    gameData.getTechnologyFrontier().addAdvance(expectedPrincipal);

    final Object actualPrincipal = principalRegistry.getPrincipal(PRINCIPAL_NAME, Arrays.asList(FakeTechAdvance.class));

    assertThat(actualPrincipal, is(sameInstance(expectedPrincipal)));
  }

  @Test
  public void registerPrincipal_ShouldThrowExceptionWhenTypeIsUnknown() {
    final Object principal = new Object();

    final Exception e = assertThrows(IOException.class, () -> principalRegistry.registerPrincipal(principal));

    assertThat(e.getMessage(), allOf(
        containsString("unknown principal type"),
        containsString(principal.getClass().getName())));
  }

  @Test
  public void shouldBeAbleToRegisterAndGetPrincipals() throws Exception {
    final Collection<Named> expectedPrincipals = Arrays.asList(
        new AARadarAdvance(gameData),
        new DestroyerBombardTechAdvance(gameData),
        new HeavyBomberAdvance(gameData),
        new ImprovedArtillerySupportAdvance(gameData),
        new ImprovedShipyardsAdvance(gameData),
        new IncreasedFactoryProductionAdvance(gameData),
        new IndustrialTechnologyAdvance(gameData),
        new JetPowerAdvance(gameData),
        new LongRangeAircraftAdvance(gameData),
        new MechanizedInfantryAdvance(gameData),
        new ParatroopersAdvance(gameData),
        new PlayerID("playerIdName", gameData),
        new ProductionFrontier("productionFrontierName", gameData),
        new ProductionRule("productionRuleName", gameData),
        new RepairFrontier("repairFrontierName", gameData),
        new RepairRule("repairRuleName", gameData),
        new Resource("resourceName", gameData),
        new RocketsAdvance(gameData),
        new SuperSubsAdvance(gameData),
        new UnitType("unitTypeName", gameData),
        new WarBondsAdvance(gameData));
    for (final Named expectedPrincipal : expectedPrincipals) {
      principalRegistry.registerPrincipal(expectedPrincipal);

      final Object actualPrincipal = principalRegistry.getPrincipal(
          expectedPrincipal.getName(),
          Arrays.asList(expectedPrincipal.getClass()));

      assertThat(actualPrincipal, is(sameInstance(expectedPrincipal)));
    }
  }
}
