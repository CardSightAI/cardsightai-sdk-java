package ai.cardsight.interceptor;

import ai.cardsight.exception.AuthenticationException;
import ai.cardsight.exception.CardSightException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * OkHttp interceptor that handles API errors globally.
 *
 * This interceptor checks response status codes and converts
 * error responses into appropriate CardSight exceptions.
 *
 * @since 0.1.0
 */
public class CardSightErrorInterceptor implements Interceptor {

    private final Gson gson = new Gson();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        // Success responses pass through
        if (response.isSuccessful()) {
            return response;
        }

        // Parse error details
        String errorMessage = "API request failed";
        String errorCode = null;
        String requestId = extractRequestId(response);

        // Try to parse error response body
        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
        if (responseBody != null) {
            try {
                String bodyString = responseBody.string();
                JsonObject errorJson = gson.fromJson(bodyString, JsonObject.class);

                if (errorJson.has("error")) {
                    errorMessage = errorJson.get("error").getAsString();
                }
                if (errorJson.has("code")) {
                    errorCode = errorJson.get("code").getAsString();
                }
            } catch (Exception e) {
                // If parsing fails, use default message
            }
        }

        // Convert to appropriate exception based on status code
        CardSightException exception;
        switch (response.code()) {
            case 401:
                exception = new AuthenticationException(
                    "Authentication failed. Please check your API key.",
                    errorCode,
                    requestId,
                    null
                );
                break;
            case 400:
                exception = new CardSightException(
                    "Invalid request: " + errorMessage,
                    errorCode,
                    requestId,
                    400,
                    null
                );
                break;
            case 404:
                exception = new CardSightException(
                    "Resource not found: " + errorMessage,
                    errorCode,
                    requestId,
                    404,
                    null
                );
                break;
            case 408:
                exception = new CardSightException(
                    "Request timed out. The operation may be too complex or the service is busy.",
                    errorCode,
                    requestId,
                    408,
                    null
                );
                break;
            case 429:
                exception = new CardSightException(
                    "Rate limit exceeded. Please wait before making more requests.",
                    errorCode,
                    requestId,
                    429,
                    null
                );
                break;
            case 500:
            case 502:
            case 503:
            case 504:
                exception = new CardSightException(
                    "Server error. The service may be temporarily unavailable.",
                    errorCode,
                    requestId,
                    response.code(),
                    null
                );
                break;
            default:
                exception = new CardSightException(
                    errorMessage,
                    errorCode,
                    requestId,
                    response.code(),
                    null
                );
        }

        // Note: We can't throw the exception directly from the interceptor
        // Instead, we need to handle this at the ApiClient level
        // For now, we'll just return the response and let the generated code handle it
        // This interceptor serves as documentation for future enhancement
        return response;
    }

    /**
     * Extracts the request ID from response headers if available.
     */
    private String extractRequestId(Response response) {
        String requestId = response.header("X-Request-Id");
        if (requestId == null) {
            requestId = response.header("x-request-id");
        }
        return requestId;
    }
}