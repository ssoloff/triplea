package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxyTestCase;

public final class ProductionRuleProxyAsProxyTest extends AbstractDefaultNamedProxyTestCase<ProductionRule> {
  public ProductionRuleProxyAsProxyTest() {
    super(ProductionRule.class, new ProductionRuleProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    // TODO: ugh, this is ugly!!!!
    // maybe we can add some kind of manager type to the test base class
    // this type will primarily act as a factory for GameDataComponents, similar to TestGameDataComponent factory
    // secondarily, since it knows which instances it has created, it can re-create them in another GameData instance
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new Resource("resource1Name", gameData))
        .add(new Resource("resource2Name", gameData))
        .add(new Resource("resource3Name", gameData))
        .add(new Resource("resource4Name", gameData))
        .build();
  }

  @Override
  protected ProductionRule createPrincipalHeader(final GameData gameData) {
    return new ProductionRule(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected ProductionRule createPrincipalBody(final GameData gameData) {
    final ProductionRule principal = new ProductionRule(PRINCIPAL_NAME, gameData);

    principal.addCost(new Resource("resource1Name", gameData), 11);
    principal.addCost(new Resource("resource2Name", gameData), 22);

    principal.addResult(new Resource("resource3Name", gameData), 33);
    principal.addResult(new Resource("resource4Name", gameData), 44);

    return principal;
  }

  @Override
  protected void assertPrincipalBodyEquals(final ProductionRule expected, final ProductionRule actual) {
    assertThat(actual.getCosts(), is(expected.getCosts()));
    assertThat(actual.getResults(), is(expected.getResults()));
  }
}
