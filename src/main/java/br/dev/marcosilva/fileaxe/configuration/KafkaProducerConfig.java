package br.dev.marcosilva.fileaxe.configuration;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.TransactionSupport;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaProducerConfig {

    private KafkaProperties properties;

    public KafkaProducerConfig(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.kafka.producer.transaction-id-prefix")
    public KafkaTransactionManager<?, ?> kafkaTransactionManager() {
        KafkaTransactionManager<?, ?> ktm = new KafkaTransactionManager(kafkaProducerFactory());
        ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
        return ktm;
    }

    @Bean(name = "kafkaProducerFactory")
    @Primary
    public ProducerFactory<?, ?> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<?, ?> factory = new CustomProducerFactory(this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    @Bean(name = "kafkaNonTransactionalProducerFactory")
    public ProducerFactory<?, ?> kafkaNonTransactionalProducerFactory() {
        return new CustomProducerFactory(this.properties.buildProducerProperties());
    }

    @Bean(name = "kafkaTemplate")
    @Primary
    public KafkaTemplate<?, ?> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    @Bean(name = "kafkaNonTransactionalTemplate")
    public KafkaTemplate<?, ?> kafkaNonTransactionalTemplate() {
        return new KafkaTemplate<>(kafkaNonTransactionalProducerFactory());
    }

    static class CustomProducerFactory extends DefaultKafkaProducerFactory<String, String> {
        public CustomProducerFactory(Map<String, Object> configs) {
            super(configs);
        }

        @Override
        public Producer<String, String> createProducer() {
            TransactionSupport.setTransactionIdSuffix(String.valueOf(System.currentTimeMillis()));
            return super.createProducer();
        }
    }
}
