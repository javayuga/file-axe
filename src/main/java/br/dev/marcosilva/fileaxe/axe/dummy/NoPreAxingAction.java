package br.dev.marcosilva.fileaxe.axe.dummy;

import br.dev.marcosilva.fileaxe.axe.interfaces.FilePreAxingStrategy;
import org.springframework.stereotype.Component;

@Component
public class NoPreAxingAction implements FilePreAxingStrategy {
    @Override
    public String runPreAxingOperations(String sourceName) {
        return sourceName;
    }
}
