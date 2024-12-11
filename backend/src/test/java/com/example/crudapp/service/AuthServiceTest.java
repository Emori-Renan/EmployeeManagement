package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.crudapp.dto.AuthenticationResponse;
import com.example.crudapp.model.UserLogin;
import com.example.crudapp.repository.UserLoginRepository;
import com.example.crudapp.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Test
void testAuthenticate_ValidUsername() {
    String username = "testuserteste";
    String password = "password";
    String encodedPassword = "$2a$10$1IUYrU4VWkp/vI8HzEC2EObVYgsTNGWLJ.FoajM9l9ZgHE5miZgeG"; // Encoded password

    // Mock the UserLogin object with the username and encoded password
    UserLogin mockUser = new UserLogin();
    mockUser.setUsername(username);
    mockUser.setPassword(encodedPassword); // Use encoded password
    mockUser.setRole("ROLE_USER");

    // Mock the repository to return the mock user when searched by username
    when(userLoginRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

    // Mock the AuthenticationManager and Authentication objects
    Authentication mockAuth = mock(Authentication.class);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mockAuth);

    // Ensure the principal is set to the mock user
    when(mockAuth.getPrincipal()).thenReturn(mockUser);

    // Mock the JWT token generation (return mock token)
    when(jwtUtil.generateToken(username, "ROLE_USER")).thenReturn("mockToken");

    // Call the authenticate method
    ResponseEntity<AuthenticationResponse> response = authService.authenticate(username, password);

    // Assertions
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("mockToken", response.getBody().getToken());
    assertEquals("Login successful", response.getBody().getMessage());
}


    // Other unit tests (e.g., invalid user, invalid password, etc.)
}
