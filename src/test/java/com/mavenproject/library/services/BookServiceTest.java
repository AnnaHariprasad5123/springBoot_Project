package com.mavenproject.library.services;

import com.mavenproject.library.dao.AuthorRepository;
import com.mavenproject.library.dao.BookRepository;
import com.mavenproject.library.dao.PublisherRepository;
import com.mavenproject.library.entity.Author;
import com.mavenproject.library.entity.Publisher;
import com.mavenproject.library.service.BookService;
import com.mavenproject.library.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

     @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PublisherRepository publisherRepository;

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

    @Test
    void testAssignAuthorToBook() {
        Long bookId = 1L;
        Long authorId = 1L;
        Book book = new Book();
        Author author = new Author();
        Set<Author> authorSet = new HashSet<>();
        authorSet.add(author);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.assignAuthorToBook(bookId, authorId);

        assertNotNull(result);
        assertEquals(authorSet, result.getAuthors());
        verify(bookRepository, times(1)).findById(bookId);
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testAssignPublisherToBook() {
        Long bookId = 1L;
        Long publisherId = 1L;
        Book book = new Book();
        Publisher publisher = new Publisher();
        Set<Publisher> publisherSet = new HashSet<>();
        publisherSet.add(publisher);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));
        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.assignPublisherToBook(bookId, publisherId);

        assertNotNull(result);
        assertEquals(publisherSet, result.getPublishers());
        verify(bookRepository, times(1)).findById(bookId);
        verify(publisherRepository, times(1)).findById(publisherId);
        verify(bookRepository, times(1)).save(book);
    }
}
