package kata

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.time.Instant
import java.util.concurrent.TimeUnit

class UserTest {

	@Nested
	class PublishingMessagesTests {

		private val testUser = User("Julian")

		@Test
		fun publishMessageAddsToTimeline() {
			val details = "test message"

			testUser.publishMessage(details)

			assertThat(testUser.timeline).hasSize(1)
			assertThat(testUser.timeline.first().details).isEqualTo("test message")
		}

		@Test
		fun publishMultipleMessageAddsToTimelineInTimePostedOrder() {
			val details = "test message"

			testUser.publishMessage(details)
			TimeUnit.SECONDS.sleep(1)

			testUser.publishMessage("new message")

			assertThat(testUser.timeline).hasSize(2)
			assertThat(testUser.timeline.first().details).isEqualTo("new message")
			assertThat(testUser.timeline.last().details).isEqualTo("test message")
		}
	}

	@Nested
	class FollowingTest {

		private val testUser = User("Julian")

		@Test
		fun userCanAddAnotherUserForFollowing() {
			val anotherUser = User("Michael")

			val didAddUserToFollowingSet = testUser.follow(anotherUser)

			assertThat(didAddUserToFollowingSet).isTrue
			assertThat(testUser.following).isNotEmpty.containsOnly(anotherUser)
		}

		@Test
		fun userCanFollowMultipleUsers() {
			val anotherUser = User("Michael")
			val anotherUserTwo = User("Michael's Clone")

			val didAddUserToFollowingSet = testUser.follow(anotherUser)
			val didAddUserToFollowingSetTwo = testUser.follow(anotherUserTwo)

			assertThat(didAddUserToFollowingSet).isTrue
			assertThat(didAddUserToFollowingSetTwo).isTrue
			assertThat(testUser.following)
				.isNotEmpty
				.containsOnly(anotherUser, anotherUserTwo)
				.hasSize(2)
		}
	}

	@Nested
	class TimelineTests {

		private val testUser = User("Julian")

		@Test
		fun userViewsOwnWall() {
			val details = "test message"
			val expectedString = "test message (1 second ago)"
			val messageStack = ArrayDeque<Message>()

			val message = Mockito.mock(Message::class.java)
			Mockito.`when`(message.formatMessage()).thenReturn("$details (1 second ago)")
			messageStack.addFirst(message)

			Mockito.`when`(testUser.timeline).thenReturn(messageStack)

			val actualValue = testUser.viewPersonalTimeline()

			assertThat(actualValue).isEqualTo(expectedString)
		}

		@Test
		fun userViewsOwnWallWithMultipleMessage() {
			val expectedString = "test message (1 second ago)\ntest message two (2 seconds ago)"
			val messageStack = ArrayDeque<Message>()

			val message: Message = mock(Message::class.java)
			Mockito.`when`(message.formatMessage())
				.thenReturn("test message (1 second ago)")
				.thenReturn("test message two (2 seconds ago)")
			messageStack.addFirst(message)
			messageStack.addFirst(message)

			`when`(testUser.timeline).thenReturn(messageStack)

			val actualValue = testUser.viewPersonalTimeline()

			assertThat(actualValue).isEqualTo(expectedString)
		}

		@Test
		fun displayOtherUsersTimelineWhenViewed() {
			val anotherUser = User("Michael")
			anotherUser.publishMessage("HELLO WORLD")
			TimeUnit.SECONDS.sleep(1)
			anotherUser.publishMessage("PREPARE TO BE, OR NOT TO BE")

			val actualValue = testUser.viewTimelineOfUser(anotherUser)

			assertThat(actualValue)
				.contains("HELLO WORLD")
				.containsOnlyOnce("\n")
				.contains("PREPARE TO BE, OR NOT TO BE")
		}

