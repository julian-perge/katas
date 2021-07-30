package kata;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserTest {

  private final User testUser = spy(new User("Julian"));

  @Nested
  class PublishingMessagesTests {

    @Test
    void publishMessageAddsToTimeline() {
      String details = "test message";

      Message message = new Message(testUser, details);

      testUser.publishMessage(details);

      assertThat(testUser.getTimeline()).hasSize(1);
      assertThat(testUser.getTimeline().getFirst().getDetails()).isEqualTo("new message");
    }

    @Test
    void publishMultipleMessageAddsToTimelineInTimePostedOrder() throws InterruptedException {
      String details = "test message";

      testUser.publishMessage(details);
      TimeUnit.SECONDS.sleep(1);

      testUser.publishMessage("new message");

      assertThat(testUser.getTimeline()).hasSize(2);
      assertThat(testUser.getTimeline().getFirst().getDetails()).isEqualTo("new message");
      assertThat(testUser.getTimeline().getLast().getDetails()).isEqualTo("test message");
    }
  }

  @Nested
  class FollowingTest {

    @Test
    void userCanAddAnotherUserForFollowing() {
      User anotherUser = new User("Michael");

      boolean didAddUserToFollowingSet = testUser.follow(anotherUser);

      assertThat(didAddUserToFollowingSet).isTrue();
      assertThat(testUser.getUsersYouFollow()).isNotEmpty().containsOnly(anotherUser);
    }

    @Test
    void userCanFollowMultipleUsers() {
      User anotherUser = new User("Michael");
      User anotherUserTwo = new User("Michael's Clone");

      boolean didAddUserToFollowingSet = testUser.follow(anotherUser);
      boolean didAddUserToFollowingSetTwo = testUser.follow(anotherUserTwo);

      assertThat(didAddUserToFollowingSet).isTrue();
      assertThat(didAddUserToFollowingSetTwo).isTrue();
      assertThat(testUser.getUsersYouFollow())
          .isNotEmpty()
          .containsOnly(anotherUser, anotherUserTwo)
          .hasSize(2);
    }
  }

  @Nested
  class TimelineTests {

    @Test
    void userViewsOwnWall() {
      String details = "test message";
      String expectedString = "test message (1 second ago)";
      Deque<Message> messageStack = new ArrayDeque<>();

      Message message = mock(Message.class);
      when(message.formatMessage()).thenReturn(details + " (1 second ago)");
      messageStack.push(message);

      when(testUser.getTimeline()).thenReturn(messageStack);

      String actualValue = testUser.viewPersonalTimeline();

      assertThat(actualValue).isEqualTo(expectedString);
    }

    @Test
    void userViewsOwnWallWithMultipleMessage() {
      String expectedString = "test message (1 second ago)\ntest message two (2 seconds ago)";
      Deque<Message> messageStack = new ArrayDeque<>();

      Message message = mock(Message.class);
      when(message.formatMessage())
          .thenReturn("test message (1 second ago)")
          .thenReturn("test message two (2 seconds ago)");
      messageStack.push(message);
      messageStack.push(message);

      when(testUser.getTimeline()).thenReturn(messageStack);

      String actualValue = testUser.viewPersonalTimeline();

      assertThat(actualValue).isEqualTo(expectedString);
    }

    @Test
    void displayOtherUsersTimelineWhenViewed() throws InterruptedException {
      User anotherUser = new User("Michael");
      anotherUser.publishMessage("HELLO WORLD");
      TimeUnit.SECONDS.sleep(1);
      anotherUser.publishMessage("PREPARE TO BE, OR NOT TO BE");

      String actualValue = testUser.viewTimelineOfUser(anotherUser);

      assertThat(actualValue)
          .contains("HELLO WORLD")
          .containsOnlyOnce("\n")
          .contains("PREPARE TO BE, OR NOT TO BE");
    }

    @Test
    void displayAllUsersIncludingOwnTimelineWhenUserHasFollowers() {
      User anotherUser = spy(new User("Michael"));

      Message message = mock(Message.class);
      when(message.formatMessageWithNameOfUser())
          .thenReturn("Michael - HELLO WORLD (1 second ago)")
          .thenReturn("Michael - PREPARE TO BE, OR NOT TO BE (2 seconds ago)");
      when(message.getTimestamp())
          .thenReturn(Instant.now().minusSeconds(1))
          .thenReturn(Instant.now().minusSeconds(2));

      Deque<Message> messageStack = new ArrayDeque<>();
      messageStack.push(message);
      messageStack.push(message);

      when(anotherUser.getTimeline()).thenReturn(messageStack);
      // end first user setup

      // second user setup
      User anotherUserClone = spy(new User("Michael's Clone"));

      Message message2 = mock(Message.class);
      when(message2.formatMessageWithNameOfUser())
          .thenReturn(
              "Michael's Clone - THE FIRST MICHAEL IS NOT THE REAL ONE, I AM (3 seconds ago)")
          .thenReturn("Michael's Clone - DON'T BELIEVE ANYTHING HE SAYS (4 seconds ago)");
      when(message2.getTimestamp())
          .thenReturn(Instant.now().minusSeconds(3))
          .thenReturn(Instant.now().minusSeconds(4));

      Deque<Message> messageStack2 = new ArrayDeque<>();
      messageStack2.push(message2);
      messageStack2.push(message2);

      when(anotherUserClone.getTimeline()).thenReturn(messageStack2);
      // end second user setup

      // third user setup
      Message message3 = mock(Message.class);
      when(message3.formatMessageWithNameOfUser())
          .thenReturn("Julian - WHO ARE YOU PEOPLE, AND WHY ARE YOU ON MY LAWN! (5 seconds ago)");
      when(message3.getTimestamp()).thenReturn(Instant.now().minusSeconds(5));

      Deque<Message> messageStack3 = new ArrayDeque<>();
      messageStack3.push(message3);

      when(testUser.getTimeline()).thenReturn(messageStack3);
      testUser.follow(anotherUser);
      testUser.follow(anotherUserClone);
      // end third user setup

      String expectedValue =
          new StringBuilder()
              .append("Michael - ")
              .append("HELLO WORLD (1 second ago)")
              .append("\n")
              .append("Michael - ")
              .append("PREPARE TO BE, OR NOT TO BE (2 seconds ago)")
              .append("\n")
              .append("Michael's Clone - ")
              .append("THE FIRST MICHAEL IS NOT THE REAL ONE, I AM (3 seconds ago)")
              .append("\n")
              .append("Michael's Clone - ")
              .append("DON'T BELIEVE ANYTHING HE SAYS (4 seconds ago)")
              .append("\n")
              .append("Julian - ")
              .append("WHO ARE YOU PEOPLE, AND WHY ARE YOU ON MY LAWN! (5 seconds ago)")
              .toString();

      String actualValue = testUser.viewWall();

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }
}
