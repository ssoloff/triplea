package games.strategy.engine.data;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;

import games.strategy.engine.data.annotations.InternalDoNotExport;

/**
 * Fake implementation of {@link IAttachment} useful for testing.
 */
@Immutable // TODO: no longer true
public final class FakeAttachment implements IAttachment, Serializable {
  private static final long serialVersionUID = 3686559484645729844L;

  // TODO: modify FakeAttachment to actually store everything a normal attachment should (attachable, GameData)
  private @Nullable Attachable attachable;
  private final String name;

  public FakeAttachment(final String name) {
    checkNotNull(name);

    this.name = name;
  }

  // TODO: no longer make sense if we now compare attachedTo
  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    } else if (!(obj instanceof FakeAttachment)) {
      return false;
    }

    final FakeAttachment other = (FakeAttachment) obj;
    return Objects.equals(name, other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        // TODO: add attachedTo
        .toString();
  }

  @Override
  public @Nullable Attachable getAttachedTo() {
    return attachable;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  @InternalDoNotExport
  public void setAttachedTo(final @Nullable Attachable attachable) {
    this.attachable = attachable;
  }

  @Override
  @InternalDoNotExport
  public void setName(final String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void validate(final GameData data) {
    throw new UnsupportedOperationException();
  }
}
