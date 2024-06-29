package org.happybaras.server.controllers;

import org.happybaras.server.domain.dtos.GeneralResponse;
import org.happybaras.server.services.CodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeController {
    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping("/new-visitor-code")
    public ResponseEntity<GeneralResponse> generateNewVisitorCode(/*TODO: create a permit dto for generating codes*/) {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @GetMapping("/new-resident-code")
    public ResponseEntity<GeneralResponse> generateNewResidentCode(/*TODO: create a user dto for generating codes*/) {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @PostMapping("/invalid-code")
    public ResponseEntity<GeneralResponse> invalidateCode(/*TODO: create a code dto for invalidating it*/) {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }

    @GetMapping("/check-code")
    public ResponseEntity<GeneralResponse> checkCode(/*TODO: create a code dto for checking its valid state*/) {
        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).getResponse();
    }
}
