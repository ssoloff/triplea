package games.strategy.engine.persistence.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;

import games.strategy.engine.data.GameData;

/**
 * A serializable proxy for a non-serializable principal.
 */
public interface Proxy {
  /**
   * Creates a new proxy reader.
   *
   * @return A new proxy reader.
   */
  Reader newReader();

  /**
   * Creates a new proxy writer for the specified principal.
   *
   * @param principal The principal whose proxy is to be written.
   *
   * @return A new proxy writer.
   *
   * @throws ClassCastException If the type of {@code principal} is not supported by this proxy.
   */
  Writer newWriterFor(Object principal);

  /**
   * Reads a proxy to reconstruct the corresponding principal.
   */
  interface Reader {
    /**
     * Reads the proxy header.
     *
     * @param context The proxy reader context.
     *
     * @throws IOException If an I/O error occurs while reading the proxy header.
     * @throws ClassNotFoundException If a class could not be found while reading the proxy header.
     */
    void readHeader(Context context) throws IOException, ClassNotFoundException;

    /**
     * Reads the proxy body.
     *
     * <p>
     * This method will only be invoked after the proxy header (if any) has been read.
     * </p>
     *
     * @param context The proxy reader context.
     *
     * @return The principal created from the proxy.
     *
     * @throws IOException If an I/O error occurs while reading the proxy body.
     * @throws ClassNotFoundException If a class could not be found while reading the proxy body.
     */
    Object readBody(Context context) throws IOException, ClassNotFoundException;

    /**
     * The execution context for a proxy reader; allows the proxy reader to interact with the the overall I/O process.
     */
    interface Context {
      /**
       * Gets the game data into which all principals are to be placed.
       *
       * @return The game data.
       */
      GameData getGameData();

      /**
       * Gets the input stream from which the proxy will be read.
       *
       * @return The input stream.
       */
      ObjectInputStream getInputStream();

      /**
       * Gets the principal with the specified name and type from the game data.
       *
       * @param name The principal name.
       * @param type The principal type.
       *
       * @return The principal with the specified name and type.
       *
       * @throws IOException If no principal with the specified name and type exists in the game data.
       */
      default <T> T getPrincipal(final String name, final Class<T> type) throws IOException {
        return getPrincipal(name, Arrays.asList(type));
      }

      /**
       * Gets the principal with the specified name and any of the specified types from the game data.
       *
       * @param name The principal name.
       * @param types The collection of principal types. The types are searched in order for a principal with the
       *        specified name.
       *
       * @return The principal with the specified name and type.
       *
       * @throws IOException If no principal with the specified name and type exists in the game data.
       */
      <T> T getPrincipal(String name, Collection<Class<? extends T>> types) throws IOException;

      // TODO: may need readProxyHeader; see how implementation of GameDataProxy evolves

      /**
       * Reads a proxy body from the context input stream and returns the associated principal.
       *
       * @return The principal created from the proxy.
       *
       * @throws IOException If an I/O error occurs while reading the proxy body.
       * @throws ClassNotFoundException If a class could not be found while reading the proxy body.
       */
      Object readProxyBody() throws IOException, ClassNotFoundException;

      /**
       * Registers the specified principal with the game data.
       *
       * @param principal The principal to register.
       *
       * @throws IOException If the principal type is not supported.
       */
      void registerPrincipal(Object principal) throws IOException;
    }
  }

  /**
   * Writes the proxy corresponding to a principal.
   */
  interface Writer {
    /**
     * Writes the proxy header.
     *
     * @param context The proxy writer context.
     *
     * @throws IOException If an I/O error occurs while writing the proxy header.
     */
    void writeHeader(Context context) throws IOException;

    /**
     * Writes the proxy body.
     *
     * <p>
     * This method will only be invoked after the proxy header (if any) has been written.
     * </p>
     *
     * @param context The proxy writer context.
     *
     * @throws IOException If an I/O error occurs while writing the proxy body.
     */
    void writeBody(Context context) throws IOException;

    /**
     * The execution context for a proxy writer; allows the proxy writer to interact with the the overall I/O process.
     */
    interface Context {
      /**
       * Gets the output stream into which the proxy will be written.
       *
       * @return The output stream.
       */
      ObjectOutputStream getOutputStream();

      /**
       * Writes the proxy header for the specified principal to the context output stream.
       *
       * @param principal The principal whose proxy header is to be written.
       *
       * @throws IOException If an I/O error occurs while writing the proxy header.
       */
      void writeProxyHeader(Object principal) throws IOException;

      /**
       * Writes the proxy body for the specified principal to the context output stream.
       *
       * @param principal The principal whose proxy body is to be written.
       *
       * @throws IOException If an I/O error occurs while writing the proxy body.
       */
      void writeProxyBody(Object principal) throws IOException;
    }
  }
}
