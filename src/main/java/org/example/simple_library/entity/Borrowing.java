package org.example.simple_library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrowing extends BaseEntity{

        @ManyToOne
        private Users user;

        @ManyToOne
        private Book book;

        private LocalDate borrowDate;
        private LocalDate returnDate;
}
