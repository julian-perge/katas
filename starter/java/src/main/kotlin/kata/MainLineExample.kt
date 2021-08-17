package kata

import java.util.concurrent.TimeUnit

class MainLineExample {

	fun main(args: Array<String>) {
		// just Alice
		exampleOne()

		// Alice viewing Bob's timeline
		exampleTwo()

		// Charlie following Alice and Bob, then viewing Charlie's wall
		exampleThree()
	}

	private fun exampleOne() {
		// create users
		val userAlice = User("Alice")

		// publish messages
		userAlice.publishMessage("I love the weather today.")

		println("=== Example One ===\n${userAlice.viewPersonalTimeline()}\n")
	}

	private fun exampleTwo() {
		// create users
		val userAlice = User("Alice")
		val userBob = User("Bob")

		// publish messages
		userBob.publishMessage("Darn! We lost!")
		TimeUnit.SECONDS.sleep(10)
		userBob.publishMessage("Good game though.")
		TimeUnit.SECONDS.sleep(10)

		println("=== Example Two ===\n${userAlice.viewTimelineOfUser(userBob)}\n")
	}

	private fun exampleThree() {
		// create users
		val userAlice = User("Alice")
		val userBob = User("Bob")
		val userCharlie = User("Charlie")

		// publish messages
		userAlice.publishMessage("I love the weather today.")
		TimeUnit.SECONDS.sleep(5)

		userBob.publishMessage("Darn! We lost!")
		TimeUnit.SECONDS.sleep(10)
		userBob.publishMessage("Good game though.")
		TimeUnit.SECONDS.sleep(10)

		userCharlie.publishMessage("I'm in New York today! Anyone wants to have a coffee?")
		TimeUnit.SECONDS.sleep(30)

		// following other users
		userCharlie.follow(userAlice)
		userCharlie.follow(userBob)

		// print wall of user Charlie
		println("=== Example Three ===\n${userCharlie.viewWall()}\n")
	}
}
