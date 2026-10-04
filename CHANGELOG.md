# Changelog

All notable changes to the CardSight AI Java SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.1.0] - 2026-10-04

Regenerated from the latest CardSight AI OpenAPI specification (now 80 paths / 364 schemas, up from 79 / 364). One new API tag, **CardMagic**, is exposed through a new `cardMagic()` accessor on `CardSightAI`. Everything else is additive: no endpoints, parameters, schemas, properties, or `required` lists were removed or changed.

### Added

- **New API category: CardMagic** — `client.cardMagic()` returns the generated `CardMagicApi`. It turns a phone photo of one or more cards into clean, listing-ready card images.
- **New endpoint:** `POST /v1/cardmagic/process` — `CardMagicApi.processCardImage(File image, String mode, BigDecimal paddingPercent, String paddingFill, String autoLevels, String outputFormat, Integer longEdge, String corners)` returns the processed image as a `File`; `processCardImageWithHttpInfo(...)` takes the same arguments and returns an `ApiResponse<File>` so you can read the response headers. Only `image` is required; pass `null` for any option to use the server default.
  - Options: `mode` (`process` default | `crop`), `paddingPercent` (0–50, default 5), `paddingFill` (`background` or `#RRGGBB`), `autoLevels` (`"true"` | `"false"`, default `"true"`), `outputFormat` (`jpeg` | `png`), `longEdge` (32–2100), `corners` (`"true"` | `"false"`, adds corner close-ups for judging condition).
  - The image is sent as `multipart/form-data` (the generated client does not offer the raw `image/jpeg`, `image/png`, `image/webp`, `image/heic`, or `image/heif` body variants). Maximum upload is 20MB and 8192px per side; send the original photo, not a downscaled or rotated copy.
  - The response is **binary**, not JSON. One card returns `image/jpeg` or `image/png` (per `outputFormat`); two or more cards, or `corners=true`, return `application/zip` containing `card_0.<ext>`, `card_1.<ext>`, ... in reading order. The returned `File` is a temp file with no extension (its `Content-Type` tells you what it is), and deleting it is up to you.
  - Response headers, available from `processCardImageWithHttpInfo(...).getHeaders()` (lookups are case-insensitive): `X-CardMagic-Count` (cards in the photo) and, for a single image only, `X-CardMagic-Width` / `X-CardMagic-Height` (pixels, padding included).
  - When no card is in the photo the API returns `422` with `{"error":"No card found","code":"NO_CARD_FOUND"}`, which surfaces as an `ApiException` with `getCode() == 422` and the JSON in `getResponseBody()`.

  ```java
  import ai.cardsight.generated.client.ApiException;
  import ai.cardsight.generated.client.ApiResponse;
  import java.io.File;
  import java.nio.file.Files;
  import java.nio.file.Path;
  import java.nio.file.StandardCopyOption;
  import java.util.List;
  import java.util.Map;

  try {
      // image, mode, paddingPercent, paddingFill, autoLevels, outputFormat, longEdge, corners
      ApiResponse<File> response = client.cardMagic()
          .processCardImageWithHttpInfo(new File("photo.jpg"), "process", null, null, null, "jpeg", 1200, null);

      Map<String, List<String>> headers = response.getHeaders();   // names are case-insensitive
      String contentType = headers.get("Content-Type").get(0);     // image/jpeg | image/png | application/zip
      int count = Integer.parseInt(headers.get("X-CardMagic-Count").get(0));

      // getData() is a temp file with no extension: pick one from Content-Type, then keep or copy it.
      String ext = contentType.startsWith("application/zip") ? "zip"
          : contentType.startsWith("image/png") ? "png" : "jpg";
      Files.copy(response.getData().toPath(), Path.of("card." + ext), StandardCopyOption.REPLACE_EXISTING);
      response.getData().delete();

      if (count == 1 && headers.containsKey("X-CardMagic-Width")) {   // Width/Height: single image only
          System.out.println(headers.get("X-CardMagic-Width").get(0) + "x" + headers.get("X-CardMagic-Height").get(0));
      }
  } catch (ApiException e) {
      String body = e.getResponseBody();
      if (e.getCode() == 422 && body != null && body.contains("NO_CARD_FOUND")) {
          System.out.println("No card found in the photo");   // not an SDK failure: ask for a clearer photo
      } else {
          throw e;
      }
  }
  ```

