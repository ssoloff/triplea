package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Function;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.triplea.delegate.TechAdvance;

/**
 * Superclass for serializable proxies whose principal is of type {@link TechAdvance}.
 *
 * @param <T> The type of the principal to be proxied.
 */
@Immutable
public abstract class AbstractTechAdvanceProxy<T extends TechAdvance> extends AbstractNamedAttachableProxy<T> {
  private final Function<GameData, T> newPrincipal;

  protected AbstractTechAdvanceProxy(final Class<T> principalType, final Function<GameData, T> newPrincipal) {
    super(principalType);

    checkNotNull(newPrincipal);

    this.newPrincipal = newPrincipal;
  }

  @Override
  protected final Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractNamedAttachableProxy<T>.AbstractReader<NamedAttachableHeader> {
    @Override
    protected T createPrincipal(final GameData gameData, final NamedAttachableHeader header) {
      return newPrincipal.apply(gameData);
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final T principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractNamedAttachableProxy<T>.AbstractWriter {
    Writer(final T principal) {
      super(principal);
    }
  }
}
