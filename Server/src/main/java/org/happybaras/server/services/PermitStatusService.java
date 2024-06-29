package org.happybaras.server.services;

import org.happybaras.server.domain.entities.PermitStatus;

public interface PermitStatusService {
    PermitStatus findByValue(String value);
}
