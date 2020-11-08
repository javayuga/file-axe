package br.dev.marcosilva.fileaxe.axe.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fileaxe")
public class FileAxeConfigurations {
    private Long chunkSize;
}
