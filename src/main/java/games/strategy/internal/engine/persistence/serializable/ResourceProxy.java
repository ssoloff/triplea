package games.strategy.internal.engine.persistence.serializable;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.Resource;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxy;
import games.strategy.engine.persistence.serializable.ProxyFactory;

/**
 * A serializable proxy for the {@link Resource} class.
 */
@Immutable
public final class ResourceProxy extends AbstractNamedAttachableProxy<Resource> {
  // TODO: get rid of FACTORY fields that aren't needed (they are typically only required by headerless proxies)
  public static final ProxyFactory FACTORY = ProxyFactory.newInstance(Resource.class, ResourceProxy::new);

  public ResourceProxy() {
    super(Resource.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  private final class Reader extends AbstractReader<NamedAttachableHeader> {
    @Override
    protected Resource createPrincipal(final GameData gameData, final NamedAttachableHeader header) {
      return new Resource(header.name, gameData);
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final Resource principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final Resource principal) {
      super(principal);
    }
  }
}
