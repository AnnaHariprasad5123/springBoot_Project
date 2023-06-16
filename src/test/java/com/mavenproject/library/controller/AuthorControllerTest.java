package com.mavenproject.library.controller;

import com.mavenproject.library.dto.AuthorDTO;
import com.mavenproject.library.entity.Author;
import com.mavenproject.library.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllAuthors_ReturnsListOfAuthorDTOs() {
        List<Author> authors = Arrays.asList(
                new Author(1L, "John", "Doe"),
                new Author(2L, "Jane", "Smith")
        );

        when(authorService.getAllAuthors()).thenReturn(authors);
        when(modelMapper.map(any(Author.class), eq(AuthorDTO.class))).thenReturn(new AuthorDTO());

        ResponseEntity<List<AuthorDTO>> response = authorController.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAuthorById_WithValidId_ReturnsAuthorDTO() {
        Long authorId = 1L;
        Author author = new Author(authorId, "John", "Doe");
        AuthorDTO authorDTO = new AuthorDTO(authorId, "John", "Doe");
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.of(author));
        when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        ResponseEntity<AuthorDTO> response = authorController.getAuthorById(authorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authorDTO, response.getBody());
    }

    @Test
    void getAuthorById_WithInvalidId_ReturnsNotFound() {
        Long authorId = 1L;
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.empty());

        ResponseEntity<AuthorDTO> response = authorController.getAuthorById(authorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createAuthor_ReturnsCreatedAuthorDTO() {
        AuthorDTO authorDTO = new AuthorDTO(1L, "John", "Doe");
        Author author = new Author(1L, "John", "Doe");
        when(modelMapper.map(authorDTO, Author.class)).thenReturn(author);
        when(authorService.saveAuthor(author)).thenReturn(author);
        when(modelMapper.map(author, AuthorDTO.class)).thenReturn(authorDTO);

        ResponseEntity<AuthorDTO> response = authorController.createAuthor(authorDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(authorDTO, response.getBody());
    }

    @Test
    void updateAuthor_WithExistingAuthor_ReturnsUpdatedAuthorDTO() {
        Long authorId = 1L;
        AuthorDTO updatedAuthorDTO = new AuthorDTO(authorId, "Updated", "Author");
        Author updatedAuthor = new Author(authorId, "Updated", "Author");
        when(modelMapper.map(updatedAuthorDTO, Author.class)).thenReturn(updatedAuthor);
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.of(new Author()));
        when(authorService.saveAuthor(updatedAuthor)).thenReturn(updatedAuthor);
        when(modelMapper.map(updatedAuthor, AuthorDTO.class)).thenReturn(updatedAuthorDTO);

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(authorId, updatedAuthorDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAuthorDTO, response.getBody());
    }

    @Test
    void updateAuthor_WithNonexistentAuthor_ReturnsNotFound() {
        Long authorId = 1L;
        AuthorDTO updatedAuthorDTO = new AuthorDTO(authorId, "Updated", "Author");
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.empty());

        ResponseEntity<AuthorDTO> response = authorController.updateAuthor(authorId, updatedAuthorDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAuthor_WithExistingAuthor_ReturnsNoContent() {
        Long authorId = 1L;
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.of(new Author()));

        ResponseEntity<Void> response = authorController.deleteAuthor(authorId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authorService, times(1)).deleteAuthor(authorId);
    }

    @Test
    void deleteAuthor_WithNonexistentAuthor_ReturnsNotFound() {
        Long authorId = 1L;
        when(authorService.getAuthorById(authorId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = authorController.deleteAuthor(authorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorService, never()).deleteAuthor(authorId);
    }
}
