package kata;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class User {

  private final Stack<Message> timeline = new Stack<>();
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

  public Stack<Message> getTimeline() {
    return timeline;
  }

  public boolean follow(User anotherUser) {
    return this.getUsersYouFollow().add(anotherUser);
  }

  public Message publishMessage(final String details) {
    return this.getTimeline().push(new Message(this, details));
  }
}
