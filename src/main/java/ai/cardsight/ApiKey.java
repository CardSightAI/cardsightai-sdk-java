package ai.cardsight;

import jakarta.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier annotation for injecting the CardSight AI API key.
 *
 * <p>This annotation can be used with dependency injection frameworks (such as CDI, Spring, or Guice)
 * to distinguish the API key string from other String dependencies.</p>
 *
 * <p>Example usage with dependency injection:</p>
 * <pre>{@code
 * @Inject
 * public MyService(@ApiKey String apiKey) {
 *     this.client = new CardSightAI(apiKey);
 * }
 * }</pre>
 *
 * @since 0.1.0
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER, METHOD})
public @interface ApiKey {
}
