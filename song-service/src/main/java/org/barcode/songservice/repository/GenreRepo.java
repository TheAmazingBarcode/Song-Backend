package org.barcode.songservice.repository;

import org.barcode.songservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends JpaRepository<Genre,Integer> {
    Genre findByName(String name);
}
