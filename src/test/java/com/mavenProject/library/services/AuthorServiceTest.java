package com.mavenproject.library.services;

import com.mavenproject.library.dao.AuthorRepository;
import com.mavenproject.library.entity.Author;
import com.mavenproject.library.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1L, "John", "Doe"));
        authors.add(new Author(2L, "Jane", "Smith"));
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.getAllAuthors();

        assertEquals(2, result.size());
        assertEquals(authors,result);
        verify(authorRepository,times(1)).findAll();

    }

    @Test
    void testGetAuthorById() {
        Author author = new Author(1L, "John", "Doe");
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.getAuthorById(1L);

        assertTrue(result.isPresent());
        assertEquals(author, result.get());
        verify(authorRepository, times(1)).findById(1L);
    }


    @Test
    void testSaveAuthor() {
        Author author = new Author(1L, "John", "Doe");
        when(authorRepository.save(author)).thenReturn(author);

        Author result = authorService.saveAuthor(author);

        assertEquals(author, result);
        verify(authorRepository,times(1)).save(author);

    }

    @Test
    void testDeleteAuthor() {
        Long authorId = 1L;

        authorService.deleteAuthor(authorId);

        verify(authorRepository, times(1)).deleteById(authorId);
    }
}
