package com.mavenproject.library.dto;

import com.mavenproject.library.entity.Author;
import com.mavenproject.library.entity.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private Integer publicationYear;
    private String isbn;
    private Set<Author> authors;
    private Set<Publisher> publishers;

    public BookDTO(long l, String s, String s1) {
    }
}
