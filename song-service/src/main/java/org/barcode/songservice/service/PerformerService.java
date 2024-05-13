package org.barcode.songservice.service;

import lombok.NoArgsConstructor;
import org.barcode.songservice.model.Performer;
import org.barcode.songservice.repository.PerformerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@NoArgsConstructor
public class PerformerService {
    public PerformerRepo performerRepo;

    @Autowired
    public PerformerService(PerformerRepo performerRepo) {
        this.performerRepo = performerRepo;
    }

    public List<Performer> getAllPerformers(){
        return performerRepo.findAll();
    }

    public Performer newPerformer(Performer performer){
        return performerRepo.save(performer);
    }
}
