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
    String encodedPassword = "$2a$10$1IUYrU4VWkp/vI8HzEC2EObVYgsTNGWLJ.FoajM9l9ZgHE5miZgeG"; 

    UserLogin mockUser = new UserLogin();
    mockUser.setUsername(username);
    mockUser.setPassword(encodedPassword); 
    mockUser.setRole("ROLE_USER");

    when(userLoginRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

    Authentication mockAuth = mock(Authentication.class);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(mockAuth);

    when(mockAuth.getPrincipal()).thenReturn(mockUser);

    when(jwtUtil.generateToken(username, "ROLE_USER")).thenReturn("mockToken");

    ResponseEntity<AuthenticationResponse> response = authService.authenticate(username, password);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    AuthenticationResponse responseBody = response.getBody();
    assertNotNull(responseBody, "AuthenticationResponse body should not be null");
    assertEquals("mockToken", responseBody.getToken());
    assertEquals("Login successful", responseBody.getMessage());
}

}
