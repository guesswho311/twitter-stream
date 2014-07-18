package twitter

import akka.actor._
import akka.routing.FromConfig
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import twitter4j.Status

trait UrlTrackerComponent {
  def urlActor: ActorRef
}

trait AkkaUrlTracker extends UrlTrackerComponent with AkkaSystem with StorageComponent {
  val urlActor = system.actorOf(Props(new UrlTrackerActor).withRouter(FromConfig()).withDispatcher("url-dispatcher"), "url-trackers")

  class UrlTrackerActor extends Actor {
    import context._
    import java.net.URL
    def receive: Receive = {
      case u:Urls =>
        if (!u.urls.isEmpty) TweetHealth.incrementUrlTweets
        val photoUrls = u.urls.filter(url => (url.getExpandedURL contains "pic.twitter.com") || (url.getExpandedURL contains "instagram"))
        if (photoUrls.length >= 1) TweetHealth.incrementPhotoUrlTweets

        storageActor ! Hosts(u.urls.map(u => new URL(u.getExpandedURL).getHost).toList)
    }
  }
}
