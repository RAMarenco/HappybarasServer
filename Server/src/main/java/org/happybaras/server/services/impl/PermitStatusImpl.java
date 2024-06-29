package org.happybaras.server.services.impl;

import org.happybaras.server.domain.entities.PermitStatus;
import org.happybaras.server.repositories.PermitStatusRepository;
import org.happybaras.server.services.PermitStatusService;
import org.springframework.stereotype.Service;

@Service
public class PermitStatusImpl implements PermitStatusService {
    private final PermitStatusRepository permitStatusRepository;

    public PermitStatusImpl(PermitStatusRepository permitStatusRepository) {
        this.permitStatusRepository = permitStatusRepository;
    }

    @Override
    public PermitStatus findByValue(String value) {
        return permitStatusRepository.findByStatus(value).orElse(null);
    }
}
