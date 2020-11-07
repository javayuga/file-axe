package br.dev.marcosilva.fileaxe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class FileAxeApp {
    public static void main(String[] args) {
        SpringApplication.run(FileAxeApp.class, args);
    }

}
