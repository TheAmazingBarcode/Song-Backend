package org.barcode.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.barcode.dto.SearchDto;

public class SearchDtoSerdes implements Serde<SearchDto> {
    @Override
    public Serializer<SearchDto> serializer() {
        return new SearchSerializer();
    }

    @Override
    public Deserializer<SearchDto> deserializer() {
        return new SearchDeserializer();
    }
}
