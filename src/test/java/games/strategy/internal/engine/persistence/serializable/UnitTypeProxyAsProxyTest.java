package games.strategy.internal.engine.persistence.serializable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.UnitType;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxyTestCase;

public final class UnitTypeProxyAsProxyTest extends AbstractNamedAttachableProxyTestCase<UnitType> {
  public UnitTypeProxyAsProxyTest() {
    super(UnitType.class, new UnitTypeProxy());
  }

  @Override
  protected UnitType createPrincipalHeader(final GameData gameData) {
    return new UnitType(PRINCIPAL_NAME, gameData);
  }

  @Override
  protected UnitType createPrincipalBody(final GameData gameData) {
    final UnitType principal = new UnitType(PRINCIPAL_NAME, gameData);
    addAttachments(principal);
    return principal;
  }
}
