package twitter

import akka.actor._
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with TwitterStreamer with AkkaSystem {
  implicit lazy val system = ActorSystem("tweet-stream-system")

  system.scheduler.scheduleOnce(FiniteDuration(5, TimeUnit.SECONDS)){
    storageActor ! TweetReport
  }
}

trait AkkaSystem {
  implicit val system: ActorSystem
}
