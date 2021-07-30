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
    String messageFormatted = "";

    final Duration timeBetween = Duration.between(this.getTimestamp(), Instant.now());
    final long numDays = timeBetween.toDays();
    final long numHours = timeBetween.toHours();
    final long numMinutes = timeBetween.toMinutes();
    final long numSeconds = timeBetween.toSeconds();

    if (numDays > 0) messageFormatted = getFormatForTimeUnit(numDays, "day");
    else if (numHours > 0) messageFormatted = getFormatForTimeUnit(numHours, "hour");
    else if (numMinutes > 0) messageFormatted = getFormatForTimeUnit(numMinutes, "minute");
    else messageFormatted = getFormatForTimeUnit(numSeconds, "second");

    return String.format("(%s)", messageFormatted);
  }

  private String getFormatForTimeUnit(long timeUnit, String timeUnitSimpleName) {
    return String.format("%s %s%s ago", timeUnit, timeUnitSimpleName, appendIfPlural(timeUnit));
  }

  public String appendIfPlural(final long timeUnit) {
    if (timeUnit < 0 || timeUnit == 1) return "";
    else return "s";
  }

  public String formatMessage() {
    return this.getDetails() + " " + calculateTimeFromWhenPosted();
  }

  public String formatMessageWithUsersName() {
    return this.getUserWhoPostedMessage().getName() + " - " + this.formatMessage();
  }
}
