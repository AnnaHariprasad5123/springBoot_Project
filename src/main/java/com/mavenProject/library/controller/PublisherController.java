package com.mavenProject.library.controller;

import com.mavenProject.library.dto.PublisherDTO;
import com.mavenProject.library.entity.Publisher;
import com.mavenProject.library.service.PublisherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;
    private final ModelMapper modelMapper;

    @Autowired
    public PublisherController(PublisherService publisherService, ModelMapper modelMapper) {
        this.publisherService = publisherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAllPublishers() {
        List<Publisher> publishers = publisherService.getAllPublishers();
        List<PublisherDTO> publisherDTOs = publishers.stream()
                .map(this::entityToDTO)
                .toList();
        return new ResponseEntity<>(publisherDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id) {
        Optional<Publisher> publisher = publisherService.getPublisherById(id);
        return publisher.map(value -> new ResponseEntity<>(entityToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@RequestBody PublisherDTO publisherDTO) {
        Publisher publisher = dtoToEntity(publisherDTO);
        Publisher createdPublisher = publisherService.savePublisher(publisher);
        PublisherDTO createdPublisherDTO = entityToDTO(createdPublisher);
        return new ResponseEntity<>(createdPublisherDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id, @RequestBody PublisherDTO publisherDTO) {
        Optional<Publisher> existingPublisher = publisherService.getPublisherById(id);
        if (existingPublisher.isPresent()) {
            Publisher publisher = dtoToEntity(publisherDTO);
            publisher.setId(id);
            Publisher updatedPublisher = publisherService.savePublisher(publisher);
            PublisherDTO updatedPublisherDTO = entityToDTO(updatedPublisher);
            return new ResponseEntity<>(updatedPublisherDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        Optional<Publisher> existingPublisher = publisherService.getPublisherById(id);
        if (existingPublisher.isPresent()) {
            publisherService.deletePublisher(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Publisher dtoToEntity(PublisherDTO publisherDTO) {
        return modelMapper.map(publisherDTO, Publisher.class);
    }

    private PublisherDTO entityToDTO(Publisher publisher) {
        return modelMapper.map(publisher, PublisherDTO.class);
    }
}
