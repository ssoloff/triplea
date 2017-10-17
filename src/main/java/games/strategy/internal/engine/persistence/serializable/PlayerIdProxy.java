package games.strategy.internal.engine.persistence.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.concurrent.Immutable;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.PlayerID;
import games.strategy.engine.data.ProductionFrontier;
import games.strategy.engine.data.RepairFrontier;
import games.strategy.engine.data.Resource;
import games.strategy.engine.data.TechnologyFrontier;
import games.strategy.engine.persistence.serializable.AbstractNamedAttachableProxy;
import games.strategy.engine.persistence.serializable.ProxyUtils;

/**
 * A serializable proxy for the {@link PlayerID} class.
 */
@Immutable
public final class PlayerIdProxy extends AbstractNamedAttachableProxy<PlayerID> {
  public PlayerIdProxy() {
    super(PlayerID.class);
  }

  @Override
  protected Reader newTypeSafeReader() {
    return new Reader();
  }

  // TODO: add remaining attributes

  private static final class PlayerIdHeader extends NamedAttachableHeader {
    String defaultType;
    boolean disableable;
    boolean disabled;
    boolean hidden;
    boolean optional;
    String whoAmI;
  }

  private final class Reader extends AbstractReader<PlayerIdHeader> {
    Reader() {
      super(PlayerIdHeader::new);
    }

    @Override
    protected void readHeader(final Context context, final PlayerIdHeader header)
        throws IOException, ClassNotFoundException {
      super.readHeader(context, header);

      final ObjectInputStream ois = context.getInputStream();
      header.defaultType = ois.readUTF();
      header.disableable = ois.readBoolean();
      header.disabled = ois.readBoolean();
      header.hidden = ois.readBoolean();
      header.optional = ois.readBoolean();
      header.whoAmI = ois.readUTF();
    }

    @Override
    protected PlayerID createPrincipal(final GameData gameData, final PlayerIdHeader header) {
      final PlayerID principal =
          new PlayerID(header.name, header.optional, header.disableable, header.defaultType, header.hidden, gameData);
      principal.setIsDisabled(header.disabled);
      principal.setWhoAmI(header.whoAmI);
      return principal;
    }

    @Override
    protected void readBody(final Context context, final PlayerID principal)
        throws IOException, ClassNotFoundException {
      super.readBody(context, principal);

      readProductionFrontier(context, principal);
      readRepairFrontier(context, principal);
      readResources(context, principal);
      readTechnologyFrontiers(context, principal);
    }

    private void readProductionFrontier(final Context context, final PlayerID principal) throws IOException {
      principal.setProductionFrontier(ProxyUtils.readNamedReference(context, ProductionFrontier.class));
    }

    private void readRepairFrontier(final Context context, final PlayerID principal) throws IOException {
      principal.setRepairFrontier(ProxyUtils.readNamedReference(context, RepairFrontier.class));
    }

    private void readResources(final Context context, final PlayerID principal) throws IOException {
      principal.getResources().add(ProxyUtils.readNamedReferenceIntegerMap(context, Resource.class));
    }

    private void readTechnologyFrontiers(final Context context, final PlayerID principal)
        throws IOException, ClassNotFoundException {
      principal.getTechnologyFrontierList()
          .addTechnologyFrontiers(ProxyUtils.readProxyBodyCollection(context, TechnologyFrontier.class));
    }
  }

  @Override
  protected Writer newTypeSafeWriterFor(final PlayerID principal) {
    return new Writer(principal);
  }

  private final class Writer extends AbstractWriter {
    Writer(final PlayerID playerId) {
      super(playerId);
    }

    @Override
    public void writeHeader(final Context context) throws IOException {
      super.writeHeader(context);

      final PlayerID principal = getPrincipal();
      final ObjectOutputStream oos = context.getOutputStream();
      oos.writeUTF(principal.getDefaultType());
      oos.writeBoolean(principal.getCanBeDisabled());
      oos.writeBoolean(principal.getIsDisabled());
      oos.writeBoolean(principal.isHidden());
      oos.writeBoolean(principal.getOptional());
      oos.writeUTF(principal.getWhoAmI());
    }

    @Override
    public void writeBody(final Context context) throws IOException {
      super.writeBody(context);

      writeProductionFrontier(context);
      writeRepairFrontier(context);
      writeResources(context);
      writeTechnologyFrontiers(context);
    }

    private void writeProductionFrontier(final Context context) throws IOException {
      ProxyUtils.writeNamedReference(context, getPrincipal().getProductionFrontier());
    }

    private void writeRepairFrontier(final Context context) throws IOException {
      ProxyUtils.writeNamedReference(context, getPrincipal().getRepairFrontier());
    }

    private void writeResources(final Context context) throws IOException {
      ProxyUtils.writeNamedReferenceIntegerMap(context, getPrincipal().getResources().getResourcesCopy());
    }

    private void writeTechnologyFrontiers(final Context context) throws IOException {
      ProxyUtils.writeProxyBodyCollection(context, getPrincipal().getTechnologyFrontierList().getFrontiers());
    }
  }
}
