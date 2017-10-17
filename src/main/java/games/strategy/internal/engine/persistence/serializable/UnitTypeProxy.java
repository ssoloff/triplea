package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.UnitType;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxy;

/**
 * A serializable proxy for the {@link UnitType} class.
 */
@Immutable
public final class UnitTypeProxy extends AbstractNamedAttachableProxy<UnitType> {
  public UnitTypeProxy() {
    super(UnitType.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader<NamedAttachableHeader> {
    @Override
    protected UnitType createPrincipal(final GameData gameData, final NamedAttachableHeader header) {
      return new UnitType(header.name, gameData);
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final UnitType principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final UnitType principal) {
      super(principal);
    }
  }
}
