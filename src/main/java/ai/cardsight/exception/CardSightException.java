package ai.cardsight.exception;

/**
 * Base exception for all CardSight AI SDK errors.
 *
 * This is the parent class for all exceptions thrown by the SDK.
 * It provides common functionality for error handling including
 * error codes and request IDs for debugging.
 *
 * @since 0.1.0
 */
public class CardSightException extends RuntimeException {

    private final String errorCode;
    private final String requestId;
    private final Integer statusCode;

    /**
     * Constructs a new CardSightException with the specified message.
     *
     * @param message the error message
     */
    public CardSightException(String message) {
        this(message, null, null, null, null);
    }

    /**
     * Constructs a new CardSightException with the specified message and cause.
     *
     * @param message the error message
     * @param cause the cause of the exception
     */
    public CardSightException(String message, Throwable cause) {
        this(message, null, null, null, cause);
    }

    /**
     * Constructs a new CardSightException with full error details.
     *
     * @param message the error message
     * @param errorCode the error code from the API
     * @param requestId the request ID for debugging
     * @param statusCode the HTTP status code
     * @param cause the cause of the exception
     */
    public CardSightException(String message, String errorCode, String requestId,
                            Integer statusCode, Throwable cause) {
        super(buildMessage(message, errorCode, requestId, statusCode), cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
        this.statusCode = statusCode;
    }

    /**
     * Builds the exception message with all available details.
     */
    private static String buildMessage(String message, String errorCode,
                                      String requestId, Integer statusCode) {
        StringBuilder sb = new StringBuilder();

        if (message != null) {
            sb.append(message);
        }

        boolean hasDetails = false;
        if (errorCode != null || requestId != null || statusCode != null) {
            sb.append(" [");

            if (statusCode != null) {
                sb.append("Status: ").append(statusCode);
                hasDetails = true;
            }

            if (errorCode != null) {
                if (hasDetails) sb.append(", ");
                sb.append("Code: ").append(errorCode);
                hasDetails = true;
            }

            if (requestId != null) {
                if (hasDetails) sb.append(", ");
                sb.append("Request ID: ").append(requestId);
            }

            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * Gets the error code returned by the API.
     *
     * @return the error code, or null if not available
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the request ID for debugging purposes.
     *
     * @return the request ID, or null if not available
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Gets the HTTP status code of the failed request.
     *
     * @return the status code, or null if not available
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Checks if this is a client error (4xx status code).
     *
     * @return true if this is a client error
     */
    public boolean isClientError() {
        return statusCode != null && statusCode >= 400 && statusCode < 500;
    }

    /**
     * Checks if this is a server error (5xx status code).
     *
     * @return true if this is a server error
     */
    public boolean isServerError() {
        return statusCode != null && statusCode >= 500 && statusCode < 600;
    }
}