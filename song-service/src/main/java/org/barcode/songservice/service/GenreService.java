package org.barcode.songservice.service;

import lombok.NoArgsConstructor;
import org.barcode.songservice.model.Genre;
import org.barcode.songservice.repository.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class GenreService {
    public GenreRepo genreRepo;

    @Autowired
    public GenreService(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public List<Genre> getAllGenres(){
        return genreRepo.findAll();
    }

    public Genre newGenre(Genre genre){
        return genreRepo.save(genre);
    }

    public Boolean deleteGenre(Integer id) {
        if(!genreRepo.existsById(id)){
            return false;
        }
        genreRepo.deleteById(id);
        return true;
    }
}
