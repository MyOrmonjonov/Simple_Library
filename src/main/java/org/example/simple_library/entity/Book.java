package org.example.simple_library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity{

    private String title;
    private String author;
    private Integer pages;
    @ManyToOne
    private Users borrowedBy;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Attachment attachment;
}
