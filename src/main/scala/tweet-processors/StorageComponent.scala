package com.johnnyquest.twitter.processors

import com.johnnyquest.twitter._
import akka.actor._
import akka.routing.FromConfig
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

trait StorageComponent {
  def storageActor: ActorRef
}

trait AkkaStorageComponent extends StorageComponent with AkkaSystem{
  val storageActor = system.actorOf(Props(new StorageActor).withRouter(FromConfig()).withDispatcher("storage-dispatcher"), "storage")

  class StorageActor extends Actor {
    import context._
    var hostAddresses: List[String] = List()
    var hashtags: List[String] = List()
    var emojis: List[String] = List()

    def receive: Receive = {
      case hosts: Hosts => hostAddresses = hostAddresses ++ hosts.urls

      case h: HashTags => hashtags = hashtags ++ h.tags

      case emoticons:Emojis => emojis = emojis ++ emoticons.e

      case TweetReport =>
        println("--------------------- Tweet Report -------------------------")
        println("Total tweets processed: " + TweetHealth.getTotalTweets)
        println("Tweets per second: " + Math.round(TweetHealth.getTweetRate))

        val topEmoji = getTopItem(emojis)
        val totalEmojiTweets = TweetHealth.getTotalEmoTweets
        println("Percentage of tweets with emojis: " + (totalEmojiTweets.toDouble / TweetHealth.getTotalTweets.toDouble) * 100)
        println("Top emoji: " + topEmoji)

        val topHashTag = getTopItem(hashtags)
        println("Top hashtag in tweets: " + topHashTag)

        println("Percentage of tweets with urls: " + (TweetHealth.getTotalUrlTweets.toDouble / TweetHealth.getTotalTweets.toDouble) * 100)
        println("Percentage of tweets with photos: " + (TweetHealth.getTotalPhotoUrlTweets.toDouble / TweetHealth.getTotalTweets.toDouble) * 100)

        val topDomain = getTopItem(hostAddresses)
        println("Top host in tweets: " + topDomain)
        println("--------------------- End Transmission -----------------------------")
        println()

        scheduleTweetReport(5)
    }

    def getTopItem(ls: List[String]) = {
      try {
        ls.groupBy(identity).mapValues(_.size).maxBy(i => i._2)._1
      } catch {
        case e: java.lang.UnsupportedOperationException => scheduleTweetReport(5)
      }
    }

    def scheduleTweetReport(time: Int) =
      context.system.scheduler.scheduleOnce(FiniteDuration(time, TimeUnit.SECONDS)){
        self ! TweetReport
      }
  }
}
