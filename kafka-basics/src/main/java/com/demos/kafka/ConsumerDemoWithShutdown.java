package com.demos.kafka;


import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemoWithShutdown {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerDemoWithShutdown.class.getSimpleName());

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

        // get a reference to the main thread
        final Thread mainThread = Thread.currentThread();

        // summary: 셧다운 훅을 등록하고, 셧다운 훅이 호출되면 consumer.wakeup()을 호출함
        // consumer.wakeup()을 호출하면 consumer.poll()이 호출되고, consumer.poll()이 호출되면 InterruptedException이 발생함
        // 그 다음 메인 스레드에 합류해서 이 페이지의 코드가 전부 완료되고 더 이상 실행되지 않을 때까지 기다림
        //
        // 즉, 셧다운 훅이 호출되면 프로그램이 종료됨

        // add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                logger.info("Detected a shutdown, let's exit by calling consumer.wakeup()");
                consumer.wakeup();
                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
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
        } catch (WakeupException e) {
            logger.info("Consumer is starting to shutdown");
        } catch (Exception e) {
            logger.error("unexpected error in the consumer");
        } finally {
            logger.info("the consumer is now gracefully shutting down");
            consumer.close(); // this will close the consumer and its offsets
        }
    }
}
