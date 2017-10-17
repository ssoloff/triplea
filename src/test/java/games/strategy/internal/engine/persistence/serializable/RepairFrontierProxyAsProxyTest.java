package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxyTestCase;

public final class RepairFrontierProxyAsProxyTest extends AbstractDefaultNamedProxyTestCase<RepairFrontier> {
  public RepairFrontierProxyAsProxyTest() {
    super(RepairFrontier.class, new RepairFrontierProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new RepairRule("repairRule1Name", gameData))
        .add(new RepairRule("repairRule2Name", gameData))
        .build();
  }

  @Override
  protected RepairFrontier createPrincipalHeader(final GameData gameData) {
    return new RepairFrontier(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected RepairFrontier createPrincipalBody(final GameData gameData) {
    final RepairFrontier principal = new RepairFrontier(PRINCIPAL_NAME, gameData);
    principal.addRule(new RepairRule("repairRule1Name", gameData));
    principal.addRule(new RepairRule("repairRule2Name", gameData));
    return principal;
  }

  @Override
  protected void assertPrincipalBodyEquals(final RepairFrontier expected, final RepairFrontier actual) {
    assertThat(actual.getRules(), is(expected.getRules()));
  }
}
