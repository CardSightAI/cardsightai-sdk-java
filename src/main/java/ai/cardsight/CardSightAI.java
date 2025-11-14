package ai.cardsight;

import ai.cardsight.config.CardSightConfig;
import ai.cardsight.interceptor.CardSightErrorInterceptor;
import ai.cardsight.generated.api.*;
import ai.cardsight.generated.client.ApiClient;
import ai.cardsight.generated.client.Configuration;
import jakarta.inject.Inject;
import okhttp3.OkHttpClient;
import java.time.Duration;
import java.util.Objects;

/**
 * Main client for the CardSight AI SDK.
 *
 * This class provides a simplified interface to interact with the CardSight AI API,
 * offering methods for card identification, catalog management, collection tracking, and more.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * // Initialize with API key
 * CardSightAI client = new CardSightAI("your-api-key");
 *
 * // Or use environment variable CARDSIGHTAI_API_KEY
 * CardSightAI client = new CardSightAI();
 *
 * // Identify a card from an image
 * var response = client.cardIdentification().v1IdentifyCardPost(imageFile);
 * if (Boolean.TRUE.equals(response.getSuccess())) {
 *     System.out.println("Found " + response.getDetections().size() + " cards");
 * }
 * }</pre>
 *
 * @since 0.1.0
 */
public class CardSightAI {

    private static final String DEFAULT_BASE_URL = "https://api.cardsight.ai";
    private static final String API_KEY_ENV_VAR = "CARDSIGHTAI_API_KEY";
    private static final String USER_AGENT = "CardSightAI-Java-SDK";

    private final CardSightConfig config;
    private final ApiClient apiClient;

    /**
     * Creates a new CardSightAI client using the API key from the environment variable.
     *
     * The API key will be read from the CARDSIGHTAI_API_KEY environment variable.
     *
     * @throws IllegalStateException if the environment variable is not set
     */
    public CardSightAI() {
        this(getApiKeyFromEnvironment());
    }

    /**
     * Creates a new CardSightAI client with the specified API key.
     *
     * @param apiKey the API key for authentication
     * @throws NullPointerException if apiKey is null
     */
    public CardSightAI(String apiKey) {
        this(CardSightConfig.builder()
                .apiKey(apiKey)
                .build());
    }

    /**
     * Creates a new CardSightAI client with custom configuration.
     *
     * @param config the configuration for the client
     * @throws NullPointerException if config is null
     */
    @Inject
    public CardSightAI(CardSightConfig config) {
        this.config = Objects.requireNonNull(config, "Configuration cannot be null");
        this.apiClient = createApiClient();
        Configuration.setDefaultApiClient(this.apiClient);
    }

