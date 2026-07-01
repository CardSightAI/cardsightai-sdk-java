# CardSight AI Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/ai.cardsight/cardsightai-sdk-java)](https://central.sonatype.com/artifact/ai.cardsight/cardsightai-sdk-java)
![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Official Java SDK for [CardSight AI](https://cardsight.ai) REST API**

The most comprehensive trading card identification and collection management platform.
**12M+ Cards** • **AI-Powered Recognition** • **Free Tier Available**

**Quick Links:** [Why CardSight AI?](#why-cardsight-ai) • [Getting Started](#getting-started) • [Installation](#installation) • [Examples](#usage-examples) • [API Documentation](https://api.cardsight.ai/documentation) • [Support](#support)

---

## Why CardSight AI?

Building a trading card app means solving two hard problems before you can ship a single feature: **recognizing cards from a photo** and **maintaining an accurate catalog of every card ever printed**. CardSight AI does both, so you can build your product instead of a computer-vision pipeline and a data-entry team.

- **One photo, full identification.** A single AI scan identifies multiple cards at once — including the exact parallel/variant, the print run for numbered cards, and the grade on a slabbed card. Point a camera at a card and get structured data back.
- **A catalog that spans the whole hobby.** 12M+ cards across sports (baseball, football, basketball, and more) *and* trading card games (Pokémon, Magic: The Gathering, Yu-Gi-Oh!, and beyond) — one API instead of stitching together a different data source per category.
- **Real market data, built in.** Completed-sale pricing, active marketplace listings, and graded population reports power "what's it worth", portfolio tracking, and deal-finding features without integrating a second vendor.
- **Collection management out of the box.** Collections, binders, want lists, and analytics are first-class endpoints — skip building your own card database and CRUD layer.
- **One integration works across every game.** Flexible metadata (HP, Rarity, Artist, Mana Cost, …) is exposed uniformly through the Fields system, so supporting a new game doesn't mean new code.
- **Free to start, fast to integrate.** A free tier, a strongly typed SDK, and auto-generation from our OpenAPI spec mean you're making real calls in minutes — and staying in sync automatically as the API grows.

**Perfect for building:** card scanner & identification apps, collection trackers, trading-card marketplaces, price guides and valuation dashboards, breaking / pack-opening tools, and grading & population lookups.

---

## Features

- **Auto-Generated from OpenAPI** - Always up-to-date with the latest API; every endpoint is available with zero manual wrapper code
- **Full Type Safety** - Strongly typed request parameters and response models for every endpoint
- **Multi-Card Detection** - Identify multiple cards in a single image with per-detection confidence levels
- **Flexible Metadata via Fields** - Search and surface arbitrary card properties (HP, Rarity, Artist, Mana Cost, etc.) across any trading card game
- **Dependency Injection Ready** - Built-in Jakarta Inject (JSR-330) annotations for Spring, CDI, and Guice
- **Robust HTTP Stack** - Built on OkHttp with Gson serialization, connection pooling, and configurable timeouts
- **Full API Coverage** - All CardSight AI endpoints, grouped into typed API categories

## Key Capabilities

| Feature | Description | Primary Methods |
|---------|-------------|-----------------|
| **Card Identification** | Identify multiple cards from images using AI; free pre-flight set identifiability lookups | `cardIdentification().identifyCard()`, `identifyCardBySegment()`, `listIdentifiableSets()`, `checkSetIdentifiable()` |
| **Card Detection** | Check if trading cards are present in an image | `detection().detectCard()` |
| **Catalog Search** | Fuzzy search across cards, sets, releases, parallels | `catalog().searchCatalog()`, `catalog().getCards()` |
| **Random Catalog** | Pack-opening simulations with parallel odds | `catalog().getRandomCards()`, `catalog().getRandomSets()` |
| **Collections** | Manage owned card collections with analytics | `collections().createCollection()`, `addCollectionCards()` |
| **Collectors** | Manage collector profiles | `collectors().createCollector()`, `updateCollector()` |
| **Lists** | Track wanted cards (wishlists) | `lists().createList()`, `addCardsToList()` |
| **Binders** | Organize collection subsets | `collections().createBinder()` |
| **Pricing** | Completed sales data for cards; free-text title search | `pricing().getCardPricing()`, `getBulkPricing()`, `searchPricingByTitle()` |
| **Marketplace** | Active marketplace listings for cards; free-text title search | `marketplace().getCardMarketplace()`, `searchMarketplaceByTitle()` |
| **Population Reports** | Graded population counts by card, set, or release | `population().getCardPopulation()`, `getSetPopulation()`, `getReleasePopulation()` |
| **Grading** | PSA, TAG, BGS, SGC grade information | `grading().getGradingCompanies()` |
| **AI Search** | Natural language queries | `ai().processAIQuery()` |
| **Autocomplete** | Search suggestions for all entities | `autocomplete().autocompleteCards()` |
| **Field Catalog** | Browse flexible metadata fields (Artist, HP, Rarity, etc.) | `catalog().getFields()`, `getFieldById()` |
| **Release Calendar** | Upcoming and recent card product releases | `releaseCalendar().getReleaseCalendar()` |

## Requirements

- Java 21 or higher (LTS)
- Maven 3.6+ or Gradle
- API Key from [cardsight.ai](https://cardsight.ai) (free tier available)

## Installation

### Maven

```xml
<dependency>
    <groupId>ai.cardsight</groupId>
    <artifactId>cardsightai-sdk-java</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'ai.cardsight:cardsightai-sdk-java:2.0.0'
```

## Getting Started

### Get Your Free API Key

Get started in minutes with a **free API key** from [cardsight.ai](https://cardsight.ai) - no credit card required!

### Quick Start (< 5 minutes)

```java
import ai.cardsight.CardSightAI;
import ai.cardsight.generated.model.IdentifyCardResponse;
import java.io.File;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        // 1. Initialize the client (or use the CARDSIGHTAI_API_KEY env var)
        CardSightAI client = new CardSightAI("your_api_key_here");

        // 2. Identify a card from an image
        IdentifyCardResponse result = client.cardIdentification()
            .identifyCard(new File("card.jpg"));

        // 3. Access the identification results
        if (Boolean.TRUE.equals(result.getSuccess()) && !result.getDetections().isEmpty()) {
            var detection = result.getDetections().get(0);   // best match first
            var card = detection.getCard();
            System.out.println("Card: " + card.getName());          // exact match only
            System.out.println("Set: " + card.getReleaseName());     // exact + set-level match
            System.out.println("Confidence: " + detection.getConfidence()); // High / Medium / Low
            System.out.println("Total cards detected: " + result.getDetections().size());
        }
    }
}
```

That's it! The SDK handles all API communication, serialization, and type mapping automatically. Every API method throws a checked `ApiException` on a non-2xx response (see [Error Handling](#error-handling)).

## Usage Examples

Each API category is reached through a typed accessor on the client (`client.catalog()`, `client.pricing()`, etc.). Each accessor returns the generated API class for that category.

### Card Identification

The identification endpoint uses AI to detect cards in images. It can identify multiple cards in a single image and returns a confidence level for each detection. Use `identifyCard()` for the default segment (baseball) or `identifyCardBySegment()` to target a specific sport/game.

```java
import ai.cardsight.CardSightAI;
import ai.cardsight.generated.model.IdentificationData;
import ai.cardsight.generated.model.IdentifyCardResponse;
import java.io.File;

CardSightAI client = new CardSightAI("your_api_key");

IdentifyCardResponse result = client.cardIdentification().identifyCard(new File("card.jpg"));

if (Boolean.TRUE.equals(result.getSuccess())) {
    if (result.getDetections().isEmpty()) {
        System.out.println("No cards detected in image");
        return;
    }

    System.out.println("Detected " + result.getDetections().size() + " card(s)");

    for (IdentificationData detection : result.getDetections()) {
        System.out.println("\nConfidence: " + detection.getConfidence());
        var card = detection.getCard();

        if (card != null && card.getId() != null) {
            // Exact match — all fields populated
            System.out.println("  Name: " + card.getName());
            System.out.println("  Year: " + card.getYear());
            System.out.println("  Manufacturer: " + card.getManufacturer());
            System.out.println("  Set: " + card.getCardSetName());
            System.out.println("  Number: " + card.getNumber());
            System.out.println("  Card ID: " + card.getId());
        } else if (card != null && card.getCardSetId() != null) {
            // Set-level match — no specific card, but set info available
            System.out.println("  Release: " + card.getReleaseName());
            System.out.println("  Set: " + card.getCardSetName());
            System.out.println("  Year: " + card.getYear());
        } else {
            // No match — a card was detected but not identified
            System.out.println("  Could not identify this card");
        }
    }

    // Request metadata
    System.out.println("\nRequest ID: " + result.getRequestId());
    System.out.println("Processing time: " + result.getProcessingTime() + "ms");

    // Server advisory messages (e.g. image-quality warnings)
    if (result.getMessages() != null) {
        result.getMessages().forEach(m ->
            System.out.println("[" + m.getType() + "] " + m.getMessage()));
    }
}

// Segment-specific identification (football, basketball, magic, etc.)
IdentifyCardResponse football = client.cardIdentification()
    .identifyCardBySegment("football", new File("card.jpg"));
```

> **Note:** `identifyCard`/`identifyCardBySegment` accept a `java.io.File`. If your image is in memory, write it to a temporary file first (e.g. `Files.write(...)`).

#### Response Structure

Each detection has a `confidence` level and a `card` object. The `card` fields are populated based on the match level:

- **Exact match**: `card.getId()` is non-null — all fields populated, including `getNumber()` and optionally `getParallel()`
- **Set-level match**: `card.getCardSetId()` is non-null but `getId()` is null — release/set info available but no specific card
- **No match**: the card's fields are all null — a card was detected in the image but couldn't be identified

Detections may also include grading information via `detection.getGrading()` when the card is inside a graded slab (see [Grading / Slab Detection](#grading--slab-detection)).

#### Working with Detections

The response models are plain Java objects, so use standard streams to filter and group detections:

```java
import ai.cardsight.generated.model.IdentificationData;

// Highest-confidence (best) match
var best = result.getDetections().stream().findFirst().orElse(null);

// Only high-confidence detections
var highConfidence = result.getDetections().stream()
    .filter(d -> d.getConfidence() == IdentificationData.ConfidenceEnum.HIGH)
    .toList();

// Only exact matches (detections with a card ID)
var exactMatches = result.getDetections().stream()
    .filter(d -> d.getCard() != null && d.getCard().getId() != null)
    .toList();
```

#### Checking Set Identifiability (free pre-flight)

Before spending a billed identify call, you can confirm whether a set is supported. These endpoints are **free** — they do not count toward your billed API usage.

```java
// List every set the system can identify (paginated)
var sets = client.cardIdentification().listIdentifiableSets(20, 0); // take, skip
System.out.println(sets.getTotalCount() + " identifiable sets");

// Check whether a specific set is identifiable by its set ID
var check = client.cardIdentification().checkSetIdentifiable("set_uuid");
if (Boolean.TRUE.equals(check.getIsIdentifiable())) {
    System.out.println("Set is identifiable");
}
```

#### Flexible Metadata, Suggestions, and Numbered Cards

Every detected `card` can also carry:

- `getNumberedTo()` — print run for numbered base cards (e.g. `25` for a `/25`), independent of parallels
- `getFields()` — a `List<FieldValue>` of key/value metadata tailored to the TCG (e.g. `HP`, `RARITY`, `ARTIST`, `MANA_COST`)
- `getSuggestions()` — alternative card candidates when multiple reprints scored similarly

```java
var detection = result.getDetections().get(0);
var card = detection.getCard();

// Iterate flexible metadata
if (card.getFields() != null) {
    card.getFields().forEach(f -> System.out.println(f.getKey() + ": " + f.getValue()));
    // e.g. "HP: 120", "RARITY: Holo Rare", "ARTIST: Mitsuhiro Arita"
}

// Base-card print run (distinct from parallel print runs)
if (card.getNumberedTo() != null) {
    System.out.println("Limited to /" + card.getNumberedTo());
}

// Alternative matches when reprints score similarly
if (card.getSuggestions() != null) {
    card.getSuggestions().forEach(alt ->
        System.out.println("Could also be: " + alt.getCardSetName()));
}
```

#### Parallel Variant Detection

The identify endpoint can detect parallel variants (Refractors, Prizms, numbered parallels, etc.). When a parallel is detected, `card.getParallel()` returns a `ParallelSummary`:

```java
var card = detection.getCard();
if (card.getParallel() != null) {
    var parallel = card.getParallel();
    System.out.println("Parallel: " + parallel.getName());          // e.g. "Gold Refractor"
    System.out.println("Parallel ID: " + parallel.getId());

    if (parallel.getDescription() != null) {
        System.out.println("Description: " + parallel.getDescription());
    }

    // Numbered parallel (limited print run)
    if (parallel.getNumberedTo() != null) {
        System.out.println("NUMBERED: only " + parallel.getNumberedTo() + " exist!");
    }
} else {
    System.out.println("Base card");
}
```

#### Grading / Slab Detection

When identifying a card inside a graded slab, `detection.getGrading()` returns a `SlabGradingDetail`:

```java
var grading = detection.getGrading();
if (grading != null) {
    System.out.println("Slab detection confidence: " + grading.getConfidence());

    // Structured slab data (each may be null depending on what was read from the label):
    //   grading.getCompany()   -> SlabCompany   (e.g. PSA, BGS, SGC)
    //   grading.getGrade()     -> SlabGrade     (grade value + condition)
    //   grading.getQualifier() -> SlabQualifier (defect qualifier, e.g. OC, MC, PD, ST)
    //   grading.getAutoGrade() -> SlabAutoGrade (autograph grade)
}
```

### Card Detection (Presence Check)

The detection endpoint is a lightweight alternative to full identification — it checks whether trading cards are present in an image without identifying them. This is faster and cheaper when you only need to know if cards exist in the image.

```java
import ai.cardsight.generated.model.DetectCardResponse;
import java.io.File;

DetectCardResponse result = client.detection().detectCard(new File("image.jpg"));

System.out.println("Cards detected: " + result.getDetected()); // true / false
System.out.println("Number of cards: " + result.getCount());   // 0, 1, 2, ...

if (result.getMessages() != null) {
    result.getMessages().forEach(m ->
        System.out.println("[" + m.getType() + "] " + m.getMessage()));
}
```

### Catalog Search

Search across cards, sets, releases, and parallels with a single query:

```java
import ai.cardsight.generated.model.CatalogSearchResponse;

// Global fuzzy search:
//   q, take, skip, type, segment, manufacturer, year, minYear, maxYear, field
CatalogSearchResponse results = client.catalog()
    .searchCatalog("Ken Griffey Jr", 10, 0, null, null, null, null, null, null, null);

// Filter by entity type ('card' | 'set' | 'release' | 'parallel') and year
var setResults = client.catalog()
    .searchCatalog("Topps Chrome", 10, 0, "set", null, null, "2023", null, null, null);

System.out.println("Found " + results.getTotalCount() + " results");
results.getResults().forEach(r -> {
    System.out.println("[" + r.getType() + "] " + r.getName() + " (relevance: " + r.getRelevance() + ")");
    if (r.getCardSetName() != null) System.out.println("  Set: " + r.getCardSetName());
    if (r.getYear() != null) System.out.println("  Year: " + r.getYear());
});
```

### Fields (Flexible Metadata System)

Every trading card game has different metadata: Pokémon cards have HP and Rarity, Magic: The Gathering cards have Mana Cost and Artist, Yu-Gi-Oh! cards have Attribute and Level. Rather than hard-coding columns per game, CardSight exposes a flexible **Fields** system — any card, set, release, or segment can carry key/value metadata, and the catalog exposes it as a first-class browsable entity.

**Browse available fields:**

```java
// take, skip, name, key, sort, order
var fields = client.catalog().getFields(20, 0, null, null, "usageCount", "desc");
fields.getFields().forEach(f ->
    System.out.println(f.getName() + " (" + f.getKey() + ")"));
```

**Surface metadata directly from an identification result** — every detected card includes a `getFields()` array:

```java
import ai.cardsight.generated.model.IdentifyCardResponse;

IdentifyCardResponse result = client.cardIdentification()
    .identifyCardBySegment("magic", new File("mtg-card.jpg"));

var card = result.getDetections().get(0).getCard();
if (card.getFields() != null) {
    for (var field : card.getFields()) {
        if ("ARTIST".equalsIgnoreCase(field.getKey())) {
            System.out.println(card.getName() + " by " + field.getValue());
        }
    }
}
```

### Pricing (Completed Sales)

Get completed sales pricing for a card, grouped into raw (ungraded) and graded sections:

```java
import ai.cardsight.generated.model.PricingResponse;

// cardId, parallelId, gradeId, period, listingType, limit
// period: "7d", "2w", "3m", "1y", "all"   listingType: "auction", "fixed", "both"
PricingResponse pricing = client.pricing()
    .getCardPricing("card_uuid", null, null, "90d", "both", 50);

// Card context
System.out.println("Card: " + pricing.getCard().getName());
System.out.println("Set: " + pricing.getCard().getSet().getName()
    + " (" + pricing.getCard().getSet().getYear() + ")");

// Raw (ungraded) sales
System.out.println("Ungraded sales: " + pricing.getRaw().getCount());
pricing.getRaw().getRecords().forEach(sale ->
    System.out.println("  $" + sale.getPrice() + " - " + sale.getDate() + " (" + sale.getSource() + ")"));

// Graded sales (grouped by company -> grade)
pricing.getGraded().forEach(company -> {
    System.out.println(company.getCompanyName() + ":");
    company.getGrades().forEach(grade ->
        System.out.println("  Grade " + grade.getGradeValue() + ": " + grade.getCount() + " sales"));
});

// Metadata
System.out.println("Total records: " + pricing.getMeta().getTotalRecords());
System.out.println("Last sale: " + pricing.getMeta().getLastSaleDate());
```

**Bulk pricing** for multiple cards (up to 100):

```java
import ai.cardsight.generated.model.BulkPricingRequestInput;
import java.util.List;
import java.util.UUID;

BulkPricingRequestInput request = new BulkPricingRequestInput();
request.setCardIds(List.of(UUID.fromString("card_uuid_1"), UUID.fromString("card_uuid_2")));
request.setPeriod("90d");

var bulk = client.pricing().getBulkPricing(request);
System.out.println("Successful: " + bulk.getMeta().getSuccessful());
```

### Pricing Search (Free-Text Title)

Search completed sales by listing title when you don't have a card ID. Returns a flat, relevance-ranked list that spans multiple cards:

```java
// q (3-300 chars), period, listingType, limit
var results = client.pricing().searchPricingByTitle("Ken Griffey Jr 1989 Upper Deck", "90d", "both", 25);

System.out.println("Found " + results.getMeta().getTotalRecords() + " sales");
results.getResults().forEach(sale ->
    System.out.println("$" + sale.getPrice() + " - " + sale.getTitle() + " (" + sale.getSource() + ")"));
```

### Marketplace (Active Listings)

Get currently active marketplace listings for a card. Same shape as pricing, but with active listings instead of completed sales:

```java
import ai.cardsight.generated.model.MarketplaceResponse;

// cardId, parallelId, gradeId, listingType, limit
MarketplaceResponse listings = client.marketplace()
    .getCardMarketplace("card_uuid", null, null, "both", 25);

System.out.println("Ungraded listings: " + listings.getRaw().getCount());
listings.getRaw().getRecords().forEach(listing -> {
    System.out.println("  " + listing.getTitle() + " - $" + listing.getPrice() + " (" + listing.getSource() + ")");
    if (listing.getUrl() != null) System.out.println("    " + listing.getUrl());
});
```

### Marketplace Search (Free-Text Title)

```java
// q (3-300 chars), listingType, limit
var results = client.marketplace().searchMarketplaceByTitle("Ken Griffey Jr 1989 Upper Deck", "both", 25);
System.out.println("Found " + results.getMeta().getTotalRecords() + " active listings");
```

### Catalog Operations

Retrieve cards, sets, releases, and other catalog data:

```java
// Search for cards:
//   take, skip, name, number, releaseId, releaseName, year, minYear, maxYear,
//   cardSetId, cardSetName, manufacturer, attributeId, attributeShortName, field, sort, order
var cards = client.catalog().getCards(
    20, 0, "Charizard", null, null, null, "2024",
    null, null, null, null, null, null, null, null, "name", "asc");
cards.getCards().forEach(c -> System.out.println(c.getName() + " #" + c.getNumber()));

// Get a specific card by ID
var card = client.catalog().getCard("card_uuid");

// Sets: take, skip, releaseId, name, year, minYear, maxYear, manufacturer, isIdentifiable, sort, order
var sets = client.catalog().getSets(20, 0, null, null, "2023", null, null, "Topps", null, "year", "desc");

// Cards in a specific set: id, take, skip, name, number, sort, order
var setCards = client.catalog().getSetCards("set_uuid", 50, 0, null, null, "number", "asc");

// Releases (product lines like "Chrome", "Series 1")
var releases = client.catalog().getReleases(20, 0, null, null, null, "2020", "2024", "Chrome", null, "year", "desc");

// Manufacturers and segments
var manufacturers = client.catalog().getManufacturers(50, 0, null, "name", "asc");
var segments = client.catalog().getSegments(50, 0, null, null, "name", "asc");

// Parallels
var parallels = client.catalog().getParallels(20, 0, null, null, null, null, null, null, "name", "asc");
var parallel = client.catalog().getParallel("parallel_uuid");

// Catalog statistics (each stat group exposes getTotal(), plus breakdowns)
var stats = client.catalog().getStatistics();
System.out.println("Total cards: " + stats.getCards().getTotal());
System.out.println("Total sets: " + stats.getSets().getTotal());
```

### Release Calendar

Browse upcoming and recent card product releases, sorted by release date (newest first):

```java
// take, skip, segment, manufacturer, year  (segment/manufacturer accept UUIDs or names)
var calendar = client.releaseCalendar().getReleaseCalendar(20, 0, null, "Panini", "2026");

calendar.getReleaseCalendar().forEach(entry -> {
    String preOrder = entry.getPreOrderDate() != null ? entry.getPreOrderDate() : "N/A";
    System.out.println(entry.getName() + " — releases " + entry.getReleaseDate()
        + " (pre-order: " + preOrder + ")");
});
System.out.println("Total upcoming: " + calendar.getTotalCount());
```

### Random Catalog (Pack Opening & Discovery)

The random endpoints enable pack-opening simulations and discovery features by returning random results instead of sorted, paginated ones:

```java
// Pack opening — 10 random cards from a set, with parallel conversion odds.
// Params: name, number, releaseId, releaseName, year, minYear, maxYear, cardSetId,
//         cardSetName, manufacturer, attributeId, attributeShortName, field, sort,
//         order, count, includeParallels
var pack = client.catalog().getRandomCards(
    null, null, null, null, null, null, null, "set_uuid",
    null, null, null, null, null, null, null, 10, true);

// Discovery — 5 random releases from 2023
var randomReleases = client.catalog().getRandomReleases(
    null, null, "2023", null, null, null, null, null, null, 5);

// Random sets from a specific release
var randomSets = client.catalog().getRandomSets(
    "release_uuid", null, null, null, null, null, null, null, null, 6);
```

### Population Reports

Get graded population counts for a card, set, or release:

```java
import ai.cardsight.generated.model.CardPopulationResponse;

// cardId, gradingCompanyId (gradingCompanyId optional — pass null for all companies)
CardPopulationResponse pop = client.population().getCardPopulation("card_uuid", null);
System.out.println(pop.getCardName() + " — total graded population: " + pop.getTotalPopulation());

// Also available:
client.population().getSetPopulation("set_uuid", null);
client.population().getReleasePopulation("release_uuid", null);
```

### Collection Management

Manage personal card collections with analytics:

```java
import ai.cardsight.generated.model.CreateCollectionInput;
import java.util.UUID;

// Create a collection (collectorId links it to a collector profile)
CreateCollectionInput input = new CreateCollectionInput();
input.setName("My Vintage Cards");
input.setDescription("Pre-1980 baseball cards");
input.setCollectorId(UUID.fromString("collector_uuid"));
var collection = client.collections().createCollection(input);
UUID collectionId = UUID.fromString(collection.getId());

// Collection analytics
var analytics = client.collections().getCollectionAnalytics(collectionId);
System.out.println("Total cards: " + analytics.getOverview().getTotalCards());
System.out.println("Unique cards: " + analytics.getOverview().getUniqueCards());
System.out.println("Total invested: " + analytics.getFinancials().getTotalInvested());

// Breakdown by category: groupBy ("year" | "manufacturer" | "set" | "grade"), collectionId, take, skip, sortBy, order, minCount
var breakdown = client.collections().getCollectionBreakdown("year", collectionId, 20, 0, null, "desc", null);

// List collections for a collector: take, skip, collectorId, name, sort, order
var collections = client.collections().getCollections(10, 0, UUID.fromString("collector_uuid"), null, "name", "asc");
```

Adding and updating cards is done with `addCollectionCards(collectionId, CreateCollectionCardInput)` and `updateCollectionCard(collectionId, cardId, UpdateCollectionCardInput)` (which supports `setBuyPrice`, `setBuyDate`, `setGradeId`, `setSellPrice`, `setSoldDate`, and more).

### Binders (Collection Organization)

Organize collection cards into binders (subsets):

```java
import ai.cardsight.generated.model.CreateBinderInput;
import java.util.UUID;

UUID collectionId = UUID.fromString("collection_uuid");

CreateBinderInput binderInput = new CreateBinderInput();
binderInput.setName("Hall of Famers");
binderInput.setDescription("Cards of HOF players");
var binder = client.collections().createBinder(collectionId, binderInput);

// List cards in a binder: collectionId, binderId, take, skip
var binderCards = client.collections().getBinderCards(collectionId, binder.getId(), 20, 0);
```

### Lists (Want Lists / Wishlists)

Track cards you want to acquire:

```java
import ai.cardsight.generated.model.CreateListInput;

CreateListInput listInput = new CreateListInput();
listInput.setName("Rookies to Find");
listInput.setDescription("2024 rookie cards I need");
listInput.setCollectorId("collector_uuid");
var list = client.lists().createList(listInput);

// Get cards in a list: listId, take, skip
var listCards = client.lists().getListCards(list.getId(), 20, 0);

// Remove a card once acquired
client.lists().removeCardFromList(list.getId(), "card_uuid");
```

### Grading Information

Access grading company data and grade values:

```java
import java.util.UUID;

// All grading companies (PSA, BGS, SGC, etc.)
var companies = client.grading().getGradingCompanies();
companies.getCompanies().forEach(c -> System.out.println(c.getName()));

// Grading types for a company, then grades for a type
var types = client.grading().getGradingTypes(UUID.fromString("company_uuid"));
var grades = client.grading().getGrades(UUID.fromString("company_uuid"), UUID.fromString("type_uuid"));
```

### AI-Powered Search

Use natural language to query the catalog:

```java
import ai.cardsight.generated.model.AIQueryRequestInput;

AIQueryRequestInput query = new AIQueryRequestInput();
query.setQuery("Show me Mike Trout rookie cards worth over $100");

var response = client.ai().processAIQuery(query);
System.out.println(response.getAnswer());
```

### Autocomplete

Provide search suggestions for users. Autocomplete methods return a `List<String>` of suggestions:

```java
// q, cardSetId, releaseId, segmentId, manufacturerId, year
var cards = client.autocomplete().autocompleteCards("aaron", null, null, null, null, null);
cards.getSuggestions().forEach(System.out::println); // "Aaron Judge", "Hank Aaron", ...

// Other entities
var sets = client.autocomplete().autocompleteSets("chrome", null, null, null, null);
var manufacturers = client.autocomplete().autocompleteManufacturers("top", null);
var years = client.autocomplete().autocompleteYears("202", null, null);
```

### Image Retrieval

Card images are returned as a `java.io.File`:

```java
import java.io.File;
import java.util.UUID;

// Get a card image: id, format ("json" for base64 JSON), _default ("true" for placeholder fallback)
File image = client.images().getCardImage(UUID.fromString("card_uuid"), null, null);

// Collection card images and thumbnails
File collectionImage = client.collectionImages()
    .getCollectionCardImage(UUID.fromString("collection_uuid"), UUID.fromString("card_uuid"));
File thumbnail = client.collectionImages()
    .getCollectionCardImageThumbnail(UUID.fromString("collection_uuid"), UUID.fromString("card_uuid"));
```

### Feedback

Submit feedback to improve the platform:

```java
import ai.cardsight.generated.model.FeedbackInputInput;
import java.util.UUID;

FeedbackInputInput feedback = new FeedbackInputInput();
// ... populate feedback fields ...

// Report an identification issue (id = identification request ID)
client.feedback().submitIdentifyFeedback(UUID.fromString("identification_request_id"), feedback);

// General feedback, or feedback on a specific catalog entity
client.feedback().submitGeneralFeedback(feedback);
client.feedback().submitCardFeedback(UUID.fromString("card_uuid"), feedback);
```

## Type Safety

Every request parameter and response is a strongly typed, auto-generated Java class in the `ai.cardsight.generated.model` package, so your IDE gives you full autocomplete and the compiler catches mistakes:

```java
import ai.cardsight.generated.model.IdentifyCardResponse;
import ai.cardsight.generated.model.IdentificationData;
import ai.cardsight.generated.model.CardDetails;
import ai.cardsight.generated.model.CatalogSearchResponse;
import ai.cardsight.generated.model.PricingResponse;

IdentifyCardResponse result = client.cardIdentification().identifyCard(new File("card.jpg"));
// The compiler knows the exact shape of every response:
for (IdentificationData detection : result.getDetections()) {
    CardDetails card = detection.getCard();
    // ...
}
```

Enumerated values are generated as Java enums (for example `IdentificationData.ConfidenceEnum.HIGH`, `MEDIUM`, `LOW`).

## Error Handling

Every API method throws `ai.cardsight.generated.client.ApiException` (a checked exception) on a non-2xx response. Inspect `getCode()` for the HTTP status and `getResponseBody()` for the raw error payload:

```java
import ai.cardsight.generated.client.ApiException;

try {
    var result = client.cardIdentification().identifyCard(new File("card.jpg"));
} catch (ApiException e) {
    System.err.println("API Error " + e.getCode() + ": " + e.getMessage());
    System.err.println("Response body: " + e.getResponseBody());

    switch (e.getCode()) {
        case 401 -> System.err.println("Authentication failed - check your API key");
        case 429 -> System.err.println("Rate limited - wait before retrying");
        case 500, 502, 503, 504 -> System.err.println("Server error - retry later");
        default -> { /* handle other status codes */ }
    }
}
```

## Configuration

```java
import ai.cardsight.CardSightAI;
import java.time.Duration;

// Basic — API key passed directly
CardSightAI client = new CardSightAI("your_api_key");

// From the CARDSIGHTAI_API_KEY environment variable
CardSightAI fromEnv = new CardSightAI();

// Advanced — builder with custom base URL, timeout, and headers
CardSightAI custom = CardSightAI.builder()
    .apiKey("your_api_key")
    .baseUrl("https://api.cardsight.ai")
    .timeout(Duration.ofSeconds(30))
    .addHeader("X-Custom-Header", "value")
    .build();
```

### Dependency Injection

The client's primary constructor is annotated with Jakarta `@Inject`, so it works with Spring, CDI, and Guice. Provide a `CardSightConfig` and inject `CardSightAI`:

```java
import ai.cardsight.CardSightAI;
import ai.cardsight.config.CardSightConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CardSightConfiguration {

    @Bean
    CardSightConfig cardSightConfig(@Value("${cardsight.api-key}") String apiKey) {
        return CardSightConfig.builder().apiKey(apiKey).build();
    }

    @Bean
    CardSightAI cardSightAI(CardSightConfig config) {
        return new CardSightAI(config);
    }
}
```

## Environment Variables

```bash
CARDSIGHTAI_API_KEY=your_api_key_here  # API key for authentication
```

## API Endpoint Coverage

The SDK provides full coverage of the CardSight AI REST API, grouped into typed accessors on the client:

| Category | Methods | Accessor |
|----------|---------|----------|
| **Health** | 2 | `health().getHealth()`, `getHealthAuthenticated()` |
| **Identification** | 4 | `cardIdentification().identifyCard()`, `identifyCardBySegment()`, `listIdentifiableSets()`, `checkSetIdentifiable()` |
| **Detection** | 1 | `detection().detectCard()` |
| **Catalog** | 21 | `catalog().searchCatalog()`, `getCards()`, `getSets()`, `getReleases()`, `getFields()`, `getParallels()`, `getRandomCards()`, `getStatistics()`, … |
| **Release Calendar** | 1 | `releaseCalendar().getReleaseCalendar()` |
| **Collections** | 23 | `collections().*` (collections, cards, binders, analytics, breakdown, set progress) |
| **Collectors** | 5 | `collectors().*` |
| **Lists** | 8 | `lists().*` |
| **Pricing** | 3 | `pricing().getCardPricing()`, `getBulkPricing()`, `searchPricingByTitle()` |
| **Marketplace** | 2 | `marketplace().getCardMarketplace()`, `searchMarketplaceByTitle()` |
| **Population** | 3 | `population().getCardPopulation()`, `getSetPopulation()`, `getReleasePopulation()` |
| **Grades** | 3 | `grading().getGradingCompanies()`, `getGradingTypes()`, `getGrades()` |
| **Autocomplete** | 6 | `autocomplete().*` |
| **AI** | 1 | `ai().processAIQuery()` |
| **Images** | 4 | `images().getCardImage()`, `collectionImages().*` |
| **Feedback** | 8 | `feedback().*` |
| **Subscription** | 1 | `subscription().getSubscription()` |

## Building from Source

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Node.js (used to preprocess the OpenAPI spec during the build)

### Build

```bash
# Clone the repository
git clone https://github.com/CardSightAI/cardsightai-sdk-java.git
cd cardsightai-sdk-java

# Build and install to your local Maven repository
mvn clean install
```

The build automatically preprocesses the bundled OpenAPI specification, generates the client, and compiles everything.

### Updating to the Latest API

The SDK is generated from the CardSight AI OpenAPI specification. To pull the latest spec and regenerate:

```bash
mvn clean compile -Pdownload-spec
```

### Testing

```bash
mvn test
```

## Contributing

Contributions are welcome! Please open an issue or pull request on GitHub.

## License

This SDK is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Support

- **Email**: support@cardsight.ai
- **Website**: [cardsight.ai](https://cardsight.ai)
- **API Documentation**: [api.cardsight.ai/documentation](https://api.cardsight.ai/documentation)
- **Issues**: [GitHub Issues](https://github.com/CardSightAI/cardsightai-sdk-java/issues)

---

*Built with ❤️ by CardSight AI*
