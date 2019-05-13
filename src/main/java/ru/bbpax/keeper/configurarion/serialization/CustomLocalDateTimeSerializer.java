package ru.bbpax.keeper.configurarion.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.bbpax.keeper.configurarion.serialization.CustomLocalDateTimeDeserializer.DATE_TIME_PATTERN;

public class CustomLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public CustomLocalDateTimeSerializer() {
        this(null);
    }

    public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value,
                          JsonGenerator jsonGen,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGen.writeString(formatter.format(value));
    }
}
