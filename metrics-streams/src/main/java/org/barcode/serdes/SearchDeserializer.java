package org.barcode.serdes;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.barcode.dto.SearchDto;

import java.nio.charset.StandardCharsets;

public class SearchDeserializer implements Deserializer<SearchDto> {
    private final Gson gson = new Gson();

    @Override
    public SearchDto deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gson.fromJson(
                new String(bytes, StandardCharsets.UTF_8), SearchDto.class);
    }
}
