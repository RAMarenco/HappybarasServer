package org.happybaras.server.services.impl;

import org.happybaras.server.domain.entities.PermitType;
import org.happybaras.server.repositories.PermitTypeRepository;
import org.happybaras.server.services.PermitTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermitTypeServiceImpl implements PermitTypeService {
    private final PermitTypeRepository permitTypeRepository;

    public PermitTypeServiceImpl(PermitTypeRepository permitTypeRepository) {
        this.permitTypeRepository = permitTypeRepository;
    }

    @Override
    public PermitType findByValue(String value) {
        return permitTypeRepository.findByType(value).orElse(null);
    }
}
