package ar.uba.fi.ingsoft1.auth.service;

import ar.uba.fi.ingsoft1.auth.controller.LoginRequest;
import ar.uba.fi.ingsoft1.auth.controller.RegisterRequest;
import ar.uba.fi.ingsoft1.auth.controller.TokenResponse;
import ar.uba.fi.ingsoft1.auth.repository.Token;
import ar.uba.fi.ingsoft1.auth.repository.TokenRepository;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request){
        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .photo(request.photo())
                .age(request.age())
                .gender(request.gender())
                .address(request.address())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isEmailVerified(false)
                .accessType(2) //No pueden darse de alta usuarios admins
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);

        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse refreshToken(final String authHeader) {
//        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
////            throw new IllegalAccessException("Invalid Bearer token");
//        }
//
        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
//
//        if(userEmail == null) {
////            throw new IllegalAccessException("Invalid Refresh token");
//        }
//
        final User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));
//
////        if (!jwtService.isvL(refreshToken)){
//////            throw new IllegalAccessException("Invalid Refresh Token");
////        }

        final String accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveUserToken(User user, String jwtToken){
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(final User user){
        final List<Token> validUserTokens = tokenRepository.findAllValidIsFalseOrRevokedIsFalseTokensByUser(user);
        if(!validUserTokens.isEmpty()){
            for(final Token token : validUserTokens){
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

}
