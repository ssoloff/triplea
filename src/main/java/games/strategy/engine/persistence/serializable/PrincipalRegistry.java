package games.strategy.engine.persistence.serializable;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.ProductionRule;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.RepairRule;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.UnitType;
import games.strategy.triplea.delegate.TechAdvance;

/**
 * A service for locating principals within a {@link GameData} instance.
 */
@Immutable
final class PrincipalRegistry {
  private static final Map<Class<?>, Registrar<?>> registrarsByType = ImmutableMap.<Class<?>, Registrar<?>>builder()
      .put(PlayerID.class, new Registrar<PlayerID>(
          (gameData, principal) -> gameData.getPlayerList().addPlayerId(principal),
          (gameData, name) -> gameData.getPlayerList().getPlayerId(name)))
      .put(ProductionFrontier.class, new Registrar<ProductionFrontier>(
          (gameData, principal) -> gameData.getProductionFrontierList().addProductionFrontier(principal),
          (gameData, name) -> gameData.getProductionFrontierList().getProductionFrontier(name)))
      .put(ProductionRule.class, new Registrar<ProductionRule>(
          (gameData, principal) -> gameData.getProductionRuleList().addProductionRule(principal),
          (gameData, name) -> gameData.getProductionRuleList().getProductionRule(name)))
      .put(RepairFrontier.class, new Registrar<RepairFrontier>(
          (gameData, principal) -> gameData.getRepairFrontierList().addRepairFrontier(principal),
          (gameData, name) -> gameData.getRepairFrontierList().getRepairFrontier(name)))
      .put(RepairRule.class, new Registrar<RepairRule>(
          (gameData, principal) -> gameData.getRepairRuleList().addRepairRule(principal),
          (gameData, name) -> gameData.getRepairRuleList().getRepairRule(name)))
      .put(Resource.class, new Registrar<Resource>(
          (gameData, principal) -> gameData.getResourceList().addResource(principal),
          (gameData, name) -> gameData.getResourceList().getResource(name)))
      .put(TechAdvance.class, new Registrar<TechAdvance>(
          (gameData, principal) -> gameData.getTechnologyFrontier().addAdvance(principal),
          (gameData, name) -> gameData.getTechnologyFrontier().getAdvanceByName(name)))
      .put(UnitType.class, new Registrar<UnitType>(
          (gameData, principal) -> gameData.getUnitTypeList().addUnitType(principal),
          (gameData, name) -> gameData.getUnitTypeList().getUnitType(name)))
      .build();

  private final GameData gameData;

  PrincipalRegistry(final GameData gameData) {
    this.gameData = gameData;
  }

  <T> T getPrincipal(final String name, final Collection<Class<? extends T>> types) throws IOException {
    for (final Class<? extends T> type : types) {
      final @Nullable T principal = getRegistrar(type).findPrincipal.apply(gameData, name);
      if (principal != null) {
        return principal;
      }
    }

    throw new IOException(String.format("unknown principal named '%s' of any type in %s",
        name, types.stream().map(Class::getName).collect(Collectors.toList())));
  }

  private static <T> Registrar<T> getRegistrar(final Class<T> type) throws IOException {
    @Nullable
    Class<?> targetType = type;
    while (targetType != null) {
      @SuppressWarnings("unchecked")
      final @Nullable Registrar<T> registrar = (Registrar<T>) registrarsByType.get(targetType);
      if (registrar != null) {
        return registrar;
      }
      targetType = targetType.getSuperclass();
    }

    throw new IOException(String.format("unknown principal type '%s'", type.getName()));
  }

  <T> void registerPrincipal(final T principal) throws IOException {
    @SuppressWarnings("unchecked")
    final Class<T> principalType = (Class<T>) principal.getClass();
    getRegistrar(principalType).registerPrincipal.accept(gameData, principal);
  }

  private static final class Registrar<T> {
    final BiFunction<GameData, String, T> findPrincipal;
    final BiConsumer<GameData, T> registerPrincipal;

    Registrar(
        final BiConsumer<GameData, T> registerPrincipal,
        final BiFunction<GameData, String, T> findPrincipal) {
      this.findPrincipal = findPrincipal;
      this.registerPrincipal = registerPrincipal;
    }
  }
}
