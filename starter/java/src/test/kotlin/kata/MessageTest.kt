package kata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.temporal.ChronoUnit

class MessageTest {

	private val testUser = User("Julian")
	private val testMessageDetails = "test message"
	private val testMessage = spy(Message(testUser, testMessageDetails))

	@Test
	fun formatMessageWithoutNameTest() {
		// Bob - Good game though. (1 minute ago)
		val details = "i love shrimp"

		val message = spy(Message(testUser, details))
		whenever(message.calculateTimeFromWhenPosted()).thenReturn("(1 minute ago)")

		val expectedValue = "i love shrimp (1 minute ago)"

		val actualValue = message.formatMessage()

		assertThat(actualValue).isEqualTo(expectedValue)
	}

	@Test
	fun formatMessageWithNameTest() {
		// Bob - Good game though. (1 minute ago)
		val details = "i love shrimp"

		val message = spy(Message(testUser, details))
		whenever(message.calculateTimeFromWhenPosted()).thenReturn("(1 minute ago)")

		val expectedValue = "Julian - i love shrimp (1 minute ago)"

		val actualValue = message.formatMessageWithNameOfUser()

		assertThat(actualValue).isEqualTo(expectedValue)
	}

	@Nested
	class AppendIfPluralTests {

		private val testMessage = mock(Message::class.java)
		private val timeUnitSimpleName = "hour"

		@Test
		fun doNotMakePluralIfTimeUnitIsLessThanZero() {
			val expectedValue = "hour"

			val actualValue = testMessage.appendIfPlural(-1, timeUnitSimpleName)

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun doNotMakePluralIfTimeUnitIsEqualToOne() {
			val expectedValue = "hour"

			val actualValue = testMessage.appendIfPlural(1, timeUnitSimpleName)

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun makePluralIfTimeUnitIsEqualToZero() {
			val expectedValue = "hours"

			val actualValue = testMessage.appendIfPlural(0, timeUnitSimpleName)

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun makePluralIfTimeUnitIsGreaterThanOne() {
			val expectedValue = "hours"

			val actualValue = testMessage.appendIfPlural(2, timeUnitSimpleName)

			assertThat(actualValue).isEqualTo(expectedValue)
		}
	}

	@Nested
	class CalculateTimeFromWhenPostedTests {

		private val testMessage = mock(Message::class.java)

		@Test
		fun whenDaysIsEqualToOne() {
			val testMessage1 = mock(Message::class.java)

			val expectedValue = "(1 day ago)"

			whenever(testMessage1.timePosted)
				.thenReturn(Instant.now().minus(1, ChronoUnit.DAYS))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenDaysIsGreaterThanOne() {
			val expectedValue = "(2 days ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minus(2, ChronoUnit.DAYS))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenHoursIsEqualToOne() {
			val expectedValue = "(1 hour ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minus(1, ChronoUnit.HOURS))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenHoursIsGreaterThanOne() {
			val expectedValue = "(2 hours ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minus(2, ChronoUnit.HOURS))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenMinutesIsEqualToOne() {
			val expectedValue = "(1 minute ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minus(1, ChronoUnit.MINUTES))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenMinutesIsGreaterThanOne() {
			val expectedValue = "(2 minutes ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minus(2, ChronoUnit.MINUTES))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenSecondsIsEqualToOne() {
			val expectedValue = "(1 second ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minusSeconds(1))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}

		@Test
		fun whenSecondsIsGreaterThanOne() {
			val expectedValue = "(2 seconds ago)"

			whenever(testMessage.timePosted).thenReturn(Instant.now().minusSeconds(2))
			val actualValue = testMessage.calculateTimeFromWhenPosted()

			assertThat(actualValue).isEqualTo(expectedValue)
		}
	}
}
