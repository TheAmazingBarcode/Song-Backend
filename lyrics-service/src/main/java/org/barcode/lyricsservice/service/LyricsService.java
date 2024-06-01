package org.barcode.lyricsservice.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.barcode.lyricsservice.configuration.ElasticClient;
import org.barcode.lyricsservice.dto.SearchDTO;
import org.barcode.lyricsservice.model.Lyrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class LyricsService {
    private final ElasticClient client;

    @Autowired
    public LyricsService(ElasticClient client) {
        this.client = client;
    }

    public List<Lyrics> getAllLyrics() throws IOException {
        SearchResponse<Lyrics> searchResponse = client.getClient()
                .search(s -> s
                        .index("song-lyrics")
                        .query(QueryBuilders.matchAll().build()._toQuery()), Lyrics.class);

        return searchResponse.hits().hits().stream().map(this::mapHitToLyrics).toList();
    }

    private Lyrics mapHitToLyrics(Hit<Lyrics> lyricsHit) {
        return lyricsHit.source()
                .setNames(null)
                .setGenre(null)
                .setId(lyricsHit.id());
    }

    public Lyrics getLyricsByID(String lyricsID) {
        try {
            GetResponse<Lyrics> getResponse;

            getResponse = client.getClient().get(g -> g
                    .index("song-lyrics")
                    .id(lyricsID), Lyrics.class);

            if (getResponse.source() == null)
                return Lyrics.builder()
                        .id("unknown")
                        .name("unknown")
                        .genre("unknown")
                        .build();


            return getResponse.source()
                    .setGenre(null)
                    .setNames(null)
                    .setId(getResponse.id());

        } catch (IOException e) {
            log.error("Error with elasticsearch, check the health or if it is running");
            return null;
        }
    }

    public String createLyrics(Lyrics lyrics) {
        try {
            IndexResponse response;

            response = client.getClient().index(index -> index
                    .index("song-lyrics")
                    .document(lyrics));

            Gson gson = new Gson();
            return gson.fromJson(response.id(), String.class);
        } catch (IOException e) {
            log.error("Error with elasticsearch, check the health or if it is running");
            return null;
        }
    }

    public Lyrics updateLyrics(Lyrics lyrics) {
        try {
            System.out.println(client.getClient()
                            .update(u -> u.index("song-lyrics")
                                    .id(lyrics.getId())
                                    .doc(lyrics)
                                    , Lyrics.class)
                            .get());
            return Lyrics.builder().id(lyrics.getId()).build();

        } catch (IOException e) {
            log.error("Error with elasticsearch, check the health or if it is running");
            return null;
        }

    }

    public Boolean deleteLyrics(String id) {
        try {
            return client.getClient().delete(d -> d
                            .index("song-lyrics")
                            .id(id))
                    .result()
                    .toString()
                    .equalsIgnoreCase("deleted");
        } catch (IOException e) {
            log.error("Error with elasticsearch, check the health or if it is running");
            return false;
        }
    }

    public List<Lyrics> getLyricsByCriteria(List<SearchDTO> searchDTOList) {
        try {
            List<Query> queries = searchDTOList.stream().map(this::formQuery).toList();
            return client.getClient()
                    .search(s -> s
                            .index("song-lyrics")
                            .query(q -> q.bool
                                    (b -> b.must(queries.stream().filter(Objects::nonNull).toList()))), Lyrics.class)
                    .hits()
                    .hits()
                    .stream()
                    .map(this::mapHitToLyrics)
                    .toList();
        } catch (IOException e) {
            log.error("Error with elasticsearch, check the health or if it is running");
            return null;
        }
    }

    private Query formQuery(SearchDTO searchDTO) {
        return switch (searchDTO.getType()) {
            case "term" -> TermQuery.of(t -> t
                    .field(searchDTO.getField())
                    .caseInsensitive(true)
                    .value((String) searchDTO.getValue()))._toQuery();
            case "phrase" -> MatchPhraseQuery.of(m -> m
                    .query((String) searchDTO.getValue())
                    .field(searchDTO.getField())
                    .slop(2))._toQuery();
            default -> null;
        };
    }
}
