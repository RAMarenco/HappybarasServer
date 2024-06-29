package org.happybaras.server.services;

import org.happybaras.server.domain.entities.Code;
import org.happybaras.server.domain.entities.Permit;
import org.happybaras.server.domain.entities.User;

public interface CodeService {
    Code getNewVisitorCode(Permit permit);
    Code getNewResidentCode(User user);
    void invalidateCode(Code code);
    boolean checkCode(Code code);
}
