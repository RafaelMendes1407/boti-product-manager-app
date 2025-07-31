package com.boti.productmanagerapp.adapters.out.batchprocess;

import com.boti.productmanagerapp.application.ports.out.ReadProductFile;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ReadProductFileImpl implements ReadProductFile {

    private static final Logger log = LoggerFactory.getLogger(ReadProductFileImpl.class);

    @Override
    public void readFile(File jsonFile) {
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(jsonFile)) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                // Lidar com um único objeto ou outro formato
                System.out.println("O arquivo JSON não começa com um array.");
                return;
            }

            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.currentToken() == JsonToken.START_OBJECT) {
                    // Aqui você processaria um objeto JSON.
                    // Para um objeto simples como {"id": 1, "nome": "Exemplo"}, você pode ler campo por campo.
                    // Exemplo:
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = parser.getCurrentName();
                        if (fieldName != null) {
                            parser.nextToken(); // Move para o valor do campo
                            switch (fieldName) {
                                case "id":
                                    int id = parser.getIntValue();
                                    // Faça algo com o ID
                                    break;
                                case "nome":
                                    String nome = parser.getText();
                                    // Faça algo com o nome
                                    break;
                                // ... outros campos
                            }
                        }
                    }
                    // Neste ponto, você tem um objeto JSON totalmente lido e pode passá-lo para o serviço de inserção no banco de dados.
                    // Exemplo: YourDatabaseService.insertRecord(new YourRecordObject(id, nome));
                }
            }
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}