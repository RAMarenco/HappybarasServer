package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String picture;
}
