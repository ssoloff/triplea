package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.Resource;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxyTestCase;

public final class ResourceProxyAsProxyTest extends AbstractNamedAttachableProxyTestCase<Resource> {
  public ResourceProxyAsProxyTest() {
    super(Resource.class, new ResourceProxy());
  }

  @Override
  protected Resource createPrincipalHeader(final GameData gameData) {
    return new Resource(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected Resource createPrincipalBody(final GameData gameData) {
    final Resource principal = new Resource(PRINCIPAL_NAME, gameData);
    addAttachments(principal);
    return principal;
  }
}
