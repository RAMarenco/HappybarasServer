package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterHouseDTO {
    @NotBlank(message = "Address cannot be blank")
    @Pattern(regexp = "^[\\w\\s,.#-]{2,}$", message = "Address must start with an alphanumeric character and can include spaces, commas, periods, hash, and hyphen.")
    private String address;

    @NotBlank
    @Pattern(regexp = "^\\d{4}", message = "Must be 4 digits long")
    private String houseNumber;

    @Pattern(regexp = "^(\\d{4}-\\d{4})$", message = "Must follow format xxxx-xxxx") // Format: 7839-4892
    private String telephone;

    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}$", message = "Must be a valid email")
    private String ownerEmail;
}
