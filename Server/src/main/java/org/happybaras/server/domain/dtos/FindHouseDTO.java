package org.happybaras.server.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.happybaras.server.domain.entities.User;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindHouseDTO {
    private UUID id;
    private int numberOfHabitants;
    private String houseNumber;
    private String address;
    private String telephone;
    private OwnerDTO owner;
}
