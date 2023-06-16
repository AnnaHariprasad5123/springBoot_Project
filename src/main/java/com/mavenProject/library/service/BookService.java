package com.mavenproject.library.service;

import com.mavenproject.library.dao.AuthorRepository;
import com.mavenproject.library.dao.BookRepository;
import com.mavenproject.library.dao.PublisherRepository;
import com.mavenproject.library.entity.Author;
import com.mavenproject.library.entity.Book;
import com.mavenproject.library.entity.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final PublisherRepository publisherRepository;

    @Autowired
    public BookService(BookRepository bookRepository,AuthorRepository authorRepository,PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book assignAuthorToBook(Long bookId, Long authorId) {
        Set<Author> authorSet = null;
        Book book = bookRepository.findById(bookId).get();
        Author author = authorRepository.findById(authorId).get();
        authorSet =  book.getAuthors();
        authorSet.add(author);
        book.setAuthors(authorSet);
        return bookRepository.save(book);
    }

    public Book assignPublisherToBook(Long bookId, Long publisherId) {
        Set<Publisher> publisherSet = null;
        Book book = bookRepository.findById(bookId).get();
        Publisher publisher = publisherRepository.findById(publisherId).get();
        publisherSet =  book.getPublishers();
        publisherSet.add(publisher);
        book.setPublishers(publisherSet);
        return bookRepository.save(book);
    }
}
