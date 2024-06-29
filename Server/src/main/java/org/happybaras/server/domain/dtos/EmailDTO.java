package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EmailDTO {
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$", message = "Must be a valid email")
    private String email;
}
