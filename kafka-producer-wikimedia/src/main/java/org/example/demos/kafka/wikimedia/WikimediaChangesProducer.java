package org.example.demos.kafka.wikimedia;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class WikimediaChangesProducer {

    public static void main(String[] args) throws InterruptedException {

        /**
         * ➜  ~ kafka-topics.sh --bootstrap-server localhost:9092 --topic wikimedia.recentchange --create --partitions 3 --replication-factor 1
         */

        /**
         * 1. create producer
         * 2. 토픽 정의
         * 3. 핸들러 정의
         * 4. EventHandler와 url을 이용해 EventSource 생성
         *    - EventSource는 스트림에서 오는 이벤트를 처리하는 역할
         * 5. EventSource 시작
         *    - 독립된 thread에서 실행
         * 6. 10분간 프로듀서 실행
         *   - TimeUnit.MINUTES.sleep(10);
         *   - 10분간 프로듀서 실행 후 종료
         *   - 종료되면 프로듀서가 종료되고, EventSource도 종료됨
         *   - EventSource는 종료되면 자동으로 스트림을 닫음
         *   - 스트림이 닫히면 onClosed() 메서드가 호출됨
         *   - onClosed() 메서드에서 프로듀서를 닫음
         *   - 프로듀서가 닫히면 스트림에서 오는 이벤트를 처리하는 핸들러도 닫힘
         */


        String bootstrapServers = "localhost:9092";
        // create producer properties

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // create the producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        String topic = "wikimedia.recentchange";

        // TODO 스트림에서 오는 이벤트를 처리해 프로듀서로 전송하는 역할
        EventHandler handler = new WikimediaChangeHandler(producer, topic);

        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(handler, URI.create(url));
        EventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);

    }
}
