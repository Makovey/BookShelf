package org.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 25)
    private String username;

    @NotBlank
    @Size(min = 2, max = 25)
    private String password;
}
