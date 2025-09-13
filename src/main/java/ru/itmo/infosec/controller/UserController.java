package ru.itmo.infosec.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.infosec.dto.*;
import ru.itmo.infosec.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody LoginRequestDto userCreateDto) {
        return ResponseEntity.ok(userService.register(userCreateDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(@Valid @RequestBody UpdateProfileDto updateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserDto updatedUser = userService.updateProfile(currentUsername, updateDto);

        ProfileUpdateResponse response = new ProfileUpdateResponse(
                updatedUser,
                "Profile updated successfully"
        );

        return ResponseEntity.ok(response);
    }
}
