package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditionDTO {
    @NotNull
    private UUID user;

    @NotNull
    private UUID house;

    @NotBlank
    private String role;
}
