package ar.uba.fi.ingsoft1.auth;

import ar.uba.fi.ingsoft1.auth.controller.LoginRequest;
import ar.uba.fi.ingsoft1.auth.controller.RegisterRequest;
import ar.uba.fi.ingsoft1.auth.controller.TokenResponse;
import ar.uba.fi.ingsoft1.auth.repository.Token;
import ar.uba.fi.ingsoft1.auth.repository.TokenRepository;
import ar.uba.fi.ingsoft1.auth.service.AuthService;
import ar.uba.fi.ingsoft1.auth.service.JwtService;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest(
                "Juancito", "Perez", "photo.jpg", 30, "Male",
                "Calle Falsa 123", "juancito.perez@test.com", "pass123"
        );

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .photo(request.photo())
                .age(request.age())
                .gender(request.gender())
                .address(request.address())
                .email(request.email())
                .password("encodedPassword")
                .isEmailVerified(false)
                .accessType(2)
                .build();

        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        TokenResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest("juancito.perez@test.com", "pass123");
        User user = User.builder()
                .email(request.email())
                .password("encodedPassword")
                .build();

        Token mockToken = Token.builder()
                .user(user)
                .token("oldToken")
                .expired(false)
                .revoked(false)
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");
        when(tokenRepository.findAllValidIsFalseOrRevokedIsFalseTokensByUser(user))
                .thenReturn(List.of(mockToken));

        TokenResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).saveAll(argThat(tokens -> {
            int count = 0;
            for (Token token : tokens) {
                if (token.isExpired() && token.isRevoked()) {
                    count++;
                }
            }
            return count == 1;
        }));
    }

    @Test
    void testLoginInvalidCredentials() {
        LoginRequest request = new LoginRequest("juancito.perez@test.com", "wrongPass");
        User user = User.builder()
                .email(request.email())
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(request));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRefreshTokenSuccess() {
        String refreshToken = "refreshToken";
        String authHeader = "Bearer " + refreshToken;
        User user = User.builder()
                .email("juancito.perez@test.com")
                .build();
    
        Token mockToken = Token.builder()
                .user(user)
                .token(refreshToken)
                .expired(false)
                .revoked(false)
                .build();
    
        when(jwtService.extractUsername(refreshToken)).thenReturn("juancito.perez@test.com");
        when(userRepository.findByEmail("juancito.perez@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");
        when(tokenRepository.findAllValidIsFalseOrRevokedIsFalseTokensByUser(user))
                .thenReturn(List.of(mockToken)); // Ensure a collection is returned
    
        TokenResponse response = authService.refreshToken(authHeader);
    
        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
        assertEquals(refreshToken, response.refreshToken());
    
        verify(tokenRepository).saveAll(argThat(tokens -> {
            int count = 0;
            for (Token token : tokens) {
                if (token.isExpired() && token.isRevoked()) {
                    count++;
                }
            }
            return count == 1;
        }));
    }
}
