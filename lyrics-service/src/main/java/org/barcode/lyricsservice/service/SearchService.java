package org.barcode.lyricsservice.service;


import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.google.gson.Gson;
import org.barcode.lyricsservice.configuration.ElasticClient;
import org.barcode.lyricsservice.dto.SearchDTO;
import org.barcode.lyricsservice.model.Lyrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@Service
public class SearchService {

    private ElasticClient client;
    private StreamBridge bridge;

    @Autowired
    public SearchService(ElasticClient client, StreamBridge bridge) {
        this.client = client;
        this.bridge = bridge;
    }

    public List<Lyrics> search(List<SearchDTO> searchTerms) {
        //bridge.setAsync(true);

        List<Query> queries = searchTerms.stream().map(this::determineType).toList();

        try {
            return client.getClient().search(s -> s
                            .index("song-lyrics")
                            .query(q ->
                                    q.bool(b -> b.must(queries.stream().filter(Objects::nonNull).toList()))), Lyrics.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(this::mapHitToLyrics)
                    .toList();
        } catch (IOException e) {
            throw new IllegalArgumentException("Malformed search Object, or failing Elasticsearch server.");
        }
    }

    private Query determineType(SearchDTO searchDTO) {
        bridge.send("output", mapToMessage(searchDTO));
        return switch (searchDTO.getType()) {
            case "term" -> TermQuery.of(t -> t
                    .field(searchDTO.getField())
                    .caseInsensitive(true)
                    .value((String) searchDTO.getValue()))._toQuery();
            case "phrase-name" -> MatchPhraseQuery.of(m -> m
                    .field(searchDTO.getField())
                    .query((String) searchDTO.getValue()))._toQuery();
            case "phrase-lyrics" -> MatchPhraseQuery.of(m -> m
                    .query((String) searchDTO.getValue())
                    .field(searchDTO.getField())
                    .slop(2))._toQuery();
            default -> null;
        };

    }

    private Lyrics mapHitToLyrics(Hit<Lyrics> lyricsHit) {
        return lyricsHit.source()
                .setNames(null)
                .setGenre(null)
                .setId(lyricsHit.id());
    }

    private Message<String> mapToMessage(SearchDTO searchDTO) {
        Gson gson = new Gson();
        return MessageBuilder
                .withPayload(gson.toJson(searchDTO))
                .setHeader(KafkaHeaders.KEY, String.valueOf(searchDTO.getValue()))
                .build();
    }
}
