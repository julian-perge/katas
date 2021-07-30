package kata;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

  private final User testUser = new User("Julian");

  @Test
  void publishMessageAddsToTimeline() {
    String details = "test message";

    Message expectedValue = new Message(testUser, details);

    Message actualValue = testUser.publishMessage(details);

    assertThat(testUser.getTimeline()).hasSize(1);
    assertThat(actualValue.getDetails()).isEqualTo(expectedValue.getDetails());
  }

  @Test
  void publishMultipleMessageAddsToTimelineInTimePostedOrder() throws InterruptedException {
    String details = "test message";

    Message messageOnePublish = testUser.publishMessage(details);
    TimeUnit.SECONDS.sleep(1);

    Message messageTwoPublish = testUser.publishMessage("new message");

    assertThat(testUser.getTimeline()).hasSize(2);
    assertThat(testUser.getTimeline()).containsSequence(messageOnePublish, messageTwoPublish);
  }

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

  @Test
  void userViewsOwnWall() {

    String details = "test message";

    Message expectedValue = new Message(testUser, details);

    Message actualValue = testUser.publishMessage(details);

    assertThat(testUser.getTimeline()).hasSize(1);
    assertThat(actualValue.getDetails()).isEqualTo(expectedValue.getDetails());
  }
}
