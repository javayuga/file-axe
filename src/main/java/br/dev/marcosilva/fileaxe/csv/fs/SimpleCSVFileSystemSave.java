package br.dev.marcosilva.fileaxe.csv.fs;

import br.dev.marcosilva.fileaxe.interfaces.FileAxingStrategy;
import br.dev.marcosilva.fileaxe.storage.FileSystemStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleCSVFileSystemSave implements FileAxingStrategy {
    @Autowired
    FileSystemStorageService fileSystemStorageService;

    @Override
    public void processChunk(String bucketIdentifier, String sequential, byte[] chunk) {
        String csvPath = bucketIdentifier+"/"+sequential+".csv";
        fileSystemStorageService.store(csvPath, chunk);

    }
}
