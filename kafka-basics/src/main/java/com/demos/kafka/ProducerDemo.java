package com.demos.kafka;


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemo {

    private static final Logger logger = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {

        logger.info("I'm a Kafka Producer");

        // Create a Producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");

        // Create the Producer

        // send data

        // flush and close producer
    }
}
