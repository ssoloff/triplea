package games.strategy.internal.persistence.serializable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.strategy.persistence.serializable.AbstractVersionedProxyTestCase;
import games.strategy.util.memento.PropertyBagMemento;

public final class PropertyBagMementoProxyAsVersionedProxyTest
    extends AbstractVersionedProxyTestCase<PropertyBagMemento> {
  public PropertyBagMementoProxyAsVersionedProxyTest() {
    super(PropertyBagMemento.class);
  }

  @Override
  protected List<SupportedVersion<PropertyBagMemento>> getSupportedVersions() {
    return Arrays.asList(v1());
  }

  private static SupportedVersion<PropertyBagMemento> v1() {
    final String base16EncodedBytes = ""
        + "ACED00057372004867616D65732E73747261746567792E696E7465726E616C2E" // ....sr.Hgames.strategy.internal.
        + "70657273697374656E63652E73657269616C697A61626C652E50726F70657274" // persistence.serializable.Propert
        + "794261674D656D656E746F50726F78796C6EA60E0C4A88670C00007870771400" // yBagMementoProxyln...J.g...xpw..
        + "00000000000001000269640000000000000008737200116A6176612E7574696C" // .........id........sr..java.util
        + "2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F72" // .HashMap......`....F..loadFactor
        + "4900097468726573686F6C6478703F4000000000000377080000000400000002" // I..thresholdxp?@......w.........
        + "74000970726F7065727479327400043231313274000970726F70657274793173" // t..property2t..2112t..property1s
        + "72000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576" // r..java.lang.Long;.....#....J..v
        + "616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B" // aluexr..java.lang.Number........
        + "0200007870000000000000002A7878" //////////////////////////////////// ...xp.......*xx
        + "";
    final Map<String, Object> propertiesByName = new HashMap<>();
    propertiesByName.put("property1", 42L);
    propertiesByName.put("property2", "2112");
    final PropertyBagMemento expected = new PropertyBagMemento("id", 8L, propertiesByName);
    return new SupportedVersion<>(expected, base16EncodedBytes);
  }
}
