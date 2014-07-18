package com.johnnyquest.twitter

import com.typesafe.config.ConfigFactory

object Config {
  lazy val TweetConfig = ConfigFactory.load()

  val consumerKey = TweetConfig.getString("twitter.consumer-key")
  val consumerSecret = TweetConfig.getString("twitter.consumer-secret")
  val accessToken = TweetConfig.getString("twitter.access-token")
  val accessSecret = TweetConfig.getString("twitter.access-secret")
}
