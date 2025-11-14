# CardSight AI Java SDK

The official Java SDK for [CardSight AI](https://cardsight.ai) - Trading Card Identification and Collection Management API.

## Features

- **AI-Powered Card Identification** - Identify trading cards from images with confidence scoring
- **2M+ Card Catalog** - Search and browse our comprehensive database
- **Collection Management** - Track your collection with purchase history and analytics
- **Want Lists** - Manage cards you're looking to acquire
- **Grading Integration** - Track grading information from major companies
- **Natural Language Search** - Find cards using AI-powered search
- **Dependency Injection Support** - Built-in Jakarta Inject annotations for Spring, CDI, and Guice
- **Auto-Generated from OpenAPI** - Always up-to-date with the latest API changes
- **Zero Maintenance** - Direct access to generated API methods means no manual updates needed

## Requirements

- Java 21 or higher (LTS)
- Maven 3.6 or higher

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>ai.cardsight</groupId>
    <artifactId>cardsightai-sdk-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'ai.cardsight:cardsightai-sdk-java:0.1.0'
```

## Quick Start

```java
import ai.cardsight.CardSightAI;
import ai.cardsight.generated.model.*;

public class QuickStart {
    public static void main(String[] args) {
        // Initialize with API key
        CardSightAI client = new CardSightAI("your-api-key");

        // Or use environment variable CARDSIGHTAI_API_KEY
        CardSightAI client = new CardSightAI();

        // Direct access to all generated API methods

        // Get a specific card
        var card = client.catalog().v1CatalogCardsIdGet("card-uuid");

        // Search for cards
        var cards = client.catalog().v1CatalogCardsGet(
            20,        // take
            0,         // skip
            null,      // segment
            null,      // manufacturer
            "2024",    // year
            null,      // min_year
            null,      // max_year
            null,      // release_id
            null,      // set_id
            "Charizard", // name
            null,      // number
            "name",    // sort
            "asc"      // order
        );

        // Check API health
        var health = client.health().healthGet();
        System.out.println("API Status: " + health.getStatus());
    }
}
```

### Dependency Injection

The SDK supports Jakarta Inject for use with CDI, Spring, Guice, and other DI frameworks:

```java
import ai.cardsight.ApiKey;
import ai.cardsight.CardSightAI;
import jakarta.inject.Inject;

public class MyService {
    private final CardSightAI client;

    // Inject the client directly if configured in your DI container
    @Inject
    public MyService(CardSightAI client) {
        this.client = client;
    }
}
```

## Configuration

### Environment Variable

Set your API key as an environment variable:

```bash
export CARDSIGHTAI_API_KEY="your-api-key"
```

Then initialize the client without parameters:

```java
CardSightAI client = new CardSightAI();
```

### Builder Pattern

Use the builder for advanced configuration:

```java
CardSightAI client = CardSightAI.builder()
    .apiKey("your-api-key")
    .baseUrl("https://api.cardsight.ai")
    .timeout(Duration.ofSeconds(30))
    .addHeader("Custom-Header", "value")
    .build();
```

## API Categories

### Card Identification

Identify trading cards from images using the generated API:

```java
// Identify from file
File imageFile = new File("card.jpg");
var response = client.cardIdentification().v1IdentifyCardPost(imageFile);

// Check success
if (Boolean.TRUE.equals(response.getSuccess())) {
    System.out.println("Request ID: " + response.getRequestId());
    System.out.println("Processing time: " + response.getProcessingTime() + "ms");

    // Process detections
    for (var detection : response.getDetections()) {
        System.out.println("Confidence: " + detection.getConfidence());
        var card = detection.getCard();
        System.out.println("Card: " + card.getName() + " - " + card.getCardSetName());
    }

    // Filter by confidence if needed
    var highConfidence = response.getDetections().stream()
        .filter(d -> "High".equals(d.getConfidence().toString()))
        .collect(Collectors.toList());
}

// Also supports byte arrays and streams
byte[] imageBytes = Files.readAllBytes(Paths.get("card.jpg"));
var response2 = client.cardIdentification().v1IdentifyCardPost(imageBytes);
```

### Catalog Search

Browse and search the card catalog using the generated API methods:

```java
// Search for cards - all parameters are strongly typed
var cards = client.catalog().v1CatalogCardsGet(
    50,          // take (max results)
    0,           // skip (offset)
    null,        // segment
    "Pokemon",   // manufacturer
    "2024",      // year
    null,        // min_year
    null,        // max_year
    null,        // release_id
    null,        // set_id
    "Charizard", // name
    null,        // number
    "name",      // sort field
    "asc"        // sort order
);

// Get random cards
var randomCards = client.catalog().v1CatalogCardsRandomGet(
    10,          // count
    null,        // segment
    null,        // manufacturer
    null,        // year
    null         // set_id
);

// Browse releases
var releases = client.catalog().v1CatalogReleasesGet(
    20,          // take
    0,           // skip
    "Sports",    // segment
    null,        // manufacturer
    "2023",      // year
    null,        // min_year
    null,        // max_year
    null,        // name
    "year",      // sort
    "desc"       // order
);

// Get specific card details
var card = client.catalog().v1CatalogCardsIdGet("card-uuid");
```

### Collection Management

Manage your card collection:

```java
// Create a collection
var request = new V1CollectionPostRequest();
request.setName("My Pokemon Collection");
request.setDescription("My personal Pokemon card collection");
var collection = client.collections().v1CollectionPost(request);

// Add cards to collection
var addRequest = new V1CollectionCollectionIdCardsPostRequest();
addRequest.setCardId(cardId);
addRequest.setBuyPrice(29.99);
addRequest.setBuyDate("2024-01-15");
addRequest.setCondition("Near Mint");
client.collections().v1CollectionCollectionIdCardsPost(
    collection.getId(),
    addRequest
);

// Get collection analytics
var analytics = client.collections().v1CollectionCollectionIdAnalyticsGet(
    collection.getId()
);

System.out.println("Total cards: " + analytics.getTotalCards());
```

### Want Lists

Track cards you want to acquire:

```java
// Create a want list
var listRequest = new V1ListsPostRequest();
listRequest.setName("Cards to Find");
listRequest.setDescription("My wish list");
var list = client.lists().v1ListsPost(listRequest);

// Add cards to want list
var cardRequest = new V1ListsListIdCardsPostRequest();
cardRequest.setCardId(cardId);
cardRequest.setMaxPrice(50.00);
cardRequest.setMinCondition("Excellent");
client.lists().v1ListsListIdCardsPost(list.getId(), cardRequest);

// Get list contents
var listCards = client.lists().v1ListsListIdCardsGet(
    list.getId(),
    20,  // take
    0    // skip
);
```

### Natural Language Search & Autocomplete

Use AI-powered features:

```java
// Natural language search
var query = new V1AiQueryPostRequest();
query.setQuery("rookie cards from 2023 worth over $100");
var results = client.ai().v1AiQueryPost(query);

// Autocomplete for cards
var suggestions = client.autocomplete().v1AutocompleteCardsGet(
    "Mich",  // query
    10       // limit
);

// Autocomplete for sets
var setSuggestions = client.autocomplete().v1AutocompleteSetsGet(
    "Chrome",  // query
    5          // limit
);
```

## Error Handling

The generated API throws `ApiException` for all error scenarios:

```java
import ai.cardsight.generated.client.ApiException;

try {
    var response = client.cardIdentification().v1IdentifyCardPost(imageFile);
} catch (ApiException e) {
    System.err.println("API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getCode());
    System.err.println("Response Body: " + e.getResponseBody());

    // Handle specific status codes
    switch (e.getCode()) {
        case 401:
            System.err.println("Authentication failed - check your API key");
            break;
        case 429:
            System.err.println("Rate limited - wait before retrying");
            break;
        case 500:
        case 503:
            System.err.println("Server error - retry later");
            break;
    }
}
```

## Pagination

Use pagination for large result sets with the take and skip parameters:

```java
// First page - get 20 cards starting at 0
var page1 = client.catalog().v1CatalogCardsGet(
    20,    // take (limit)
    0,     // skip (offset)
    null, null, null, null, null, null, null, null, null, null, null
);

// Next page - get next 20 cards
var page2 = client.catalog().v1CatalogCardsGet(
    20,    // take
    20,    // skip (start at card #21)
    null, null, null, null, null, null, null, null, null, null, null
);

// Process results
for (var card : page1.getCards()) {
    System.out.println(card.getName());
}
```

## SDK Architecture

This SDK uses a **simplified architecture** that directly exposes the auto-generated API classes. This approach provides:

1. **Zero maintenance overhead** - No manual wrapper methods to update
2. **Automatic API updates** - New endpoints are immediately available
3. **Full type safety** - All parameters and responses are strongly typed
4. **Complete API coverage** - Access to all 68+ endpoints without manual coding

### Direct API Access

The SDK exposes the generated API classes directly:

```java
CardSightAI client = new CardSightAI("api-key");

// Each API category is directly accessible
client.catalog()      // CatalogApi - 20+ methods
client.collections()  // CollectionManagementApi - 15+ methods
client.lists()       // ListsApi - 8+ methods
client.ai()          // AiApi - natural language search
client.grading()     // GradesApi - grading information
client.health()      // HealthApi - status checks
// ... and more
```

### Generated Method Names

The generated methods follow the OpenAPI operation pattern:
- `v1CatalogCardsGet` - GET /v1/catalog/cards
- `v1CollectionPost` - POST /v1/collection
- `v1ListsListIdCardsPost` - POST /v1/lists/{listId}/cards

While these names are verbose, they provide:
- Clear mapping to API endpoints
- No ambiguity about what each method does
- Automatic updates when endpoints change

## Advanced Usage

### Dependency Injection Configuration

The SDK includes built-in support for Jakarta Inject (JSR-330), making it compatible with CDI, Spring, Guice, and other dependency injection frameworks.

#### Spring Framework Example

Configure the SDK as a Spring bean:

```java
import ai.cardsight.ApiKey;
import ai.cardsight.CardSightAI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class CardSightConfig {

    @Bean
    @ApiKey
    public String apiKey(@Value("${cardsight.api.key}") String key) {
        return key;
    }

    @Bean
    public CardSightAI cardSightClient(@ApiKey String apiKey) {
        return new CardSightAI(apiKey);
    }
}
```

Then inject the client in your services:

```java
import ai.cardsight.CardSightAI;
import org.springframework.stereotype.Service;

@Service
public class CardService {
    private final CardSightAI client;

    @Autowired
    public CardService(CardSightAI client) {
        this.client = client;
    }

    public CardDetails getCard(String cardId) {
        return client.catalog().v1CatalogCardsIdGet(cardId);
    }
}
```

#### CDI (Jakarta EE) Example

Configure a CDI producer:

```java
import ai.cardsight.ApiKey;
import ai.cardsight.CardSightAI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class CardSightProducer {

    @Produces
    @ApiKey
    public String produceApiKey() {
        // Read from config, environment, etc.
        return System.getenv("CARDSIGHTAI_API_KEY");
    }

    @Produces
    @ApplicationScoped
    public CardSightAI produceClient(@ApiKey String apiKey) {
        return new CardSightAI(apiKey);
    }
}
```

Inject into your beans:

```java
import ai.cardsight.CardSightAI;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class CardSearchBean {

    @Inject
    private CardSightAI client;

    public List<Card> searchCards(String query) {
        return client.catalog().v1CatalogCardsGet(
            20, 0, null, null, null, null, null, null, null,
            query, null, "name", "asc"
        ).getCards();
    }
}
```

#### Guice Example

Configure a Guice module:

```java
import ai.cardsight.ApiKey;
import ai.cardsight.CardSightAI;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class CardSightModule extends AbstractModule {

    @Provides
    @ApiKey
    public String provideApiKey() {
        return System.getenv("CARDSIGHTAI_API_KEY");
    }

    @Provides
    @Singleton
    public CardSightAI provideClient(@ApiKey String apiKey) {
        return new CardSightAI(apiKey);
    }
}
```

### Custom HTTP Client Configuration

The SDK uses OkHttp by default. You can access the underlying API client for advanced configuration:

```java
ApiClient apiClient = client.getApiClient();
apiClient.setDebugging(true); // Enable debug logging
apiClient.setConnectTimeout(60000); // 60 seconds
```

### Rate Limiting

The API has rate limits. Handle rate limit errors appropriately:

```java
import ai.cardsight.generated.client.ApiException;

try {
    var response = client.catalog().v1CatalogCardsGet(/* parameters */);
} catch (ApiException e) {
    if (e.getCode() == 429) {
        // Rate limited - wait and retry
        Thread.sleep(1000);
        // Retry the request
    }
}
```

## Building from Source

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Node.js (for OpenAPI preprocessing)

### Build Process

Clone the repository and build with Maven:

```bash
git clone https://github.com/CardSightAI/cardsightai-sdk-java.git
cd cardsightai-sdk-java
mvn clean install
```

The build process automatically:
1. Downloads the OpenAPI specification (if using `-Pdownload-spec` profile)
2. Preprocesses the spec to remove JSON Schema meta-properties
3. Generates Java code from the cleaned specification
4. Compiles all source code

### Updating the OpenAPI Specification

The SDK is auto-generated from the OpenAPI specification. To update:

1. Download the latest spec:
```bash
mvn clean generate-sources -Pdownload-spec
```

2. The build will automatically preprocess and regenerate code:
```bash
mvn clean compile
```

### About OpenAPI Preprocessing

This SDK includes an automated preprocessing step that removes JSON Schema 2020-12 meta-properties (`$schema` and `$id`) from the OpenAPI specification. This is necessary because:

- The CardSight AI API includes these properties in component schemas
- OpenAPI Generator cannot parse these meta-properties when embedded in schemas
- The preprocessing happens automatically during the Maven build

**For SDK users:** This is completely transparent - the published SDK works out of the box.

**For contributors:** The `preprocess-openapi.js` script runs automatically during `mvn compile`. No manual steps required.

## Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

## Support

- **Documentation**: [https://docs.cardsight.ai](https://docs.cardsight.ai)
- **API Reference**: [https://api.cardsight.ai/documentation](https://api.cardsight.ai/documentation)
- **Issues**: [GitHub Issues](https://github.com/CardSightAI/cardsightai-sdk-java/issues)
- **Email**: support@cardsight.ai

## License

This SDK is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## About CardSight AI

CardSight AI provides cutting-edge computer vision technology for trading card identification and collection management. Our API serves collectors, marketplace platforms, and trading card businesses worldwide.

Visit [cardsight.ai](https://cardsight.ai) to learn more.
