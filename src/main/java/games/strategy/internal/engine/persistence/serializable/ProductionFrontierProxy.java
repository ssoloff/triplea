package games.strategy.internal.engine.persistence.serializable;

import java.io.IOException;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxy;
import games.strategy.engine.persistence.serializable.ProxyFactory;
import games.strategy.engine.persistence.serializable.ProxyUtils;

/**
 * A serializable proxy for the {@link ProductionFrontier} class.
 */
@Immutable
public final class ProductionFrontierProxy extends AbstractDefaultNamedProxy<ProductionFrontier> {
  public static final ProxyFactory FACTORY =
      ProxyFactory.newInstance(ProductionFrontier.class, ProductionFrontierProxy::new);

  public ProductionFrontierProxy() {
    super(ProductionFrontier.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader<DefaultNamedHeader> {
    @Override
    protected ProductionFrontier createPrincipal(final GameData gameData, final DefaultNamedHeader header) {
      return new ProductionFrontier(header.name, gameData);
    }

    @Override
    protected void readBody(final Context context, final ProductionFrontier principal) throws IOException {
      readProductionRules(context, principal);
    }

    private void readProductionRules(final Context context, final ProductionFrontier principal) throws IOException {
      ProxyUtils.readNamedReferenceCollection(context, ProductionRule.class).forEach(principal::addRule);
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final ProductionFrontier principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final ProductionFrontier principal) {
      super(principal);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      super.writeBody(context);

      writeProductionRules(context);
    }

    private void writeProductionRules(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceCollection(context, getPrincipal().getRules());
    }
  }
}
