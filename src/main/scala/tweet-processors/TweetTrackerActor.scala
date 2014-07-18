package com.johnnyquest.twitter.processors

import com.johnnyquest.twitter._
import akka.actor._
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import twitter4j.Status
import akka.routing.FromConfig

trait TweetTrackerComponent {
  def tweetTrackerActor: ActorRef
}

trait AkkaTweetTrackerComponent extends TweetTrackerComponent with AkkaSystem {
  val tweetTrackerActor = system.actorOf(Props(new TweetTrackerActor).withRouter(FromConfig()).withDispatcher("tweet-dispatcher"), "tweet-trackers")
  class TweetTrackerActor extends Actor {
    import context._
    def receive: Receive = {
      case LogTweet =>
        TweetHealth.incrementTweets
        TweetHealth.markTweet
    }
  }
}
