package com.demos.kafka;


import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithKeys {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class.getSimpleName());

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

        for (int j = 0; j < 2; j++) {

            for (int i = 0; i < 10; i++) {

                String topic = "demo_java";
                String key = "id_" + i;
                String value = "onyu i miss you " + i;

                // Create a Producer Record
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

                // send data
                // asynchronous 이므로 close를 호출하지 않으면 데이터를 전송하기 전에 프로세스가 종료됨
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        // executes every time a record is successfully sent or an exception is thrown
                        if (e == null) {
                            // the record was successfully sent
                            logger.info("Key: " + key + " | Partition: " + metadata.partition());
                        } else {
                            logger.error("Error while producing: ", e);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }

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
