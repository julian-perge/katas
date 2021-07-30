package kata;

import java.util.concurrent.TimeUnit;

public class MainLineExample {

  public static void main(String[] args) throws InterruptedException {
    // just Alice
    exampleOne();

    // Alice viewing Bob's timeline
    exampleTwo();

    // Charlie following Alice and Bob, then viewing Charlie's wall
    exampleThree();
  }

  public static void exampleOne() {
    // create users
    User userAlice = new User("Alice");

    // publish messages
    userAlice.publishMessage("I love the weather today.");

    System.out.println("=== Example One ===\n" + userAlice.viewPersonalTimeline() + "\n");
  }

  public static void exampleTwo() throws InterruptedException {
    // create users
    User userAlice = new User("Alice");
    User userBob = new User("Bob");

    // publish messages
    userBob.publishMessage("Darn! We lost!");
    TimeUnit.SECONDS.sleep(10);
    userBob.publishMessage("Good game though.");
    TimeUnit.SECONDS.sleep(10);

    System.out.println("=== Example Two ===\n" + userAlice.viewTimelineOfUser(userBob) + "\n");
  }

  public static void exampleThree() throws InterruptedException {
    // create users
    User userAlice = new User("Alice");
    User userBob = new User("Bob");
    User userCharlie = new User("Charlie");

    // publish messages
    userAlice.publishMessage("I love the weather today.");
    TimeUnit.SECONDS.sleep(5);

    userBob.publishMessage("Darn! We lost!");
    TimeUnit.SECONDS.sleep(10);
    userBob.publishMessage("Good game though.");
    TimeUnit.SECONDS.sleep(10);

    userCharlie.publishMessage("I'm in New York today! Anyone wants to have a coffee?");
    TimeUnit.SECONDS.sleep(30);

    // following other users
    userCharlie.follow(userAlice);
    userCharlie.follow(userBob);

    // print wall of user Charlie
    System.out.println("=== Example Three ===\n" + userCharlie.viewWall() + "\n");
  }
}
