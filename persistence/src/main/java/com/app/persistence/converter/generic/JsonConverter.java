package com.app.persistence.converter.generic;

import com.app.persistence.exceptions.JsonConversionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {
    private final String filename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverter(String filename) {
        this.filename = filename;
    }

    public void toJson(final T element) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            if (element == null) {
                throw new NullPointerException("Element is null");
            }
            gson.toJson(element, fileWriter);
        } catch (Exception e) {
            throw new JsonConversionException(e.getMessage());
        }
    }

    public Optional<T> fromJson() {
        try (FileReader fileReader = new FileReader(filename)) {
            return Optional.of(gson.fromJson(fileReader, type));
        } catch (Exception e) {
            throw new JsonConversionException(e.getMessage());
        }
    }
}