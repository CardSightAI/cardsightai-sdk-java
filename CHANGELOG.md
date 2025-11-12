# Changelog

All notable changes to the CardSight AI Java SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
