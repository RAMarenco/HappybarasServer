package org.happybaras.server.services.impl;

import org.happybaras.server.domain.entities.Code;
import org.happybaras.server.domain.entities.Permit;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.repositories.CodeRepository;
import org.happybaras.server.services.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {
    private final CodeRepository codeRepository;

    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    @Override
    public Code getNewVisitorCode(Permit permit) {
        return null;
    }

    @Override
    public Code getNewResidentCode(User user) {
        return null;
    }

    @Override
    public void invalidateCode(Code code) {

    }

    @Override
    public boolean checkCode(Code code) {
        return false;
    }
}
