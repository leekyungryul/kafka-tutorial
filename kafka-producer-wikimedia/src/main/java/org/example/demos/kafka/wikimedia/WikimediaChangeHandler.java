package org.example.demos.kafka.wikimedia;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;

/**
 * 스트림이 새로운 메시지를 발견할 때마다 호출되는 핸들러
 */
public class WikimediaChangeHandler implements EventHandler {

    private final Logger logger = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());

    KafkaProducer<String, String> producer;
    String topic;

    public WikimediaChangeHandler(KafkaProducer<String, String> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    /**
     * 스트림이 연결되면 호출되는 메서드
     * @throws Exception
     */
    @Override
    public void onOpen() {
        // nothing to do
    }

    /**
     * 스트림이 닫히면 호출되는 메서드
     * @throws Exception
     */
    @Override
    public void onClosed() {
        producer.close();
    }

    /**
     * http 스트림에서 온 메시지를 처리하는 메서드
     * @param event the event name, from the {@code event:} line in the stream
     * @param messageEvent a {@link MessageEvent} object containing all the other event properties
     * @throws Exception
     */
    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        logger.info("event: {}, data: {}", event, messageEvent.getData());
        // asynchronous
        producer.send(new ProducerRecord<>(topic, messageEvent.getData()));
    }

    /**
     * http 스트림에서 온 주석을 처리하는 메서드
     * @param comment the comment line
     * @throws Exception
     */
    @Override
    public void onComment(String comment) {
        // nothing to do
    }

    @Override
    public void onError(Throwable t) {
        logger.error("Error in Stream Reading : {}", t.getMessage());
    }
}
