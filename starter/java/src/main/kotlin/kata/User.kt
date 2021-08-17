package kata

data class User(val name: String) {

	val timeline = ArrayDeque<Message>()
	val following = hashSetOf<User>()
	val blocked = hashSetOf<User>()

	fun viewPersonalTimeline() = timeline.joinToString("\n") { it.formatMessage() }

	fun viewWall(): String {
		return this.timeline.asSequence().plus(this.following.flatMap { it.timeline })
			.sortedWith(Message.SortByTimeStamp().reversed())
			.map { it.formatMessageWithNameOfUser() }
			.joinToString { "\n" }
	}

	fun follow(anotherUser: User): Boolean {
		if (anotherUser.blocked.contains(this)) {
			return false
		}
		return this.following.add(anotherUser)
	}

	fun publishMessage(details: String) = this.timeline.addFirst(Message(this, details))

	fun viewTimelineOfUser(otherUser: User) = otherUser.viewPersonalTimeline()

	fun unfollow(otherUser: User) = this.following.remove(otherUser)

	fun block(otherUser: User) {
		this.blocked.add(otherUser)
		this.unfollow(otherUser)
		otherUser.unfollow(this)
	}
}
