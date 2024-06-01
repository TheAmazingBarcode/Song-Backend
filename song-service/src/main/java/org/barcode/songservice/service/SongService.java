package org.barcode.songservice.service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.barcode.songservice.dto.LyricsDTO;
import org.barcode.songservice.dto.SongDTO;
import org.barcode.songservice.model.*;
import org.barcode.songservice.repository.GenreRepo;
import org.barcode.songservice.repository.ParticipationRepo;
import org.barcode.songservice.repository.SongRepo;
import org.barcode.songservice.repository.SongWriterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class SongService {
    private SongRepo songRepo;
    private SongWriterRepo songWriterRepo;
    private ParticipationRepo participationRepo;
    private WebClient.Builder webClientBuilder;
    private GenreRepo genreRepo;

    @Autowired
    public SongService(SongRepo songRepo, SongWriterRepo songWriterRepo, ParticipationRepo repo, WebClient.Builder webClientBuilder, GenreRepo genreRepo) {
        this.songRepo = songRepo;
        this.songWriterRepo = songWriterRepo;
        this.participationRepo = repo;
        this.webClientBuilder = webClientBuilder;
        this.genreRepo = genreRepo;
    }

    @Transactional
    public SongDTO newSong(SongDTO songDTO) {

        Set<SongWriter> authors = extractSongWriters(songDTO);

        Set<Participation> performers = extractParticipations(songDTO);

        List<String> names = extractNames(songDTO);

        //Talk to the Lyrics service which uses ElasticSearch to store large text objects.

        String lyricsId = createLyricsObject(transformSong(songDTO));

        Song song = Song.builder()
                .id(songDTO.getId())
                .genre(songDTO.getGenre())
                .name(songDTO.getName())
                .authors(authors)
                .performers(performers)
                .lyricsId(lyricsId)
                .build();

        Song copy = songRepo.save(song);

        authors.forEach(author -> author.setSongWriterSource(copy));
        performers.forEach(performer -> performer.setSongPerformerSource(copy));

        songWriterRepo.saveAll(authors);
        participationRepo.saveAll(performers);

        return mapSongToDTO(copy);

    }

    private List<String> extractNames(SongDTO songDTO) {
        List<String> names = new ArrayList<>();
        songDTO.getAuthors().forEach(author -> names.add(author.getFirstName() + " " + author.getLastName()));
        songDTO.getPerformers().forEach(performer -> {
            names.add(performer.getArtistName());
            names.add(performer.getFirstName() + " " + performer.getLastName());
        });
        return names;
    }


    ///!!! NOT ASYNC
    private String createLyricsObject(LyricsDTO dto) {
        return webClientBuilder.build().post()
                .uri("http://lyrics-service/lyrics/new")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private LyricsDTO getLyricsObject(String lyricsID) {
        return webClientBuilder.build().get()
                .uri("http://lyrics-service/lyrics/id/" + lyricsID)
                .retrieve()
                .bodyToMono(LyricsDTO.class)
                .block();
    }

    private LyricsDTO transformSong(SongDTO songDTO) {
        return LyricsDTO.builder()
                .id(songDTO.getLyricsID())
                .lyrics(songDTO.getLyrics())
                .name(songDTO.getName())
                .names(extractNames(songDTO))
                .genre(songDTO.getGenre().getName())
                .build();
    }

    private Set<SongWriter> extractSongWriters(SongDTO songDTO) {
        return songDTO.getAuthors().stream().map(author -> SongWriter
                .builder()
                .songWriter(author)
                .build()).collect(Collectors.toSet());
    }

    private Set<Participation> extractParticipations(SongDTO songDTO) {
        return songDTO.getPerformers().stream().map(performer -> Participation
                .builder()
                .songPerformer(performer)
                .build()).collect(Collectors.toSet());
    }

    public List<SongDTO> getAllSongs() {
        return songRepo.findAll().stream().map(this::mapSongToDTO).collect(Collectors.toList());
    }

    public SongDTO mapSongToDTO(Song song) {
        return SongDTO.builder()
                .name(song.getName())
                .id(song.getId())
                .genre(song.getGenre())
                .authors(song.getAuthors().stream().map(SongWriter::getSongWriter).collect(Collectors.toSet()))
                .performers(song.getPerformers().stream().map(Participation::getSongPerformer).collect(Collectors.toSet()))
                .lyricsID(song.getLyricsId())
                .build();
    }

    @Transactional
    public SongDTO updateSong(SongDTO songDTO) {
        Song song = Song.builder()
                .id(songDTO.getId())
                .name(songDTO.getName())
                .lyricsId(songDTO.getLyricsID())
                .genre(songDTO.getGenre())
                .build();

        LyricsDTO lyricsDTO = transformSong(songDTO);

        webClientBuilder.build().put()
                .uri("http://lyrics-service/lyrics/update")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(lyricsDTO)
                .retrieve()
                .bodyToMono(LyricsDTO.class)
                .block();

        Song copy = songRepo.save(song);


        //This is probably a bad idea, but Many to Many with Hibernate is not a great experience
        songWriterRepo.deleteSongWritersBySongWriterSource(copy);
        participationRepo.deleteParticipationsBySongPerformerSource(copy);

        copy.setAuthors(extractSongWriters(songDTO));
        copy.setPerformers(extractParticipations(songDTO));

        copy.getAuthors().forEach(songWriter -> songWriter.setSongWriterSource(copy));
        copy.getPerformers().forEach(participation -> participation.setSongPerformerSource(copy));

        songWriterRepo.saveAll(copy.getAuthors());
        participationRepo.saveAll(copy.getPerformers());

        return mapSongToDTO(copy);
    }

    @Transactional
    public Boolean deleteSong(Integer id) {
        if (!songRepo.existsById(id)) {
            return false;
        }
        Song song = songRepo.findById(id).get();
        participationRepo.deleteParticipationsBySongPerformerSource(song);
        songWriterRepo.deleteSongWritersBySongWriterSource(song);
        songRepo.deleteById(id);

        webClientBuilder.build().delete()
                .uri("http://lyrics-service/lyrics/delete/" + song.getLyricsId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        return true;
    }

    public List<SongDTO> getSongsByGenre(String name) {
        return songRepo.findSongsByGenre(genreRepo.findByName(name)).stream().map(this::mapSongToDTO).toList();
    }

    public List<SongDTO> getSongsByAuthor(Integer id) {
        List<SongWriter> songWriters = songWriterRepo.findSongWritersBySongWriter(Author.builder().id(id).build());
        return songWriters.stream().map(SongWriter::getSongWriterSource).map(this::mapSongToDTO).toList();
    }

    public List<SongDTO> getSongsByPerformer(Integer id) {
        List<Participation> participations = participationRepo.findParticipationsBySongPerformer(Performer.builder().id(id).build());
        return participations.stream().map(Participation::getSongPerformerSource).map((this::mapSongToDTO)).toList();
    }

    public List<SongDTO> searchSongs(List<String> ids) {
        return songRepo.findSongsByLyricsIds(ids).stream().map(this::mapSongToDTO).toList();
    }
}
