package games.strategy.engine.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import games.strategy.engine.data.Named;
import games.strategy.util.IntegerMap;

/**
 * A collection of methods for proxy implementors to assist in reading and writing common data types.
 */
public final class ProxyUtils {
  private ProxyUtils() {}

  /**
   * Reads a named reference from the context input stream and returns the associated principal.
   *
   * @param context The proxy reader context.
   * @param principalType The type of the principal.
   *
   * @return The principal associated with the named reference or {@code null} if the reference was empty.
   *
   * @throws IOException If an I/O error occurs while reading the named reference or if the named reference does not
   *         exist in the context game data.
   */
  public static @Nullable <T extends Named> T readNamedReference(
      final Proxy.Reader.Context context,
      final Class<T> principalType)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principalType);

    final String name = context.getInputStream().readUTF();
    return name.isEmpty() ? null : context.getPrincipal(name, principalType);
  }

  /**
   * Writes a named reference for the specified principal.
   *
   * @param context The proxy writer context.
   * @param principal The principal or {@code null} if an empty reference should be written.
   *
   * @throws IOException If an I/O error occurs while writing the named reference.
   */
  public static void writeNamedReference(
      final Proxy.Writer.Context context,
      final @Nullable Named principal)
      throws IOException {
    checkNotNull(context);

    context.getOutputStream().writeUTF(principal != null ? principal.getName() : "");
  }

  /**
   * Reads a collection of named references from the context input stream and returns the associated principals.
   *
   * @param context The proxy reader context.
   * @param principalType The type of the principal.
   *
   * @return The collection of principals associated with the named references.
   *
   * @throws IOException If an I/O error occurs while reading the collection of named references or if a named reference
   *         does not exist in the context game data.
   */
  public static <T extends Named> Collection<T> readNamedReferenceCollection(
      final Proxy.Reader.Context context,
      final Class<T> principalType)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principalType);

    final ObjectInputStream ois = context.getInputStream();
    final int size = ois.readInt();
    final Collection<T> principals = new ArrayList<>(size);
    for (int i = 0; i < size; ++i) {
      final String name = ois.readUTF();
      principals.add(context.getPrincipal(name, principalType));
    }
    return principals;
  }

  /**
   * Writes a collection of named references for the specified principals.
   *
   * @param context The proxy writer context.
   * @param principals The principals.
   *
   * @throws IOException If an I/O error occurs while writing the collection of named references.
   */
  public static void writeNamedReferenceCollection(
      final Proxy.Writer.Context context,
      final Collection<? extends Named> principals)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principals);

    final ObjectOutputStream oos = context.getOutputStream();
    oos.writeInt(principals.size());
    for (final Named principal : principals) {
      oos.writeUTF(principal.getName());
    }
  }

  /**
   * Reads an integer map of named references from the context input stream and returns the associated integer map of
   * principals.
   *
   * @param context The proxy reader context.
   * @param principalType The type of the principal.
   *
   * @return The integer map of principals associated with the named references.
   *
   * @throws IOException If an I/O error occurs while reading the integer map of named references or if a named
   *         reference does not exist in the context game data.
   */
  public static <T extends Named> IntegerMap<T> readNamedReferenceIntegerMap(
      final Proxy.Reader.Context context,
      final Class<T> principalType)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principalType);

    return readNamedReferenceIntegerMap(context, Arrays.asList(principalType));
  }

  /**
   * Reads an integer map of named references from the context input stream and returns the associated integer map of
   * principals.
   *
   * @param context The proxy reader context.
   * @param principalTypes The collection of principal types. The types are searched in order for a principal with the
   *        specified name.
   *
   * @return The integer map of principals associated with the named references.
   *
   * @throws IOException If an I/O error occurs while reading the integer map of named references or if a named
   *         reference does not exist in the context game data.
   */
  public static <T extends Named> IntegerMap<T> readNamedReferenceIntegerMap(
      final Proxy.Reader.Context context,
      final Collection<Class<? extends T>> principalTypes)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principalTypes);

    final ObjectInputStream ois = context.getInputStream();
    final int size = ois.readInt();
    final IntegerMap<T> valuesByPrincipal = new IntegerMap<>();
    for (int i = 0; i < size; ++i) {
      final String name = ois.readUTF();
      final int value = ois.readInt();
      valuesByPrincipal.add(context.getPrincipal(name, principalTypes), value);
    }
    return valuesByPrincipal;
  }

  /**
   * Writes an integer map of named references for the specified integer map of principals.
   *
   * @param context The proxy writer context.
   * @param valuesByPrincipal The integer map of principals.
   *
   * @throws IOException If an I/O error occurs while writing the integer map of named references.
   */
  public static void writeNamedReferenceIntegerMap(
      final Proxy.Writer.Context context,
      final IntegerMap<? extends Named> valuesByPrincipal)
      throws IOException {
    checkNotNull(context);
    checkNotNull(valuesByPrincipal);

    final ObjectOutputStream oos = context.getOutputStream();
    oos.writeInt(valuesByPrincipal.size());
    for (final Map.Entry<? extends Named, Integer> entry : valuesByPrincipal.entrySet()) {
      oos.writeUTF(entry.getKey().getName());
      oos.writeInt(entry.getValue());
    }
  }

  /**
   * Reads a collection of proxy bodies from the context input stream and returns the associated principals.
   *
   * @param context The proxy reader context.
   * @param principalType The type of the principal.
   *
   * @return The collection of principals associated with the proxy bodies.
   *
   * @throws IOException If an I/O error occurs while reading the collection of proxy bodies.
   * @throws ClassNotFoundException If a class could not be found while reading the proxy bodies.
   */
  public static <T> Collection<T> readProxyBodyCollection(
      final Proxy.Reader.Context context,
      final Class<T> principalType)
      throws IOException, ClassNotFoundException {
    checkNotNull(context);
    checkNotNull(principalType);

    final ObjectInputStream ois = context.getInputStream();
    final int size = ois.readInt();
    final Collection<T> principals = new ArrayList<>(size);
    for (int i = 0; i < size; ++i) {
      principals.add(principalType.cast(context.readProxyBody()));
    }
    return principals;
  }

  /**
   * Writes a collection of proxy bodies for the specified principals.
   *
   * @param context The proxy writer context.
   * @param principals The principals.
   *
   * @throws IOException If an I/O error occurs while writing the collection of proxy bodies.
   */
  public static void writeProxyBodyCollection(
      final Proxy.Writer.Context context,
      final Collection<?> principals)
      throws IOException {
    checkNotNull(context);
    checkNotNull(principals);

    final ObjectOutputStream oos = context.getOutputStream();
    oos.writeInt(principals.size());
    for (final Object principal : principals) {
      context.writeProxyBody(principal);
    }
  }
}
