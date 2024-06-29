package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEntryDTO {
    // TODO: Modify RegisterEntryDTO for requiring the vigilant token
    @NotBlank
    private String identifier;

    @NotBlank
    private String identityDocument;

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{4}") // The residence number will have a maximum of 4 digits
    private String houseNumber;

    @NotBlank
    private String description;
}
