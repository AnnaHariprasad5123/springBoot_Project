package com.mavenproject.library.services;

import com.mavenproject.library.dao.PublisherRepository;
import com.mavenproject.library.entity.Publisher;
import com.mavenproject.library.service.PublisherService;
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

class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(new Publisher(1L, "Publisher 1", "Location 1", new HashSet<>()));
        publishers.add(new Publisher(2L, "Publisher 2", "Location 2", new HashSet<>()));
        when(publisherRepository.findAll()).thenReturn(publishers);

        List<Publisher> result = publisherService.getAllPublishers();

        assertEquals(2, result.size());
        assertEquals(publishers, result);
        verify(publisherRepository, times(1)).findAll();
    }

    @Test
    void testGetPublisherById() {
        Publisher publisher = new Publisher(1L, "Publisher 1", "Location 1", new HashSet<>());
        when(publisherRepository.findById(anyLong())).thenReturn(Optional.of(publisher));

        Optional<Publisher> result = publisherService.getPublisherById(1L);

        assertTrue(result.isPresent());
        assertEquals(publisher, result.get());
        verify(publisherRepository, times(1)).findById(1L);
    }

    @Test
    void testSavePublisher() {
        Publisher publisher = new Publisher(1L, "Publisher 1", "Location 1", new HashSet<>());
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        Publisher result = publisherService.savePublisher(publisher);

        assertEquals(publisher, result);
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void testDeletePublisher() {
        Long publisherId = 1L;

        publisherService.deletePublisher(publisherId);

        verify(publisherRepository, times(1)).deleteById(publisherId);
    }
}
