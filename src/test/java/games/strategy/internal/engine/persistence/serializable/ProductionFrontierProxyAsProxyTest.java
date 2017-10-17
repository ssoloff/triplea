package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxyTestCase;

public final class ProductionFrontierProxyAsProxyTest extends AbstractDefaultNamedProxyTestCase<ProductionFrontier> {
  public ProductionFrontierProxyAsProxyTest() {
    super(ProductionFrontier.class, new ProductionFrontierProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new ProductionRule("productionRule1Name", gameData))
        .add(new ProductionRule("productionRule2Name", gameData))
        .build();
  }

  @Override
  protected ProductionFrontier createPrincipalHeader(final GameData gameData) {
    return new ProductionFrontier(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected ProductionFrontier createPrincipalBody(final GameData gameData) {
    final ProductionFrontier principal = new ProductionFrontier(PRINCIPAL_NAME, gameData);
    principal.addRule(new ProductionRule("productionRule1Name", gameData));
    principal.addRule(new ProductionRule("productionRule2Name", gameData));
    return principal;
  }

  @Override
  protected void assertPrincipalBodyEquals(final ProductionFrontier expected, final ProductionFrontier actual) {
    assertThat(actual.getRules(), is(expected.getRules()));
  }
}
