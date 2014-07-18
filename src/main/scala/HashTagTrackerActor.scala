package twitter
import akka.actor._
import twitter4j.Status
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

trait HashTagTrackerComponent {
  def hashActor: ActorRef
}

trait AkkaHashTagTrackerComponent extends HashTagTrackerComponent with StorageComponent with AkkaSystem {
  val hashActor = system.actorOf(Props(new HashTagTrackerActor))

  class HashTagTrackerActor extends Actor {
    import context._

    def receive: Receive = {
      case h:HashTagEntities =>
        storageActor ! HashTags(h.tags.map(ht => ht.getText).toList)
    }
  }
}
