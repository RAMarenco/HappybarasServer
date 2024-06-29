package org.happybaras.server.domain.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UUIDDTO {
    @NotNull
    private UUID id;
}
