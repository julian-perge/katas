package kata;

import java.time.Duration;
import java.time.Instant;

public class Message {

  private final User userWhoPostedMessage;
  private final String details;
  private final Instant timePosted;

  public Message(User userWhoPostedMessage, String details) {
    this.userWhoPostedMessage = userWhoPostedMessage;
    this.details = details;
    this.timePosted = Instant.now();
  }

  public User getUserWhoPostedMessage() {
    return userWhoPostedMessage;
  }

  public String getDetails() {
    return details;
  }

  public Instant getTimestamp() {
    return timePosted;
  }

  public String calculateTimeFromWhenPosted() {
    return String.valueOf(Duration.between(this.getTimestamp(), Instant.now()).getSeconds());
  }

  public String formatMessage() {
    return calculateTimeFromWhenPosted();
  }
}
