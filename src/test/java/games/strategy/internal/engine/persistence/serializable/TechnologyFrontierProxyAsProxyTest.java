package games.strategy.internal.engine.persistence.serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.ImmutableList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.TechnologyFrontier;
import games.strategy.engine.persistence.serializable.AbstractHeaderlessProxyTestCase;
import games.strategy.triplea.delegate.FakeTechAdvance;

public final class TechnologyFrontierProxyAsProxyTest extends AbstractHeaderlessProxyTestCase<TechnologyFrontier> {
  public TechnologyFrontierProxyAsProxyTest() {
    super(TechnologyFrontier.class, new TechnologyFrontierProxy());
  }

  @Override
  protected Collection<Object> getPrincipalDependencies(final GameData gameData) {
    return ImmutableList.<Object>builder()
        .addAll(super.getPrincipalDependencies(gameData))
        .add(new FakeTechAdvance(gameData, "techAdvance1Name"))
        .add(new FakeTechAdvance(gameData, "techAdvance2Name"))
        .build();
  }

  @Override
  protected TechnologyFrontier createPrincipal(final GameData gameData) {
    final TechnologyFrontier principal = new TechnologyFrontier("technologyFrontierName", gameData);
    principal.addAdvance(Arrays.asList(
        new FakeTechAdvance(gameData, "techAdvance1Name"),
        new FakeTechAdvance(gameData, "techAdvance2Name")));
    return principal;
  }

  @Override
  protected void assertPrincipalEquals(final TechnologyFrontier expected, final TechnologyFrontier actual) {
    assertThat(actual.getName(), is(expected.getName()));
    assertThat(actual.getTechs(), is(expected.getTechs()));
  }
}
