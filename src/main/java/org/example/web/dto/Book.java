package org.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20)
    @Pattern(regexp="^[A-Za-z]*$")
    private String author;

    @NotBlank
    @Size(min = 2, max = 20)
    private String title;

    @NotNull
    @Digits(integer = 4, fraction = 0)
    private Long size;
}
