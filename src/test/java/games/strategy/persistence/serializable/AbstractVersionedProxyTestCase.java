package games.strategy.persistence.serializable;

import static com.google.common.base.Preconditions.checkNotNull;
import static games.strategy.persistence.serializable.TestProxyUtil.deserializeFromBase16EncodedBytes;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

/**
 * A fixture for testing versioned serializable proxy classes to ensure they can successfully read all supported
 * versions of the proxy's serialized form.
 */
public abstract class AbstractVersionedProxyTestCase {
  /**
   * Initializes a new instance of the {@code AbstractVersionedProxyTestCase} class.
   */
  protected AbstractVersionedProxyTestCase() {}

  /**
   * Asserts that the specified subjects are equal.
   *
   * <p>
   * This implementation compares the two objects using the {@code equals} method.
   * </p>
   *
   * @param expected The expected subject; must not be {@code null}.
   * @param actual The actual subject; may be {@code null}.
   *
   * @throws AssertionError If the two subjects are not equal.
   */
  protected void assertSubjectEquals(final Object expected, final Object actual) {
    assertThat(actual, is(expected));
  }

  /**
   * Returns a collection of supported serialized proxy representations that will be tested to ensure they can be read
   * by the current proxy version.
   *
   * @return A collection of supported serialized proxy representations; never {@code null}.
   */
  protected abstract List<SupportedVersion> getSupportedVersions();

  @Test
  public void proxy_ShouldBeAbleToReadAllSupportedVersions() throws Exception {
    for (final SupportedVersion supportedVersion : getSupportedVersions()) {
      final Object actual = deserializeFromBase16EncodedBytes(supportedVersion.base16EncodedBytes);

      assertSubjectEquals(supportedVersion.expected, actual);
    }
  }

  /**
   * Encapsulates information about a single serializable proxy version used by the test fixture to ensure the proxy
   * supports reading it.
   */
  protected static final class SupportedVersion {
    /**
     * The base16-encoded serialized representation of the proxy version to be read by the current proxy version; never
     * {@code null}.
     */
    public final String base16EncodedBytes;

    /**
     * The expected deserialized object; never {@code null}.
     */
    public final Object expected;

    /**
     * Initializes a new instance of the {@code SupportedVersion} class.
     *
     * @param expected The expected deserialized object; must not be {@code null}.
     * @param base16EncodedBytes The base16-encoded serialized representation of the proxy version to be read by the
     *        current proxy version; must not be {@code null}.
     */
    public SupportedVersion(final Object expected, final String base16EncodedBytes) {
      checkNotNull(expected);
      checkNotNull(base16EncodedBytes);

      this.base16EncodedBytes = base16EncodedBytes;
      this.expected = expected;
    }
  }
}
