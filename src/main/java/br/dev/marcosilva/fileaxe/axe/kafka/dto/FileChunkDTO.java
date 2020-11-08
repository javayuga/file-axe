package br.dev.marcosilva.fileaxe.axe.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileChunkDTO {
    String identifier;
    String sequential;
    byte[] content;

    public String toString(){
        return String.format("[identifier=\"%s\", sequential=\"%s\", content.length=%d",
                identifier, sequential, content.length);
    }
}
