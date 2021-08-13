package kata;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User {

  private final Deque<Message> timeline = new ArrayDeque<>();
  private final String name;
  private final Set<User> following = new HashSet<>();

  public User(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Set<User> getUsersYouFollow() {
    return following;
  }

  public Deque<Message> getTimeline() {
    return timeline;
  }

  public String viewPersonalTimeline() {
    return this.getTimeline().stream()
        .map(Message::formatMessage)
        .collect(Collectors.joining("\n"));
  }

  public String viewWall() {
    return Stream.concat(this.getTimeline().stream(), this.getUsersYouFollow().stream()
            .flatMap((User user) -> user.getTimeline().stream()))
        .sorted(Message.SORT_BY_TIME_STAMP.reversed())
        .map(Message::formatMessageWithNameOfUser)
        .collect(Collectors.joining("\n"));
  }

  public boolean follow(final User anotherUser) {
    return this.getUsersYouFollow().add(anotherUser);
  }

  public void publishMessage(final String details) {
    this.getTimeline().push(new Message(this, details));
  }

  public String viewTimelineOfUser(final User otherUser) {
    return otherUser.viewPersonalTimeline();
  }

  public void unfollow(User otherUser) {
    this.getUsersYouFollow().remove(otherUser);
  }
}
