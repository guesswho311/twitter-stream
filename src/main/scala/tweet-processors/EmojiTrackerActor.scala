package com.johnnyquest.twitter.processors

import com.johnnyquest.twitter._
import akka.actor._
import akka.routing.FromConfig
import twitter4j.Status
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

trait EmojiTrackerComponent {
  def emojiActor: ActorRef
}

trait AkkaEmojiTracker extends EmojiTrackerComponent with AkkaSystem with StorageComponent{
  val emojiActor = system.actorOf(Props(new EmojiTrackerActor).withRouter(FromConfig()).withDispatcher("emoji-dispatcher"), "emoji-trackers")

  class EmojiTrackerActor extends Actor {
    import context._
    def receive: Receive = {
      case s: Status =>
        val emoticons = "[^\u0000-\uFFFF]".r.findAllIn(s.getText)
        if (!emoticons.isEmpty) {
          TweetHealth.incrementEmoTweets
          storageActor ! Emojis(emoticons.map(e => e).toList)
        }
    }
  }
}
