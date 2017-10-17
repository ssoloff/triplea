package games.strategy.internal.engine.persistence.serializable;

import java.io.IOException;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.persistence.serializable.AbstractDefaultNamedProxy;
import games.strategy.engine.persistence.serializable.ProxyUtils;

/**
 * A serializable proxy for the {@link RepairFrontier} class.
 */
@Immutable
public final class RepairFrontierProxy extends AbstractDefaultNamedProxy<RepairFrontier> {
  public RepairFrontierProxy() {
    super(RepairFrontier.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader<DefaultNamedHeader> {
    @Override
    protected RepairFrontier createPrincipal(final GameData gameData, final DefaultNamedHeader header) {
      return new RepairFrontier(header.name, gameData);
    }

    @Override
    protected void readBody(final Context context, final RepairFrontier principal) throws IOException {
      readRepairRules(context, principal);
    }

    private void readRepairRules(final Context context, final RepairFrontier principal) throws IOException {
      ProxyUtils.readNamedReferenceCollection(context, RepairRule.class).forEach(principal::addRule);
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final RepairFrontier principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final RepairFrontier principal) {
      super(principal);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      super.writeBody(context);

      writeRepairRules(context);
    }

    private void writeRepairRules(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceCollection(context, getPrincipal().getRules());
    }
  }
}
