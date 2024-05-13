package org.barcode.cacheservice.repository;


import org.barcode.cacheservice.model.LyricsDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepo extends CrudRepository<LyricsDto,Integer> {
}