		@Test
		fun displayAllUsersIncludingOwnTimelineWhenUserHasFollowers() {
			val anotherUser = spy(User("Michael"))

			val message = mock(Message::class.java)
			`when`(message.formatMessageWithNameOfUser())
				.thenReturn("Michael - HELLO WORLD (1 second ago)")
				.thenReturn("Michael - PREPARE TO BE, OR NOT TO BE (2 seconds ago)")
			`when`(message.timePosted)
				.thenReturn(Instant.now().minusSeconds(1))
				.thenReturn(Instant.now().minusSeconds(2))

			val messageStack = ArrayDeque<Message>()
			messageStack.addFirst(message)
			messageStack.addFirst(message)

			`when`(anotherUser.timeline).thenReturn(messageStack)
			// end first user setup

			// second user setup
			val anotherUserClone = spy(User("Michael's Clone"))

			val message2 = mock(Message::class.java)
			`when`(message2.formatMessageWithNameOfUser())
				.thenReturn(
					"Michael's Clone - THE FIRST MICHAEL IS NOT THE REAL ONE, I AM (3 seconds ago)"
				)
				.thenReturn("Michael's Clone - DON'T BELIEVE ANYTHING HE SAYS (4 seconds ago)")
			`when`(message2.timePosted)
				.thenReturn(Instant.now().minusSeconds(3))
				.thenReturn(Instant.now().minusSeconds(4))

			val messageStack2 = ArrayDeque<Message>()
			messageStack2.addFirst(message2)
			messageStack2.addFirst(message2)

			`when`(anotherUserClone.timeline).thenReturn(messageStack2)
			// end second user setup

			// third user setup
			val message3 = mock(Message::class.java)
			`when`(message3.formatMessageWithNameOfUser())
				.thenReturn("Julian - WHO ARE YOU PEOPLE, AND WHY ARE YOU ON MY LAWN! (5 seconds ago)")
			`when`(message3.timePosted).thenReturn(Instant.now().minusSeconds(5))

			val messageStack3 = ArrayDeque<Message>()
			messageStack3.addFirst(message3)

			`when`(testUser.timeline).thenReturn(messageStack3)
			testUser.follow(anotherUser)
			testUser.follow(anotherUserClone)
			// end third user setup

			val expectedValue =
				StringBuilder()
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
					.toString()

			val actualValue = testUser.viewWall()

			assertThat(actualValue).isEqualTo(expectedValue)
		}
	}

	@Nested
	class UnfollowTests {

		private val anotherUser = User("Alice")

		@Test
		fun userUnfollowsOtherUser() {
			val otherUser = User("Bob")
			anotherUser.follow(otherUser)
			anotherUser.unfollow(otherUser)
			assertThat(anotherUser.following).doesNotContain(otherUser)
		}

		@Test
		fun whenUserAUnfollowsUserBThenUserBDoesNotShowOnUserAWall() {
			val otherUser = User("Bob")
			anotherUser.follow(otherUser)
			otherUser.publishMessage("Alice is being mean to me")
			assertThat(anotherUser.viewWall()).contains("Bob")
			anotherUser.unfollow(otherUser)
			assertThat(anotherUser.viewWall()).doesNotContain("Bob")
		}
	}


	@Nested
	class BlockTests {

		@Test
		fun givenTwoUsersThatFollowEachOther() {
			val userBob = User("Bob")
			val userAlice = User("Alice")
			userAlice.publishMessage("I am Test User, hear me roar!")
			userBob.publishMessage("No you're not, you're a FOOL.")
			userBob.follow(userAlice)
			userAlice.follow(userBob)

			userBob.block(userAlice)

			assertThat(userBob.viewWall()).doesNotContain("Alice")
			assertThat(userBob.following).doesNotContain(userAlice)
			assertThat(userBob.blocked).contains(userAlice)
			assertThat(userAlice.viewWall()).doesNotContain(userBob.name)
			assertThat(userBob.following).contains(userAlice)

			userAlice.follow(userBob)

			assertThat(userAlice.following).doesNotContain(userBob)
		}

	}

}
