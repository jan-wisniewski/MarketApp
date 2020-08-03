package com.app.persistence.converter;

import com.app.persistence.converter.generic.JsonConverter;
import com.app.persistence.model.Purchase;

import java.util.List;

public class JsonPurchasesConverter extends JsonConverter<List<Purchase>> {
    public JsonPurchasesConverter(String filename) {
        super(filename);
    }
}
