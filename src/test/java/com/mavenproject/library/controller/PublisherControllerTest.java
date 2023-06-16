package com.mavenproject.library.controller;

import com.mavenproject.library.dto.PublisherDTO;
import com.mavenproject.library.entity.Publisher;
import com.mavenproject.library.service.PublisherService;
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

class PublisherControllerTest {

    @Mock
    private PublisherService publisherService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PublisherController publisherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllPublishers_ReturnsListOfPublisherDTOs() {
        List<Publisher> publishers = Arrays.asList(
                new Publisher(1L, "Publisher 1"),
                new Publisher(2L, "Publisher 2")
        );

        when(publisherService.getAllPublishers()).thenReturn(publishers);
        when(modelMapper.map(any(Publisher.class), eq(PublisherDTO.class))).thenReturn(new PublisherDTO());

        ResponseEntity<List<PublisherDTO>> response = publisherController.getAllPublishers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPublisherById_WithValidId_ReturnsPublisherDTO() {
        long publisherId = 1L;
        Publisher publisher = new Publisher(publisherId, "Publisher 1");
        PublisherDTO publisherDTO = new PublisherDTO(publisherId, "Publisher 1");
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.of(publisher));
        when(modelMapper.map(publisher, PublisherDTO.class)).thenReturn(publisherDTO);

        ResponseEntity<PublisherDTO> response = publisherController.getPublisherById(publisherId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(publisherDTO, response.getBody());
    }

    @Test
    void getPublisherById_WithInvalidId_ReturnsNotFound() {
        Long publisherId = 1L;
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.empty());

        ResponseEntity<PublisherDTO> response = publisherController.getPublisherById(publisherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createPublisher_ReturnsCreatedPublisherDTO() {
        PublisherDTO publisherDTO = new PublisherDTO(1L, "Publisher 1");
        Publisher publisher = new Publisher(1L, "Publisher 1");
        when(modelMapper.map(publisherDTO, Publisher.class)).thenReturn(publisher);
        when(publisherService.savePublisher(publisher)).thenReturn(publisher);
        when(modelMapper.map(publisher, PublisherDTO.class)).thenReturn(publisherDTO);

        ResponseEntity<PublisherDTO> response = publisherController.createPublisher(publisherDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(publisherDTO, response.getBody());
    }

    @Test
    void updatePublisher_WithExistingPublisher_ReturnsUpdatedPublisherDTO() {
        long publisherId = 1L;
        PublisherDTO updatedPublisherDTO = new PublisherDTO(publisherId, "Updated Publisher");
        Publisher updatedPublisher = new Publisher(publisherId, "Updated Publisher");
        when(modelMapper.map(updatedPublisherDTO, Publisher.class)).thenReturn(updatedPublisher);
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.of(new Publisher()));
        when(publisherService.savePublisher(updatedPublisher)).thenReturn(updatedPublisher);
        when(modelMapper.map(updatedPublisher, PublisherDTO.class)).thenReturn(updatedPublisherDTO);

        ResponseEntity<PublisherDTO> response = publisherController.updatePublisher(publisherId, updatedPublisherDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPublisherDTO, response.getBody());
    }

    @Test
    void updatePublisher_WithNonexistentPublisher_ReturnsNotFound() {
        Long publisherId = 1L;
        PublisherDTO updatedPublisherDTO = new PublisherDTO(publisherId, "Updated Publisher");
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.empty());

        ResponseEntity<PublisherDTO> response = publisherController.updatePublisher(publisherId, updatedPublisherDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePublisher_WithExistingPublisher_ReturnsNoContent() {
        Long publisherId = 1L;
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.of(new Publisher()));

        ResponseEntity<Void> response = publisherController.deletePublisher(publisherId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(publisherService, times(1)).deletePublisher(publisherId);
    }

    @Test
    void deletePublisher_WithNonexistentPublisher_ReturnsNotFound() {
        Long publisherId = 1L;
        when(publisherService.getPublisherById(publisherId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = publisherController.deletePublisher(publisherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(publisherService, never()).deletePublisher(publisherId);
    }


}
