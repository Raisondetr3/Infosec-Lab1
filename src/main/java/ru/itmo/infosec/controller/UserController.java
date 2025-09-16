package ru.itmo.infosec.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.infosec.dto.*;
import ru.itmo.infosec.service.JwtBlacklistService;
import ru.itmo.infosec.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            jwtBlacklistService.blacklistToken(token);

            return ResponseEntity.ok(Map.of(
                    "message", "Successfully logged out",
                    "timestamp", String.valueOf(System.currentTimeMillis())
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "error", "No token provided"
        ));
    }
}
