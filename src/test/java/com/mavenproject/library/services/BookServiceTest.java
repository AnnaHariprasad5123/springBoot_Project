package com.mavenproject.library.services;

import com.mavenproject.library.dao.BookRepository;
import com.mavenproject.library.service.BookService;
import com.mavenproject.library.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>()));
        books.add(new Book(2L, "Book 2", 2023, "ISBN-2", new HashSet<>(), new HashSet<>()));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals(books, result);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        Book book = new Book(1L, "Book 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveBook() {
        Book book = new Book(1L, "Book 1", 2023, "ISBN-1", new HashSet<>(), new HashSet<>());
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.saveBook(book);

        assertEquals(book, result);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
