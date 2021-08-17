package kata

import lombok.Data
import lombok.Getter
import java.time.Duration
import java.time.Instant

data class Message(
	private val userWhoPostedMessage: User,
	val details: String,
) {

	val timePosted: Instant = Instant.now()

	fun calculateTimeFromWhenPosted(): String {
		val timeBetween = Duration.between(this.timePosted, Instant.now())
		val numDays = timeBetween.toDays()
		val numHours = timeBetween.toHours()
		val numMinutes = timeBetween.toMinutes()
		val numSeconds = timeBetween.toSeconds()

		val messageFormatted = if (numDays > 0) getFormatForTimeUnit(numDays, "day")
		else if (numHours > 0) getFormatForTimeUnit(numHours, "hour")
		else if (numMinutes > 0) getFormatForTimeUnit(numMinutes, "minute")
		else getFormatForTimeUnit(numSeconds, "second")

		return "(${messageFormatted})"
	}

	private fun getFormatForTimeUnit(timeUnit: Long, timeUnitSimpleName: String) =
		"$timeUnit ${this.appendIfPlural(timeUnit, timeUnitSimpleName)} ago"

	fun appendIfPlural(timeUnit: Long, timeUnitSimpleName: String): String {
		return if (timeUnit < 0L || timeUnit == 1L) timeUnitSimpleName
		else "${timeUnitSimpleName}s"
	}

	fun formatMessage() = "$details ${calculateTimeFromWhenPosted()}"

	fun formatMessageWithNameOfUser() = "${this.userWhoPostedMessage.name} - ${this.formatMessage()}"

//	public fun <Message> Comparator test =

	class SortByTimeStamp : Comparator<Message> {
		override fun compare(o1: Message?, o2: Message?): Int {
			if (o1 == null || o2 == null) return 0
			return o1.timePosted.compareTo(o2.timePosted)
		}
	}
}
