package com.mavenProject.library.controller;

import com.mavenProject.library.dto.BookDTO;
import com.mavenProject.library.entity.Book;
import com.mavenProject.library.service.BookService;
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

class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllBooks_ReturnsListOfBookDTOs() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", "Author 1"),
                new Book(2L, "Book 2", "Author 2")
        );

        when(bookService.getAllBooks()).thenReturn(books);
        when(modelMapper.map(any(Book.class), eq(BookDTO.class))).thenReturn(new BookDTO());

        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBookById_WithValidId_ReturnsBookDTO() {
        long bookId = 1L;
        Book book = new Book(bookId, "Book 1", "Author 1");
        BookDTO bookDTO = new BookDTO(bookId, "Book 1", "Author 1");
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
    }

    @Test
    void getBookById_WithInvalidId_ReturnsNotFound() {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createBook_ReturnsCreatedBookDTO() {
        BookDTO bookDTO = new BookDTO(1L, "Book 1", "Author 1");
        Book book = new Book(1L, "Book 1", "Author 1");
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        when(bookService.saveBook(book)).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.createBook(bookDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bookDTO, response.getBody());
    }

    @Test
    void updateBook_WithExistingBook_ReturnsUpdatedBookDTO() {
        long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(bookId, "Updated Book", "Updated Author");
        Book updatedBook = new Book(bookId, "Updated Book", "Updated Author");
        when(modelMapper.map(updatedBookDTO, Book.class)).thenReturn(updatedBook);
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(new Book()));
        when(bookService.saveBook(updatedBook)).thenReturn(updatedBook);
        when(modelMapper.map(updatedBook, BookDTO.class)).thenReturn(updatedBookDTO);

        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, updatedBookDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBookDTO, response.getBody());
    }

    @Test
    void updateBook_WithNonexistentBook_ReturnsNotFound() {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(bookId, "Updated Book", "Updated Author");
        when(bookService.getBookById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, updatedBookDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteBook_WithExistingBook_ReturnsNoContent() {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(new Book()));

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void deleteBook_WithNonexistentBook_ReturnsNotFound() {
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, never()).deleteBook(bookId);
    }
}
