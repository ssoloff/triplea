package games.strategy.util.memento;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link Memento} that represents the originator state as a property bag.
 *
 * <p>
 * Instances of this class are immutable.
 * </p>
 */
public final class PropertyBagMemento implements Memento {
  private final String id;

  private final Map<String, Object> propertiesByName;

  private final long version;

  /**
   * Initializes a new instance of the {@code PropertyBagMemento} class.
   *
   * @param id The memento identifier; must not be {@code null}.
   * @param version The memento version.
   * @param propertiesByName The collection of originator properties; must not be {@code null}. The key is the property
   *        name. The value is the property value.
   */
  public PropertyBagMemento(final String id, final long version, final Map<String, Object> propertiesByName) {
    checkNotNull(id);
    checkNotNull(propertiesByName);

    this.id = id;
    this.propertiesByName = new HashMap<>(propertiesByName);
    this.version = version;
  }

  /**
   * Gets the memento identifier.
   *
   * @return The memento identifier; never {@code null}.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the collection of originator properties.
   *
   * @return The collection of originator properties; never {@code null}. The key is the property name. The value is the
   *         property value.
   */
  public Map<String, Object> getPropertiesByName() {
    return new HashMap<>(propertiesByName);
  }

  /**
   * Gets the memento version.
   *
   * @return The memento version.
   */
  public long getVersion() {
    return version;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    } else if (!(obj instanceof PropertyBagMemento)) {
      return false;
    }

    final PropertyBagMemento other = (PropertyBagMemento) obj;
    return id.equals(other.id)
        && (version == other.version)
        && propertiesByName.equals(other.propertiesByName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version, propertiesByName);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("PropertyBagMemento[");
    sb.append("id=");
    sb.append(id);
    sb.append(", propertiesByName=");
    sb.append(propertiesByName);
    sb.append(", version=");
    sb.append(version);
    sb.append("]");
    return sb.toString();
  }
}
