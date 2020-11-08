package br.dev.marcosilva.fileaxe.axe.kafka;


import br.dev.marcosilva.fileaxe.axe.kafka.dto.FileChunkDTO;
import br.dev.marcosilva.fileaxe.axe.interfaces.FileAxingStrategy;
import br.dev.marcosilva.fileaxe.axe.kafka.properties.KafkaFileAxeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class SimpleKafkaTopicPost implements FileAxingStrategy {

    @Autowired
    KafkaFileAxeProperties kafkaFileAxeProperties;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Override
    public void processChunk(String identifier, Integer sequential, byte[] chunk) {
        FileChunkDTO dto = new FileChunkDTO(identifier, String.format("%04d", sequential), chunk);

        ListenableFuture<SendResult<String, FileChunkDTO>> future = kafkaTemplate.send(
                kafkaFileAxeProperties.getFileChunks(),
                sequential%kafkaFileAxeProperties.getNumPartitions(),
                identifier,
                dto);

        future.addCallback(new ListenableFutureCallback<SendResult<String, FileChunkDTO>>() {

            @Override
            public void onSuccess(SendResult<String, FileChunkDTO> result) {
                log.info("Sent message=[" + result + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[" + dto + "] due to : " + ex.getMessage());
            }
        });
    }
}
