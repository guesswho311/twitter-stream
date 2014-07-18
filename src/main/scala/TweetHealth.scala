package twitter

import com.codahale.metrics._

object TweetHealth {
  val metrics = new MetricRegistry
  val tweetsReceived:Counter = metrics.counter(MetricRegistry.name("total-tweets"))

  def incrementTweets = tweetsReceived.inc
  def getTotalTweets = tweetsReceived.getCount

  val tweetsMeter: Meter = metrics.meter(MetricRegistry.name("rate-of-tweetes"))

  def markTweet = tweetsMeter.mark
  def getTweetRate = tweetsMeter.getMeanRate

  val tweetsWithEmojis:Counter = metrics.counter(MetricRegistry.name("emoji-tweets"))
  def incrementEmoTweets = tweetsWithEmojis.inc
  def getTotalEmoTweets = tweetsWithEmojis.getCount

  val tweetsWithUrls: Counter = metrics.counter(MetricRegistry.name("url-tweets"))
  def incrementUrlTweets = tweetsWithUrls.inc
  def getTotalUrlTweets = tweetsWithUrls.getCount

  val tweetsWithPhotoUrls: Counter = metrics.counter(MetricRegistry.name("photo-url-tweets"))
  def incrementPhotoUrlTweets = tweetsWithPhotoUrls.inc
  def getTotalPhotoUrlTweets = tweetsWithPhotoUrls.getCount
}
