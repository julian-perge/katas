package kata;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

class MessageTest {

  private final User testUser = new User("Julian");
  private final String testMessageDetails = "test message";
  private final Message testMessage = spy(new Message(testUser, testMessageDetails));

  @Test
  void messageReturnsUserWhoPostedMessage() {
    String details = "message details";

    Message message = new Message(testUser, details);

    assertThat(message.getDetails()).isEqualTo(details);
  }

  @Test
  void calculateTimeFromWhenPostedSingleTimeUnit() {
    String expectedValue = "(1 second ago)";

    String actualValue = testMessage.calculateTimeFromWhenPosted();

    assertThat(actualValue).isEqualTo(expectedValue);
  }
}
