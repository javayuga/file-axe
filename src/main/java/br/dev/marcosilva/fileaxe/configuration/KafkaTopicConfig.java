package br.dev.marcosilva.fileaxe.configuration;

import br.dev.marcosilva.fileaxe.axe.kafka.properties.KafkaFileAxeProperties;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Autowired
    KafkaProperties kafkaProperties;

    @Autowired
    KafkaFileAxeProperties kafkaFileAxeProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic fileChunkTopic() {
        return new NewTopic(kafkaFileAxeProperties.getFileChunks(), 1, (short) 1);
    }
}