    /**
     * Creates a new CardSightAI client builder.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the API key from the environment variable.
     *
     * @return the API key from CARDSIGHTAI_API_KEY environment variable
     * @throws IllegalStateException if the environment variable is not set
     */
    private static String getApiKeyFromEnvironment() {
        String apiKey = System.getenv(API_KEY_ENV_VAR);
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException(
                "API key not found. Please set the " + API_KEY_ENV_VAR +
                " environment variable or pass the API key directly to the constructor."
            );
        }
        return apiKey;
    }

    /**
     * Creates and configures the underlying API client.
     */
    private ApiClient createApiClient() {
        // Create OkHttp client with error interceptor
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(new CardSightErrorInterceptor());

        // Set timeout if configured
        if (config.getTimeout() != null) {
            httpClientBuilder
                .connectTimeout(config.getTimeout())
                .readTimeout(config.getTimeout())
                .writeTimeout(config.getTimeout());
        }

        OkHttpClient httpClient = httpClientBuilder.build();

        // Create API client with custom HTTP client
        ApiClient client = new ApiClient(httpClient);

        // Set base URL
        String baseUrl = config.getBaseUrl() != null ? config.getBaseUrl() : DEFAULT_BASE_URL;
        client.setBasePath(baseUrl);

        // Set API key
        client.setApiKey(config.getApiKey());
        client.setApiKeyPrefix("X-API-Key");

        // Set user agent
        client.setUserAgent(USER_AGENT + "/" + getVersion());

        // Add custom headers
        if (config.getHeaders() != null) {
            config.getHeaders().forEach(client::addDefaultHeader);
        }

        return client;
    }

    /**
     * Gets the SDK version.
     *
     * @return the SDK version string
     */
    private String getVersion() {
        Package pkg = getClass().getPackage();
        String version = pkg != null ? pkg.getImplementationVersion() : null;
        return version != null ? version : "0.1.0";
    }

    // Direct API accessors - Zero maintenance, automatic updates

    /**
     * Gets the AI API for natural language search operations.
     *
     * @return the AI API instance
     */
    public AiApi ai() {
        return new AiApi(apiClient);
    }

    /**
     * Gets the Autocomplete API for card and set autocomplete.
     *
     * @return the Autocomplete API instance
     */
    public AutocompleteApi autocomplete() {
        return new AutocompleteApi(apiClient);
    }

    /**
     * Gets the Catalog API for browsing and searching the card catalog.
     *
     * @return the Catalog API instance
     */
    public CatalogApi catalog() {
        return new CatalogApi(apiClient);
    }

    /**
     * Gets the Collection Management API for managing card collections.
     *
     * @return the Collection Management API instance
     */
    public CollectionManagementApi collections() {
        return new CollectionManagementApi(apiClient);
    }

    /**
     * Gets the Collectors API for managing collector profiles.
     *
     * @return the Collectors API instance
     */
    public CollectorsApi collectors() {
        return new CollectorsApi(apiClient);
    }

    /**
     * Gets the Collection Card Images API for managing collection card images.
     *
     * @return the Collection Card Images API instance
     */
    public CollectionCardImagesApi collectionImages() {
        return new CollectionCardImagesApi(apiClient);
    }

    /**
     * Gets the Feedback API for submitting feedback.
     *
     * @return the Feedback API instance
     */
    public FeedbackApi feedback() {
        return new FeedbackApi(apiClient);
    }

    /**
     * Gets the Grades API for grading company information.
     *
     * @return the Grades API instance
     */
    public GradesApi grading() {
        return new GradesApi(apiClient);
    }

    /**
     * Gets the Health API for checking API status.
     *
     * @return the Health API instance
     */
    public HealthApi health() {
        return new HealthApi(apiClient);
    }

    /**
     * Gets the Images API for card image retrieval.
     *
     * @return the Images API instance
     */
    public ImagesApi images() {
        return new ImagesApi(apiClient);
    }

    /**
     * Gets the Lists API for managing want lists.
     *
     * @return the Lists API instance
     */
    public ListsApi lists() {
        return new ListsApi(apiClient);
    }

    /**
     * Gets the Subscription API for subscription management.
     *
     * @return the Subscription API instance
     */
    public SubscriptionApi subscription() {
        return new SubscriptionApi(apiClient);
    }

    /**
     * Gets the Card Identification API directly for advanced use.
     * For simple file uploads, use {@link #identify()} instead.
     *
     * @return the Card Identification API instance
     */
    public CardIdentificationApi cardIdentification() {
        return new CardIdentificationApi(apiClient);
    }


    /**
     * Gets the underlying API client for advanced usage.
     *
     * @return the API client
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * Gets the configuration used by this client.
     *
     * @return the configuration
     */
    public CardSightConfig getConfig() {
        return config;
    }

    /**
     * Builder for creating CardSightAI instances with custom configuration.
     */
    public static class Builder {
        private final CardSightConfig.Builder configBuilder = CardSightConfig.builder();

        /**
         * Sets the API key for authentication.
         *
         * @param apiKey the API key
         * @return this builder
         */
        public Builder apiKey(String apiKey) {
            configBuilder.apiKey(apiKey);
            return this;
        }

        /**
         * Sets the base URL for the API.
         *
         * @param baseUrl the base URL
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * Sets the timeout duration for API calls.
         *
         * @param duration the timeout value
         * @return this builder
         */
        public Builder timeout(Duration duration) {
            configBuilder.timeout(duration);
            return this;
        }

        /**
         * Adds a custom header to be sent with all API requests.
         *
         * @param name the header name
         * @param value the header value
         * @return this builder
         */
        public Builder addHeader(String name, String value) {
            configBuilder.addHeader(name, value);
            return this;
        }

        /**
         * Builds a new CardSightAI instance with the configured settings.
         *
         * @return a new CardSightAI client
         */
        public CardSightAI build() {
            return new CardSightAI(configBuilder.build());
        }
    }
}
