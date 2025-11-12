package ai.cardsight.exception;

/**
 * Exception thrown when authentication fails.
 *
 * This exception is thrown when the API key is invalid, missing,
 * or the user doesn't have permission to access a resource.
 *
 * @since 0.1.0
 */
public class AuthenticationException extends CardSightException {

    /**
     * Constructs a new AuthenticationException with the specified message.
     *
     * @param message the error message
     */
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR", null, 401, null);
    }

    /**
     * Constructs a new AuthenticationException with the specified message and request ID.
     *
     * @param message the error message
     * @param requestId the request ID for debugging
     */
    public AuthenticationException(String message, String requestId) {
        super(message, "AUTHENTICATION_ERROR", requestId, 401, null);
    }

    /**
     * Constructs a new AuthenticationException with full details.
     *
     * @param message the error message
     * @param errorCode the specific error code from the API
     * @param requestId the request ID for debugging
     * @param cause the underlying cause
     */
    public AuthenticationException(String message, String errorCode, String requestId, Throwable cause) {
        super(message, errorCode, requestId, 401, cause);
    }
}