package org.happybaras.server.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.happybaras.server.domain.entities.UserRole;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindResidentDTO {
    private UUID id;
    private String username;
    private String email;
    private String avatar;
    private List<UserRole> rol;
}
