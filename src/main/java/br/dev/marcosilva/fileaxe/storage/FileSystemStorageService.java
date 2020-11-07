package br.dev.marcosilva.fileaxe.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

import br.dev.marcosilva.fileaxe.storage.configuration.StorageProperties;
import br.dev.marcosilva.fileaxe.storage.exception.StorageException;
import br.dev.marcosilva.fileaxe.storage.exception.StorageFileNotFoundException;
import br.dev.marcosilva.fileaxe.storage.interfaces.StorageFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Component
public class FileSystemStorageService implements StorageFacade {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = getPathRelativeToRootLocation(file.getOriginalFilename());

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void createFolderAtRoot(String path) {
        try {
            Files.createDirectories(getPathRelativeToRootLocation(path));
        }
        catch (IOException e) {
            throw new StorageException(String.format("Could not create %s", path), e);
        }

    }



    @Override
    public void store(String filePath, byte[] content) {
        try{
            Path destinationFile = getPathRelativeToRootLocation(filePath);

            Files.write(destinationFile, content, StandardOpenOption.CREATE);

        } catch (IOException e) {
            throw new StorageFileNotFoundException("Could not write file: " + filePath, e);
        }

    }

    private Path getPathRelativeToRootLocation(String filePath) {
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(filePath))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().startsWith(this.rootLocation.toAbsolutePath())) {
            throw new StorageException(
                    "Cannot store file outside current directory.");
        }
        return destinationFile;
    }
}
