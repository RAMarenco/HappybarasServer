package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class HouseNumberDTO {
    @NotBlank
    @Pattern(regexp = "^\\d{4}", message = "Must be 4 digits long")
    private String houseNumber;
}
