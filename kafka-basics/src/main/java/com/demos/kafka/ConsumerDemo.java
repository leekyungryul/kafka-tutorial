package com.demos.kafka;


import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {

        logger.info("I'm a Kafka Consumer");

        String groupId = "my-java-application";
        String topic = "demo_java";

        // Create a Producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "43.201.105.31:9092");
        // set the key deserializer
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        // set the value deserializer
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        // none, earliest, latest
        // none: if no offsets are saved for this group, an exception will be thrown
        // earliest: automatically reset the offset to the earliest offset
        // latest: automatically reset the offset to the latest offset
        properties.setProperty("auto.offset.reset", "earliest");

        // Create a consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe consumer to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        // poll for new data
        while (true) {
            logger.info("Polling");
            consumer.poll(1000).forEach(record -> {
                logger.info("Key: " + record.key() + " | Value: " + record.value());
                logger.info("Partition: " + record.partition() + " | Offset: " + record.offset());
            });
        }
    }
}
