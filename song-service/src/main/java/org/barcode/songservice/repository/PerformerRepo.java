package org.barcode.songservice.repository;

import org.barcode.songservice.model.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformerRepo extends JpaRepository<Performer,Integer> {
}
