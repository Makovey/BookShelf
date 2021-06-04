package org.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookFiltered {

    @Pattern(regexp="^[A-Za-z]*$")
    @Size(max = 20)
    private String author;

    @Size(max = 20)
    private String title;

    @Digits(integer = 4, fraction = 0)
    private Long size;
}
