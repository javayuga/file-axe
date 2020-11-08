package br.dev.marcosilva.fileaxe.axe.fs;

import br.dev.marcosilva.fileaxe.axe.interfaces.FileAxingStrategy;
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
    public void processChunk(String bucketIdentifier, Integer sequential, byte[] chunk) {
        String csvPath = bucketIdentifier+"/"+String.format("%04d", sequential)+".csv";
        fileSystemStorageService.store(csvPath, chunk);

    }
}
