package games.strategy.internal.engine.persistence.serializable;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.UnitType;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxy;
import games.strategy.engine.persistence.serializable.ProxyFactory;
import games.strategy.engine.persistence.serializable.ProxyUtils;

/**
 * A serializable proxy for the {@link ProductionRule} class.
 */
@Immutable
public final class ProductionRuleProxy extends AbstractDefaultNamedProxy<ProductionRule> {
  public static final ProxyFactory FACTORY = ProxyFactory.newInstance(ProductionRule.class, ProductionRuleProxy::new);

  public ProductionRuleProxy() {
    super(ProductionRule.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader<DefaultNamedHeader> {
    @Override
    protected ProductionRule createPrincipal(final GameData gameData, final DefaultNamedHeader header) {
      return new ProductionRule(header.name, gameData);
    }

    @Override
    protected void readBody(final Context context, final ProductionRule principal) throws IOException {
      readCosts(context, principal);
      readResults(context, principal);
    }

    private void readCosts(final Context context, final ProductionRule principal) throws IOException {
      ProxyUtils.readNamedReferenceIntegerMap(context, Resource.class).entrySet()
          .forEach(it -> principal.addCost(it.getKey(), it.getValue()));
    }

    private void readResults(final Context context, final ProductionRule principal) throws IOException {
      ProxyUtils.readNamedReferenceIntegerMap(context, Arrays.asList(Resource.class, UnitType.class)).entrySet()
          .forEach(it -> principal.addResult(it.getKey(), it.getValue()));
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final ProductionRule principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final ProductionRule principal) {
      super(principal);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      super.writeBody(context);

      writeCosts(context);
      writeResults(context);
    }

    private void writeCosts(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceIntegerMap(context, getPrincipal().getCosts());
    }

    private void writeResults(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceIntegerMap(context, getPrincipal().getResults());
    }
  }
}
