package com.example.taxinvoice.controller;

import com.example.taxinvoice.entity.User;
import com.example.taxinvoice.service.AuthService;
import com.example.taxinvoice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;  // Inject JWT utility class

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(authService.register(email, password));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String email, @RequestParam String password) {
        User user = authService.authenticate(email, password); // Authenticate user
        String token = jwtUtil.generateToken(user.getEmail());  // Generate JWT token

        return ResponseEntity.ok(Map.of("token", token));
    }
}
