package com.mavenProject.library.controller;

import com.mavenProject.library.dto.AuthorDTO;
import com.mavenProject.library.entity.Author;
import com.mavenProject.library.service.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorController(AuthorService authorService, ModelMapper modelMapper) {
        this.authorService = authorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        List<AuthorDTO> authorDTOs = authors.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(authorDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.getAuthorById(id);
        return author.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        Author author = dtoToEntity(authorDTO);
        Author createdAuthor = authorService.saveAuthor(author);
        AuthorDTO createdAuthorDTO = entityToDTO(createdAuthor);
        return new ResponseEntity<>(createdAuthorDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);
        if (existingAuthor.isPresent()) {
            Author author = dtoToEntity(authorDTO);
            author.setId(id);
            Author updatedAuthor = authorService.saveAuthor(author);
            AuthorDTO updatedAuthorDTO = entityToDTO(updatedAuthor);
            return new ResponseEntity<>(updatedAuthorDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);
        if (existingAuthor.isPresent()) {
            authorService.deleteAuthor(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Author dtoToEntity(AuthorDTO authorDTO) {
        return modelMapper.map(authorDTO, Author.class);
    }

    private AuthorDTO entityToDTO(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }
}
