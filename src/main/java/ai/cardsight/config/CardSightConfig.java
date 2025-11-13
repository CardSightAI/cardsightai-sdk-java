package ai.cardsight.config;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Configuration for the CardSight AI client.
 *
 * This class holds all configuration options for the SDK including API key,
 * base URL, timeout settings, and custom headers.
 *
 * <p>Use the builder to create instances:</p>
 * <pre>{@code
 * CardSightConfig config = CardSightConfig.builder()
 *     .apiKey("your-api-key")
 *     .timeout(Duration.ofSeconds(30))
 *     .addHeader("Custom-Header", "value")
 *     .build();
 * }</pre>
 *
 * @since 0.1.0
 */
public final class CardSightConfig {

    private final String apiKey;
    @Nullable private final String baseUrl;
    @Nullable private final Duration timeout;
    private final Map<String, String> headers;

    private CardSightConfig(Builder builder) {
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API key cannot be null");
        this.baseUrl = builder.baseUrl;
        this.timeout = builder.timeout;
        this.headers = Collections.unmodifiableMap(builder.headers);
    }

    /**
     * Gets the API key for authentication.
     *
     * @return the API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Gets the base URL for the API.
     *
     * @return the base URL, or null to use the default
     */
    public @Nullable String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Gets the timeout duration for API calls.
     *
     * @return the timeout duration, or null for default
     */
    public @Nullable Duration getTimeout() {
        return timeout;
    }

    /**
     * Gets the custom headers to be sent with all API requests.
     *
     * @return an unmodifiable map of headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Creates a new builder for CardSightConfig.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for CardSightConfig.
     */
    public static final class Builder {
        private String apiKey;
        @Nullable private String baseUrl;
        @Nullable private Duration timeout;
        private final Map<String, String> headers = new HashMap<>();

        private Builder() {
            // Private constructor to force use of builder() method
        }

        /**
         * Sets the API key for authentication.
         *
         * @param apiKey the API key
         * @return this builder
         * @throws NullPointerException if apiKey is null
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = Objects.requireNonNull(apiKey, "API key cannot be null");
            return this;
        }

        /**
         * Sets the base URL for the API.
         *
         * @param baseUrl the base URL
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * Sets the timeout duration for API calls.
         *
         * @param duration the timeout duration
         * @return this builder
         */
        public Builder timeout(Duration duration) {
            this.timeout = duration;
            return this;
        }

        /**
         * Adds a custom header to be sent with all API requests.
         *
         * @param name the header name
         * @param value the header value
         * @return this builder
         * @throws NullPointerException if name or value is null
         */
        public Builder addHeader(String name, String value) {
            Objects.requireNonNull(name, "Header name cannot be null");
            Objects.requireNonNull(value, "Header value cannot be null");
            this.headers.put(name, value);
            return this;
        }

        /**
         * Adds multiple custom headers to be sent with all API requests.
         *
         * @param headers the headers to add
         * @return this builder
         * @throws NullPointerException if headers is null
         */
        public Builder addHeaders(Map<String, String> headers) {
            Objects.requireNonNull(headers, "Headers cannot be null");
            headers.forEach(this::addHeader);
            return this;
        }

        /**
         * Builds a new CardSightConfig instance.
         *
         * @return a new CardSightConfig
         * @throws NullPointerException if apiKey has not been set
         */
        public CardSightConfig build() {
            return new CardSightConfig(this);
        }
    }
}