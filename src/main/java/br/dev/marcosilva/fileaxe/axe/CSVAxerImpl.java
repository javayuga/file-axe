package br.dev.marcosilva.fileaxe.axe;

import br.dev.marcosilva.fileaxe.axe.interfaces.FileAxer;
import br.dev.marcosilva.fileaxe.axe.interfaces.FileAxingStrategy;
import br.dev.marcosilva.fileaxe.axe.interfaces.FilePreAxingStrategy;
import lombok.Builder;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Setter
@Builder
public class CSVAxerImpl implements FileAxer {
    private FilePreAxingStrategy filePreAxingStrategy;
    private FileAxingStrategy fileAxingStrategy;
    private Long chunkSize;

    @Override
    public void processStream(String sourceName, InputStream inputStream) throws IOException {
        String bucketIdentifier = filePreAxingStrategy.runPreAxingOperations(sourceName);

        StringBuilder content = new StringBuilder();
        String header, line;

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(inputStream));

        if ((header = reader.readLine()) != null){
            content.append(header);
            content.append(System.lineSeparator());
        }

        Integer sequential = 0;
        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());

            if (content.toString().length()>=chunkSize){
                fileAxingStrategy.processChunk(
                        bucketIdentifier, sequential, content.toString().getBytes());

                sequential++;
                content = new StringBuilder();
                content.append(header);
                content.append(System.lineSeparator());
            }

        }

        if (content.toString().length()>=0) {
            fileAxingStrategy.processChunk(
                    bucketIdentifier, sequential, content.toString().getBytes());
        }




        }

}
