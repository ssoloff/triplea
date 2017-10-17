package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.TechnologyFrontier;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxyTestCase;
import games.strategy.engine.persistence.serializable.ProxyFactory;
import games.strategy.triplea.delegate.FakeTechAdvance;
import games.strategy.triplea.delegate.TechAdvance;
import games.strategy.util.Tuple;

public final class PlayerIdProxyAsProxyTest extends AbstractNamedAttachableProxyTestCase<PlayerID> {
  private static final String PRODUCTION_FRONTIER_NAME = "productionFrontier1";
  private static final String REPAIR_FRONTIER_NAME = "repairFrontier1";
  private static final String RESOURCE_1_NAME = "resource1Name";
  private static final String RESOURCE_2_NAME = "resource2Name";
  private static final String TECH_ADVANCE_1_NAME = "techAdvance1Name";
  private static final String TECH_ADVANCE_2_NAME = "techAdvance2Name";

  public PlayerIdProxyAsProxyTest() {
    super(PlayerID.class, new PlayerIdProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new ProductionFrontier(PRODUCTION_FRONTIER_NAME, gameData))
        .add(new RepairFrontier(REPAIR_FRONTIER_NAME, gameData))
        .add(new Resource(RESOURCE_1_NAME, gameData))
        .add(new Resource(RESOURCE_2_NAME, gameData))
        .add(new FakeTechAdvance(gameData, TECH_ADVANCE_1_NAME))
        .add(new FakeTechAdvance(gameData, TECH_ADVANCE_2_NAME))
        .build();
  }

  @Override
  protected Collection<ProxyFactory> getProxyFactories() {
    return ImmutableList.<ProxyFactory>builder()
        .addAll(super.getProxyFactories())
        .add(TechnologyFrontierProxy.FACTORY)
        .build();
  }

  @Override
  protected PlayerID createPrincipalHeader(final GameData gameData) {
    final PlayerID principal = new PlayerID(PRINCIPAL_NAME, true, true, "defaultType", true, gameData);
    principal.setIsDisabled(true);
    principal.setWhoAmI("null:anyone");
    return principal;
  }

  @Override
  protected void assertPrincipalHeaderEquals(final PlayerID expected, final PlayerID actual) {
    super.assertPrincipalHeaderEquals(expected, actual);

    assertThat(actual.getDefaultType(), is(expected.getDefaultType()));
    assertThat(actual.getCanBeDisabled(), is(expected.getCanBeDisabled()));
    assertThat(actual.getIsDisabled(), is(expected.getIsDisabled()));
    assertThat(actual.isHidden(), is(expected.isHidden()));
    assertThat(actual.getOptional(), is(expected.getOptional()));
    assertThat(actual.getWhoAmI(), is(expected.getWhoAmI()));
  }

  @Override
  protected PlayerID createPrincipalBody(final GameData gameData) {
    final PlayerID principal = new PlayerID(PRINCIPAL_NAME, gameData);
    addAttachments(principal);
    principal.setProductionFrontier(new ProductionFrontier(PRODUCTION_FRONTIER_NAME, gameData));
    principal.setRepairFrontier(new RepairFrontier(REPAIR_FRONTIER_NAME, gameData));
    addResources(gameData, principal);
    addTechnologyFrontiers(gameData, principal);
    return principal;
  }

  private static void addResources(final GameData gameData, final PlayerID principal) {
    Arrays.asList(
        Tuple.of(new Resource(RESOURCE_1_NAME, gameData), 11),
        Tuple.of(new Resource(RESOURCE_2_NAME, gameData), 22))
        .forEach(it -> principal.getResources().addResource(it.getFirst(), it.getSecond()));
  }

  private static void addTechnologyFrontiers(final GameData gameData, final PlayerID principal) {
    final Collection<TechAdvance> techAdvances = Arrays.asList(
        new FakeTechAdvance(gameData, TECH_ADVANCE_1_NAME),
        new FakeTechAdvance(gameData, TECH_ADVANCE_2_NAME));
    final TechnologyFrontier technologyFrontier1 = new TechnologyFrontier("technologyFrontier1Name", gameData);
    technologyFrontier1.addAdvance(techAdvances);
    final TechnologyFrontier technologyFrontier2 = new TechnologyFrontier("technologyFrontier2Name", gameData);
    technologyFrontier2.addAdvance(techAdvances);
    principal.getTechnologyFrontierList()
        .addTechnologyFrontiers(Arrays.asList(technologyFrontier1, technologyFrontier2));
  }

  @Override
  protected void assertPrincipalBodyEquals(final PlayerID expected, final PlayerID actual) {
    super.assertPrincipalBodyEquals(expected, actual);

    assertThat(actual.getProductionFrontier().getName(), is(PRODUCTION_FRONTIER_NAME));
    assertThat(actual.getRepairFrontier().getName(), is(REPAIR_FRONTIER_NAME));
    assertThat(actual.getTechnologyFrontierList().getFrontiers(),
        is(expected.getTechnologyFrontierList().getFrontiers()));
    assertThat(actual.getResources().getResourcesCopy(), is(expected.getResources().getResourcesCopy()));
  }
}
