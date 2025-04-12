package org.example.simple_library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String title;
    private String author;
    private Integer pages;
    private Integer borrowedById;
    private Integer genreId;
    private Integer attachmentId;
}
