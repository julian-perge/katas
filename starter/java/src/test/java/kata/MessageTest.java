package kata;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class MessageTest {

  private final User testUser = new User("Julian");
  private final String testMessageDetails = "test message";
  private final Message testMessage = spy(new Message(testUser, testMessageDetails));

  @Test
  void formatMessageWithoutNameTest() {
    // Bob - Good game though. (1 minute ago)
    String details = "i love shrimp";

    Message message = spy(new Message(testUser, details));
    when(message.calculateTimeFromWhenPosted()).thenReturn("(1 minute ago)");

    String expectedValue = "i love shrimp (1 minute ago)";

    String actualValue = message.formatMessage();

    assertThat(actualValue).isEqualTo(expectedValue);
  }

  @Test
  void formatMessageWithNameTest() {
    // Bob - Good game though. (1 minute ago)
    String details = "i love shrimp";

    Message message = spy(new Message(testUser, details));
    when(message.calculateTimeFromWhenPosted()).thenReturn("(1 minute ago)");

    String expectedValue = "Julian - i love shrimp (1 minute ago)";

    String actualValue = message.formatMessageWithNameOfUser();

    assertThat(actualValue).isEqualTo(expectedValue);
  }

  @Nested
  class AppendIfPluralTests {

    private final String timeUnitSimpleName = "hour";

    @Test
    void doNotMakePluralIfTimeUnitIsLessThanZero() {
      String expectedValue = "hour";

      String actualValue = testMessage.appendIfPlural(-1, timeUnitSimpleName);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void doNotMakePluralIfTimeUnitIsEqualToOne() {
      String expectedValue = "hour";

      String actualValue = testMessage.appendIfPlural(1, timeUnitSimpleName);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void makePluralIfTimeUnitIsEqualToZero() {
      String expectedValue = "hours";

      String actualValue = testMessage.appendIfPlural(0, timeUnitSimpleName);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void makePluralIfTimeUnitIsGreaterThanOne() {
      String expectedValue = "hours";

      String actualValue = testMessage.appendIfPlural(2, timeUnitSimpleName);

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }

  @Nested
  class CalculateTimeFromWhenPostedTests {

    @Test
    void whenDaysIsEqualToOne() {
      String expectedValue = "(1 day ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(1, ChronoUnit.DAYS));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenDaysIsGreaterThanOne() {
      String expectedValue = "(2 days ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(2, ChronoUnit.DAYS));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenHoursIsEqualToOne() {
      String expectedValue = "(1 hour ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(1, ChronoUnit.HOURS));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenHoursIsGreaterThanOne() {
      String expectedValue = "(2 hours ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(2, ChronoUnit.HOURS));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenMinutesIsEqualToOne() {
      String expectedValue = "(1 minute ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(1, ChronoUnit.MINUTES));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenMinutesIsGreaterThanOne() {
      String expectedValue = "(2 minutes ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minus(2, ChronoUnit.MINUTES));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenSecondsIsEqualToOne() {
      String expectedValue = "(1 second ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minusSeconds(1));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void whenSecondsIsGreaterThanOne() {
      String expectedValue = "(2 seconds ago)";

      when(testMessage.getTimestamp()).thenReturn(Instant.now().minusSeconds(2));
      String actualValue = testMessage.calculateTimeFromWhenPosted();

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }
}
