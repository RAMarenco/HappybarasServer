package org.happybaras.server.services;

import org.happybaras.server.domain.entities.PermitType;

public interface PermitTypeService {
    PermitType findByValue(String value);
}