- **`IdentifyCardResponse.getDetectedCount()` and `getIdentifiedCount()`** (`Integer`, also on `IdentifyCardResponseInput`). `detectedCount` is the number of cards found in the image whether or not they were identified, and is present on unsuccessful identifications too (so `0` means "no card in the image" and a positive value with no match means "a card was found but not identified"); it is omitted when unavailable. `identifiedCount` is the number of `detections` whose `card` matched the catalog (an exact card or a set-level match).
- **`SlabGradingDetail.getCertNumber()`** (`String`, also on `SlabGradingDetailInput`) — the certification number read from the slab label; absent when it could not be read.
- **`getVariations()`** (`List<String>` of card UUIDs, each carrying this card's UUID in `variationOf`) on `CardSummary`, `CardWithOptionalParallel`, `DetailedCard`, and `DetailedCardResponse` (and their `*Input` counterparts); omitted when the card has no variations.

### Changed

- **Slabbed-card identification behaviour (no schema change).** A card inside a graded slab that could not be identified is now returned as a detection with an empty `card` plus its `grading`. Code that assumed every detection with `getGrading()` has a matched card must check for an exact or set-level match before reading `getCard()` — for example by comparing `getIdentifiedCount()` with `getDetections().size()`.
- The `IdentifyCardResponse.detections` documentation now describes the slabbed-card case above.

## [3.0.0] - 2026-09-11

Regenerated from the latest CardSight AI OpenAPI specification (now 79 paths / 364 schemas, up from 78 / 346). No new API tags — every endpoint is reachable through an existing typed accessor on `CardSightAI`.

### Breaking

- **`CardDetails.parallel` removed, replaced by `parallelSuggestions`.** Card detail responses no longer return a single guessed parallel; they return a ranked array of `ParallelSuggestion` (best match first), each with an optional `confidence` (`High` | `Medium` | `Low` — absent means not assessed, not "Low").
  - Migration: replace `cardDetails.getParallel()` with `cardDetails.getParallelSuggestions().get(0)` (guard for an empty/null list), and read `ParallelSuggestion.getName()` / `getId()` / `getDescription()` / `getIsPartial()` / `getNumberedTo()` / `getCards()` / `getConfidence()` in place of the old single object's fields.
  - `PricingCardContext.getParallel()` is **unaffected** — pricing context still reports a single resolved parallel and keeps its existing shape.

### Added

- **New endpoint:** `GET /v1/pricing/{card_id}/timeseries` — historical pricing candles, exposed via the existing `client.pricing()` accessor as `PricingApi.getCardPricingTimeseries(interval, cardId, periods, asOfDate, listingType, parallelId, gradeId)`. `interval` (`daily` | `weekly` | `monthly`) is required; `periods`, `asOfDate`, `listingType`, `parallelId`, and `gradeId` are optional. Returns a `TimeseriesResponse` backed by new `CandlePeriod`, `CandleStats`, `RawTimeseriesSection`, `TimeseriesCompanyGroup`, `TimeseriesGradeGroup`, `TimeseriesTypeTotals`, and `TimeseriesQueryEcho` models (plus their `*Input` counterparts).
- `CardSuggestion` now carries full card fields (year, manufacturer, releaseName, setName, name, number, description, etc.), populated when card-detection confidence is `Medium` or `Low`.
- `FeedbackResponse.status` gained new values: `new`, `confirmed_bug`, `enhancement_backlog`, `enhancement_planned`, `released`, `not_an_issue`, `closed` (existing values are unchanged; several older values are now documented as deprecated).
- `SearchResult` gained optional `segmentName`, `cardNumber`, and `matchKind` (`exact` | `fuzzy`).
- Card detections may now include `CARD_LANGUAGE` in `card.fields`.
- New documented `409`, `408`, and `503` error responses on several endpoints.

### Changed

- Title search `q` minimum length is now 2 characters (previously allowed shorter queries).
- Parallel catalog endpoints are no longer labelled free-tier.

## [2.1.0] - 2026-07-15

### Added
- **Pricing history paging** — `as_of_date` query param on `GET /v1/pricing/{card_id}` (500-row cap; advisory `messages`).
- **Catalog `/N` slash search** — `SearchResult` gains `numberedTo`.
- **Server advisory messages** — `ServerMessage[]` `messages` arrays on `PaginatedCardsResponse`, `CatalogSearchResponse`, and `PricingResponse`.

### Changed
- Regenerated from the latest OpenAPI spec; parallel catalog endpoints now marked free.
- `BulkPricingRequest.limit` documented maximum reduced from 500 to 100.

## [2.0.0] - 2026-06-30

Regenerated from the latest CardSight AI OpenAPI specification (now 78 paths / 346 schemas, up from 61 / 236).

### Added

- **Five new API categories**, each exposed via a typed accessor on `CardSightAI`:
  - Card Detection — `client.detection()` — locate cards within an image
  - Marketplace — `client.marketplace()` — marketplace listings and sales data
  - Population — `client.population()` — graded card population reports (by card, set, release)
  - Pricing — `client.pricing()` — card pricing, pricing search, and bulk pricing
  - Release Calendar — `client.releaseCalendar()` — upcoming and recent release schedules
- **17 new endpoints**, including catalog search (`/v1/catalog/search`), catalog fields and parallels, segment-scoped identification (`/v1/identify/card/{segment}`), set-identifiability checks, and the marketplace/pricing/population/release-calendar endpoints above.
- **110+ new model classes** covering the new request/response types.

### Changed

- OpenAPI preprocessing now strips `const` / single-value `enum` from boolean schema properties (e.g. the new `isPartial` field). OpenAPI Generator 7.17.0 otherwise renders these as an uncompilable single-value enum.
- The `-Pdownload-spec` profile now bypasses the download cache and runs in the `validate` phase (before preprocessing), so refreshing the spec reliably picks up server-side API changes. Previously it served a stale cached spec.

### Removed

- **BREAKING:** `CollectionAnalyticsResponse.performance` and its supporting models (`CollectionPerformance`, `TopGainerCard`, `TopValueCard`, `MostValuableGroup`, `BestPerformingGroup`, `AIIdentification`). The collection analytics endpoint no longer returns a performance breakdown — code calling `analytics.getPerformance()` must be updated.

[2.0.0]: https://github.com/CardSightAI/cardsightai-sdk-java/releases/tag/v2.0.0

## [1.0.0] - 2025-01-11

### Added

- **Complete API Coverage** - Full support for all CardSight AI API endpoints including:
  - Card identification with AI-powered image recognition
  - Catalog browsing and search across 2M+ trading cards
  - Collection management with analytics and tracking
  - Want list management
  - Grading company integration
  - Natural language search powered by AI
  - Autocomplete for cards, sets, manufacturers, and more

- **Dependency Injection Support** - Built-in Jakarta Inject (JSR-330) annotations for seamless integration with enterprise frameworks:
  - Spring Framework and Spring Boot
  - Jakarta EE / CDI
  - Google Guice
  - Custom `@ApiKey` qualifier annotation for type-safe API key injection
  - `@Inject` annotations on all constructors

- **Type-Safe API Client** - Fully generated from OpenAPI specification with:
  - Strong typing for all request and response models
  - 236 model classes covering all API data structures
  - 79 API operations across 13 API categories
  - Automatic serialization/deserialization with Gson

- **Flexible Configuration** - Multiple ways to configure the SDK:
  - Direct instantiation with API key
  - Environment variable support (`CARDSIGHTAI_API_KEY`)
  - Builder pattern for advanced configuration
  - Custom base URL support
  - Configurable timeouts
  - Custom HTTP headers

- **Robust HTTP Client** - Built on industry-standard libraries:
  - OkHttp 4.12.0 for reliable HTTP communication
  - Automatic retry logic
  - Connection pooling
  - Request/response interceptors

- **Comprehensive Error Handling** - Detailed error information:
  - Typed exceptions with status codes
  - Response body access for debugging
  - Clear error messages

- **File Upload Support** - Multiple input types for card identification:
  - `File` objects
  - Byte arrays
  - Input streams

- **Complete Documentation** - Production-ready documentation including:
  - Quick start guide
  - API category examples
  - Dependency injection configuration for major frameworks
  - Error handling patterns
  - Pagination examples
  - Advanced usage scenarios

### Technical Details

- **Java Version**: Targets Java 21 (LTS) for maximum compatibility
- **Build System**: Maven 3.6+
- **API Specification**: Auto-generated from OpenAPI 3.1 specification
- **HTTP Client**: OkHttp 4.12.0 with Gson for JSON serialization
- **Dependencies**:
  - Jakarta Annotations API 3.0.0
  - Jakarta Validation API 3.1.0
  - Jakarta Inject API 2.0.1
  - SLF4J 2.0.16 for logging

### Architecture

- **Zero-Maintenance Design** - Direct exposure of generated API clients eliminates the need for manual wrapper updates when the API evolves
- **Automated Build Pipeline** - OpenAPI specification preprocessing and code generation fully automated in Maven build
- **Clean Separation** - Generated code isolated in `ai.cardsight.generated` package, hand-written code in `ai.cardsight`

[1.0.0]: https://github.com/CardSightAI/cardsightai-sdk-java/releases/tag/v1.0.0
