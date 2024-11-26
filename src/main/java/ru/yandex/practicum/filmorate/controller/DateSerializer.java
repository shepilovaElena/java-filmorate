package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;

import java.time.format.DateTimeParseException;

import static java.util.Objects.isNull;

@JsonComponent
public class DateSerializer {

    @PostConstruct
    public void init() {
        System.err.println("Serializer has been initialized");
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (!isNull(date)) {
                jsonGenerator.writeString(date.toString());
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String date = jsonParser.getText();
            if (date.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(date);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Error while parsing date", ex);
            }
        }
    }
}

