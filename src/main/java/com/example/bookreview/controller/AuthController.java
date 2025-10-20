package com.example.bookreview.controller;


import com.example.bookreview.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@RequestMapping("/token")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;

    @Operation(
            summary = "First step! Generates a token",
            description = "Insert any username and generate a token with which you can Authorize all method calls using the Authorize button!"
    )
    @GetMapping
    public ResponseEntity<?> getJwtToken(@RequestParam String username) {
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }
}