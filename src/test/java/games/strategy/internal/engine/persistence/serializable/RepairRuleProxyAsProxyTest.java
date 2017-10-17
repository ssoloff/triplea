package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxyTestCase;

public final class RepairRuleProxyAsProxyTest extends AbstractDefaultNamedProxyTestCase<RepairRule> {
  public RepairRuleProxyAsProxyTest() {
    super(RepairRule.class, new RepairRuleProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    // TODO: fix DRYness; see ProductionRuleProxyAsProxyTest
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new Resource("resource1Name", gameData))
        .add(new Resource("resource2Name", gameData))
        .add(new Resource("resource3Name", gameData))
        .add(new Resource("resource4Name", gameData))
        .build();
  }

  @Override
  protected RepairRule createPrincipalHeader(final GameData gameData) {
    return new RepairRule(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected RepairRule createPrincipalBody(final GameData gameData) {
    final RepairRule principal = new RepairRule(PRINCIPAL_NAME, gameData);

    principal.addCost(new Resource("resource1Name", gameData), 11);
    principal.addCost(new Resource("resource2Name", gameData), 22);

    principal.addResult(new Resource("resource3Name", gameData), 33);
    principal.addResult(new Resource("resource4Name", gameData), 44);

    return principal;
  }

  @Override
  protected void assertPrincipalBodyEquals(final RepairRule expected, final RepairRule actual) {
    assertThat(actual.getCosts(), is(expected.getCosts()));
    assertThat(actual.getResults(), is(expected.getResults()));
  }
}
