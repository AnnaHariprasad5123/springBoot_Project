package com.mavenproject.library.controller;

import com.mavenproject.library.dto.BookDTO;
import com.mavenproject.library.entity.Book;
import com.mavenproject.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Autowired
    public BookController(BookService bookService, ModelMapper modelMapper) {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        Book book = dtoToEntity(bookDTO);
        Book createdBook = bookService.saveBook(book);
        BookDTO createdBookDTO = entityToDTO(createdBook);
        return new ResponseEntity<>(createdBookDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        Optional<Book> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            Book book = dtoToEntity(bookDTO);
            book.setId(id);
            Book updatedBook = bookService.saveBook(book);
            BookDTO updatedBookDTO = entityToDTO(updatedBook);
            return new ResponseEntity<>(updatedBookDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{bookId}/author/{authorId}")
    public Book assignAuthorToBook(
            @PathVariable Long bookId,
            @PathVariable Long authorId
    ) {
        return bookService.assignAuthorToBook(bookId, authorId);
    }

    @PutMapping("/{bookId}/publisher/{publisherId}")
    public Book assignPublisherToBook(
            @PathVariable Long bookId,
            @PathVariable Long publisherId
    ) {
        return bookService.assignPublisherToBook(bookId, publisherId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> existingBook = bookService.getBookById(id);
        if (existingBook.isPresent()) {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Book dtoToEntity(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    private BookDTO entityToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }
}
