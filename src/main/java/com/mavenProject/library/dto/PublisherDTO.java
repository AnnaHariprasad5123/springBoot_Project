package com.mavenProject.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mavenProject.library.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {
    private Long id;
    private String name;
    private String location;
    @JsonIgnore
    private Set<Book> books;

    public PublisherDTO(long l, String s) {
    }
}
