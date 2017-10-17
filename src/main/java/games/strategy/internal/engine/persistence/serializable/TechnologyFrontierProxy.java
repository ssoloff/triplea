package games.strategy.internal.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.TechnologyFrontier;
import games.strategy.engine.persistence.serializable.AbstractHeaderlessProxy;
import games.strategy.engine.persistence.serializable.ProxyFactory;
import games.strategy.engine.persistence.serializable.ProxyUtils;
import games.strategy.triplea.delegate.TechAdvance;

/**
 * A serializable proxy for the {@link TechnologyFrontier} class.
 */
@Immutable
public final class TechnologyFrontierProxy extends AbstractHeaderlessProxy<TechnologyFrontier> {
  public static final ProxyFactory FACTORY =
      ProxyFactory.newInstance(TechnologyFrontier.class, TechnologyFrontierProxy::new);

  public TechnologyFrontierProxy() {
    super(TechnologyFrontier.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader {
    @Override
    public Object readBody(final Context context) throws IOException {
      checkNotNull(context);

      final String name = context.getInputStream().readUTF();

      final TechnologyFrontier principal = new TechnologyFrontier(name, context.getGameData());

      readTechAdvances(context, principal);

      return principal;
    }

    private void readTechAdvances(final Context context, final TechnologyFrontier principal) throws IOException {
      principal.addAdvance(ProxyUtils.readNamedReferenceCollection(context, TechAdvance.class));
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final TechnologyFrontier principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final TechnologyFrontier principal) {
      super(principal);
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      checkNotNull(context);

      context.getOutputStream().writeUTF(getPrincipal().getName());

      writeTechAdvances(context);
    }

    private void writeTechAdvances(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceCollection(context, getPrincipal().getTechs());
    }
  }
}
