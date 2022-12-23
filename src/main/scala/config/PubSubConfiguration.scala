package config

case class PubSubConfiguration(
    projectId: String,
    host: String,
    port: Int,
    keyFileLocation: String,
    subscriptions: PubSubSubscriptions,
    topics: PubSubTopics
)

case class PubSubSubscriptions(
    orders: String
)

case class PubSubTopics(
    orders: String
)
