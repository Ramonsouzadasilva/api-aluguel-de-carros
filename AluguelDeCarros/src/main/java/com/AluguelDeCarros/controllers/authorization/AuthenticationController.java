package com.AluguelDeCarros.controllers.authorization;

import com.AluguelDeCarros.dto.authorization.AuthenticationDto;
import com.AluguelDeCarros.dto.authorization.LoginResponseDto;
import com.AluguelDeCarros.dto.authorization.RegisterDto;
import com.AluguelDeCarros.service.authorization.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto data) {
        String token = userService.login(data);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterDto data) {
        userService.register(data);
        return ResponseEntity.ok().build();
    }
}
