package jhyun.cbr.testing_supports;

import com.google.common.base.Verify;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class ThrownByHelper {

    public static class ThrownByPair {
        private String name;
        private ThrowableAssert.ThrowingCallable callable;
        private Class<? extends Throwable> expected;

        public ThrownByPair(String name, ThrowableAssert.ThrowingCallable callable, Class<? extends Throwable> expected) {
            this.name = name;
            this.callable = callable;
            this.expected = expected;
        }
    }

    public static ThrownByPair thrownByPair(
            final String name,
            final ThrowableAssert.ThrowingCallable callable,
            Class<? extends Throwable> expected) {
        return new ThrownByPair(name, callable, expected);
    }

    public static void assertThrownByPairs(ThrownByPair... pairs) {
        for (ThrownByPair it : pairs) {
            Verify.verify(it != null);
            assertThatThrownBy(it.callable)
                    .as(it.name)
                    .isInstanceOf(it.expected);
        }
    }
}
