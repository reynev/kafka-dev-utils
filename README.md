# kafka-dev-utils

Simple REST server to simplify developing application that uses Kafka.

## Use cases

- [x] Create message on topic
- [x] Read x latest messages from topic
- [x] List topics
- [ ] Create message on partition
- [ ] Read x latest messages from partition
- [ ] List partition for topics
- [ ] List consumer groups and consumers?
- [ ] Show Metrics (KafkaConsumer.metrics() ??)

## How to run it?

* Kafka 0.10.1.0  - not tested with other versions
 * Ready to use docker container: [spotify/kafka/](https://hub.docker.com/r/spotify/kafka)
* Run application
 * Build poject `mvn clean install`
 * Run `java -jar target/kafka-dev-utils-1.0.0-SNAPSHOT.jar`
* Open swagger-ui http://localhost:8080/swagger-ui.html#
