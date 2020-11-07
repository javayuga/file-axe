package br.dev.marcosilva.fileaxe.csv.fs;

import br.dev.marcosilva.fileaxe.interfaces.FilePreAxingStrategy;
import br.dev.marcosilva.fileaxe.storage.FileSystemStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SimpleFSCreateTimeStampFolder implements FilePreAxingStrategy {
    private FileSystemStorageService fileSystemStorageService;

    public SimpleFSCreateTimeStampFolder(FileSystemStorageService fileSystemStorageService){
        this.fileSystemStorageService=fileSystemStorageService;
    }

    @Override
    public String runPreAxingOperations(String sourceName) {
        String bucketIdentifier = sourceName + "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss"));

        log.info("creating bucket {}", bucketIdentifier);
        fileSystemStorageService.createFolderAtRoot(bucketIdentifier);
        return bucketIdentifier;
    }

}
