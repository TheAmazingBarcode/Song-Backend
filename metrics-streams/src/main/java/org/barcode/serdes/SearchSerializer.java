package org.barcode.serdes;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;
import org.barcode.dto.SearchDto;

import java.nio.charset.StandardCharsets;

public class SearchSerializer implements Serializer<SearchDto> {
    private final Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, SearchDto searchDto) {
        if (searchDto == null) return null;
        return gson.toJson(searchDto).getBytes(StandardCharsets.UTF_8);
    }
}
