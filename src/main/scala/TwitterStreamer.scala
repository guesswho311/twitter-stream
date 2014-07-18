package com.johnnyquest.twitter

import com.johnnyquest.twitter.processors._
import twitter4j._
import twitter4j.conf.ConfigurationBuilder
import akka.actor._
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global

trait TwitterStreamer extends AkkaTweetTrackerComponent with AkkaStorageComponent with AkkaEmojiTracker with AkkaUrlTracker with AkkaHashTagTrackerComponent{
  val listener = new StatusListener {
    def onStatus(status: Status) = {
      tweetTrackerActor ! LogTweet
      emojiActor ! status
      urlActor ! Urls(status.getURLEntities)
      hashActor ! HashTagEntities(status.getHashtagEntities)
    }

    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }

  import Config._
  lazy val twitterConfig = new ConfigurationBuilder()
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessSecret)

  val twitterStream = new TwitterStreamFactory(twitterConfig.build).getInstance()
  twitterStream.addListener(listener)
  twitterStream.sample
}
