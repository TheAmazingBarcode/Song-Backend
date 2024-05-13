package org.barcode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.barcode.dto.LyricsDto;
import org.barcode.dto.SearchDto;
import org.barcode.serdes.SearchDtoSerdes;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SearchTermsTopology {

    public static SearchTermsTopology instance;
    private StreamsBuilder builder;

    private KafkaStreams streams;

    private final Type typeOfLyricsList = new TypeToken<List<LyricsDto>>() {}.getType();

    private SearchTermsTopology() {
    }

    public static SearchTermsTopology getInstance() {
        if (instance == null) {
            instance = new SearchTermsTopology();
            instance.buildTopology();
            return instance;
        }
        return instance;
    }

    public void startStream() {
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }

    private void buildTopology() {
        this.builder = new StreamsBuilder();
        searchLogs();
        Topology topology = this.builder.build();
        this.streams = new KafkaStreams(topology, config());
    }

    private Properties config() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "search-analyzer");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return config;
    }


    private void searchLogs() {
        Gson gson = new Gson();
        builder.stream("search-logs"
                        , Consumed.with(Serdes.String(), new SearchDtoSerdes()))
                .filter((k, v) -> v.getType().equals("phrase-lyrics") && k.split(" ").length >= 3)
                .groupByKey()
                .count()
                .filter((k,v) -> v >= 10)
                .mapValues((k,v) -> popularSongIds(k))
                .toStream()
                .flatMapValues(list-> list)
                .selectKey((k,v) -> v.getId())
                .mapValues((k,v)-> gson.toJson(v))
                .to("popular-songs",Produced.with(Serdes.String(),Serdes.String()));
    }

    private List<LyricsDto> popularSongIds(String value) {
        Gson gson = new Gson();
        SearchDto searchDto = new SearchDto();

        searchDto.setType("phrase");
        searchDto.setField("lyrics");
        searchDto.setValue(value);
        List<SearchDto> searchDtos = new ArrayList<>();
        searchDtos.add(searchDto);

        HttpRequest request;
        try {

            request = HttpRequest.newBuilder()
                    .uri(new URI("https://localhost:7777/lyrics/category"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(searchDtos)))
                    .header("Content-Type","application/json")
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String response = future.thenApply(HttpResponse::body).get();
            return  gson.fromJson(response,typeOfLyricsList);
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
