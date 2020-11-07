package br.dev.marcosilva.fileaxe.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface FileAxer {
    void processStream(String sourceName, InputStream inputStream) throws IOException;
}
