package br.dev.marcosilva.fileaxe.interfaces;

public interface FileAxingStrategy {
    void processChunk(String identifier, String sequential, byte[] chunk);
}
