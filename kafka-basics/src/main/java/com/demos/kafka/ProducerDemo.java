package com.demos.kafka;


import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {

        /**
         * 프로그램 실행 전 아래 명령어로 토픽을 생성해야함
         * kafka-topics.sh --create --bootstrap-server localhost:9092 --partitions 3 --topic demo_java
         */

        logger.info("I'm a Kafka Producer");

        // Create a Producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // set the key serializer
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        // set the value serializer
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // Create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Create a Producer Record
         ProducerRecord<String, String> record = new ProducerRecord<>("demo_java", "onyu");

        // send data
        // asynchronous 이므로 close를 호출하지 않으면 데이터를 전송하기 전에 프로세스가 종료됨
        producer.send(record);

        // flush data
        // tell the producer to send all data and block until done -- synchronous
        // close를 호출하면 자동으로 flush가 호출됨
        // 필요시에만 호출하면 됨
        // 실제 운영에서는 close를 호출하지 않고 flush만 호출함
        producer.flush();

        // flush and close producer
        // 프로그램 종료 전에 close를 호출해야함
        producer.close();
    }
}
