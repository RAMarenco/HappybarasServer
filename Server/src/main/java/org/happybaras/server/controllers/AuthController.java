package org.happybaras.server.controllers;

import jakarta.validation.Valid;
import org.happybaras.server.domain.dtos.GeneralResponse;
import org.happybaras.server.domain.dtos.LoginDTO;
import org.happybaras.server.domain.dtos.LoginResponseDTO;
import org.happybaras.server.domain.entities.Token;
import org.happybaras.server.domain.entities.User;
import org.happybaras.server.services.UserService;
import org.happybaras.server.utils.DTOConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final DTOConverters dtoConverters;

    public AuthController(UserService userService, DTOConverters dtoConverters) {
        this.userService = userService;
        this.dtoConverters = dtoConverters;
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@RequestBody @Valid LoginDTO info) {
        User user = userService.findOneByIdentifier(info.getEmail());

        if (user == null) {
            userService.createUser(info);
        }

        user = userService.findOneByIdentifier(info.getEmail());

        if (user != null) {
            try {
                Token token = userService.registerToken(user);
                LoginResponseDTO loginResponseDTO = dtoConverters.convertToLoginResponseDTO(token, user);
                return GeneralResponse.builder().status(HttpStatus.OK).message("Logged in successfully.").data(loginResponseDTO).getResponse();
            } catch (Exception e) {
                e.printStackTrace();
                return GeneralResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).getResponse();
            }
        }

        return GeneralResponse.builder().status(HttpStatus.NOT_FOUND).message("Account not found.").getResponse();
    }
}
