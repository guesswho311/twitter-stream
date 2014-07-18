package twitter

import twitter4j._

case object LogTweet
case object TweetReport
case class Urls(urls: Array[URLEntity])
case class HashTagEntities(tags: Array[HashtagEntity])

case class Hosts(urls: List[String])
case class HashTags(tags: List[String])
case class Emojis(e: List[String])
