package org.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    private Long id;
    private String author;
    private String title;

    @Digits(integer = 4, fraction = 0)
    private Long size;
}
