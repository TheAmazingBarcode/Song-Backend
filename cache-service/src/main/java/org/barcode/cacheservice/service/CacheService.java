package org.barcode.cacheservice.service;

import io.confluent.ksql.api.client.BatchedQueryResult;
import io.confluent.ksql.api.client.Row;
import org.barcode.cacheservice.client.ClientWrapper;
import org.barcode.cacheservice.model.LyricsDto;
import org.barcode.cacheservice.repository.RedisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

@Service
public class CacheService {

    private final ClientWrapper clientWrapper;
    private final WebClient.Builder webClient;
    private final RedisRepo repo;

    @Autowired
    public CacheService(ClientWrapper clientWrapper, WebClient.Builder webClient,RedisRepo repo) {
        this.clientWrapper = clientWrapper;
        this.webClient = webClient;
        this.repo = repo;
    }

    @Async
    public void pollKsqlDb() {
        BatchedQueryResult result = clientWrapper.getClient().executeQuery("SELECT * FROM POPULAR_QUERY;");
        try {
            List<LyricsDto> lyrics = result.get().stream().map(this::mapRowToLyrics).toList();
            writeToRedis(lyrics);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private LyricsDto mapRowToLyrics(Row row) {
        return new LyricsDto(row.getString(1), row.getString(2), row.getString(3));
    }

    private void writeToRedis(List<LyricsDto> lyricsDtos){
        repo.saveAll(lyricsDtos);
    }

    public List<LyricsDto> getAllSongs() {
        return StreamSupport.stream(repo.findAll().spliterator(),true).toList();
    }
}
