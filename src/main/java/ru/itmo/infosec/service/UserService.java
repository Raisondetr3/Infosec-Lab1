package ru.itmo.infosec.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import ru.itmo.infosec.dto.AuthResponseDto;
import ru.itmo.infosec.dto.LoginRequestDto;
import ru.itmo.infosec.dto.UpdateProfileDto;
import ru.itmo.infosec.dto.UserDto;
import ru.itmo.infosec.entity.User;
import ru.itmo.infosec.exception.UserAlreadyExistsException;
import ru.itmo.infosec.exception.UserNotFoundException;
import ru.itmo.infosec.repo.UserRepository;
import ru.itmo.infosec.util.EntityMapper;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto register(LoginRequestDto dto) {
        String safeUsername = HtmlUtils.htmlEscape(dto.getUsername().trim());

        if (userRepository.findByUsername(safeUsername).isPresent()) {
            throw new UserAlreadyExistsException("Username " + safeUsername + " already exists");
        }

        if (!isPasswordStrong(dto.getPassword())) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters, including uppercase, lowercase, number and special character");
        }

        LoginRequestDto safeDto = new LoginRequestDto(safeUsername, dto.getPassword());

        User user = entityMapper.toUserEntity(safeDto, passwordEncoder);
        User savedUser = userRepository.save(user);
        UserDto registeredUser = entityMapper.toUserDto(savedUser);

        String token = jwtService.generateToken(registeredUser.getUsername());
        long expirationTime = System.currentTimeMillis() + jwtService.getJwtExpiration();

        return new AuthResponseDto(token, expirationTime, registeredUser);
    }

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        String safeUsername = HtmlUtils.htmlEscape(loginRequestDto.getUsername().trim());

        UserDetails userDetails = userRepository.findByUsername(safeUsername)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPassword())) {
            passwordEncoder.encode("dummy");
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDto user = findByUsername(safeUsername);

        String token = jwtService.generateToken(user.getUsername());
        long expirationTime = System.currentTimeMillis() + jwtService.getJwtExpiration();

        return new AuthResponseDto(token, expirationTime, user);
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(entityMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException("User by username " + username + " not found"));
    }

    @Transactional
    public UserDto updateProfile(String currentUsername, UpdateProfileDto updateDto) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + currentUsername));

        if (updateDto.getNewPassword() != null && !updateDto.getNewPassword().trim().isEmpty()) {
            if (updateDto.getCurrentPassword() == null || updateDto.getCurrentPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Current password is required to change password");
            }

            if (!passwordEncoder.matches(updateDto.getCurrentPassword(), user.getPassword())) {
                passwordEncoder.encode("dummy");
                throw new BadCredentialsException("Current password is incorrect");
            }

            if (isPasswordStrong(updateDto.getNewPassword())) {
                throw new IllegalArgumentException(
                        "New password must contain at least 8 characters, including uppercase, lowercase, number and special character");
            }

            if (passwordEncoder.matches(updateDto.getNewPassword(), user.getPassword())) {
                throw new IllegalArgumentException("New password must be different from current password");
            }

            user.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));
        }

        if (updateDto.getUsername() != null && !updateDto.getUsername().trim().isEmpty()) {
            String newUsername = HtmlUtils.htmlEscape(updateDto.getUsername().trim());

            if (!newUsername.equals(currentUsername)) {
                if (userRepository.findByUsername(newUsername).isPresent()) {
                    throw new UserAlreadyExistsException("Username " + newUsername + " is already taken");
                }
                user.setUsername(newUsername);
            }
        }

        User savedUser = userRepository.save(user);
        return entityMapper.toUserDto(savedUser);
    }

    private boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return true;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        return !hasUpper || !hasLower || !hasDigit || !hasSpecial;
    }
}