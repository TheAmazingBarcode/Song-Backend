package org.barcode.songservice.service;

import lombok.NoArgsConstructor;
import org.barcode.songservice.model.Author;
import org.barcode.songservice.repository.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@NoArgsConstructor
public class AuthorService {
    private AuthorRepo authorRepo;

    @Autowired
    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public List<Author> getAllAuthors(){
        return authorRepo.findAll();
    }

    public Author newAuthor(Author author){
        return authorRepo.save(author);
    }
}
