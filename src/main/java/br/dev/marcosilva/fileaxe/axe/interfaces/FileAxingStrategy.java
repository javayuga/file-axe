package br.dev.marcosilva.fileaxe.axe.interfaces;

public interface FileAxingStrategy {
    void processChunk(String identifier, Integer sequential, byte[] chunk);
}
