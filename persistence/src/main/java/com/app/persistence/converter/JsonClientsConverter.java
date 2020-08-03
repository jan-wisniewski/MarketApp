package com.app.persistence.converter;

import com.app.persistence.converter.generic.JsonConverter;
import com.app.persistence.model.Client;

import java.util.List;

public class JsonClientsConverter extends JsonConverter<List<Client>> {
    public JsonClientsConverter(String filename) {
        super(filename);
    }
}
