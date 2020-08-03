package com.app.persistence.converter;

import com.app.persistence.converter.generic.JsonConverter;
import com.app.persistence.model.Product;

import java.util.List;

public class JsonProductsConverter extends JsonConverter<List<Product>> {
    public JsonProductsConverter(String filename) {
        super(filename);
    }
}
