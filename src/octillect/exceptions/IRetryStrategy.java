package octillect.exceptions;

@FunctionalInterface
public interface IRetryStrategy {
    /**
     * The retrying Strategy for the retry button.
     * @return true if success and false if fails.
     */
    boolean retry();
}
