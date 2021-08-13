package kata;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

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

  private String getFormatForTimeUnit(final long timeUnit, final String timeUnitSimpleName) {
    return String.format("%s %s ago", timeUnit, appendIfPlural(timeUnit, timeUnitSimpleName));
  }

  public String appendIfPlural(final long timeUnit, final String timeUnitSimpleName) {
    if (timeUnit < 0 || timeUnit == 1) return timeUnitSimpleName;
    else return timeUnitSimpleName + "s";
  }

  public String formatMessage() {
    return this.getDetails() + " " + calculateTimeFromWhenPosted();
  }

  public String formatMessageWithNameOfUser() {
    return this.getUserWhoPostedMessage().getName() + " - " + this.formatMessage();
  }

  public static final Comparator<Message> SORT_BY_TIME_STAMP =
      Comparator.comparing(Message::getTimestamp);
}
