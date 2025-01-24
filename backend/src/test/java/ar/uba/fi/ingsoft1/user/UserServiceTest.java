package ar.uba.fi.ingsoft1.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setPassword("password123");
        mockUser.setPhoto("http://example.com/photo.jpg");
        mockUser.setAge(30);
        mockUser.setAddress("123 Main St");
        mockUser.setGender("Male");
        mockUser.setEmailVerified(false);
    }

    @Test
    void testRegisterUser() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User registeredUser = userService.registerUser(mockUser);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void testGetUserByEmailFound() {
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(mockUser));

        User user = userService.getUserByEmail("johndoe@example.com");

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("johndoe@example.com"));
    }

    @Test
    void testGetUserByIdFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUsers() {
        List<User> users = Collections.singletonList(mockUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> fetchedUsers = userService.getUsers();

        assertNotNull(fetchedUsers);
        assertEquals(1, fetchedUsers.size());
        assertEquals("John", fetchedUsers.get(0).getFirstName());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User updatedUser = userService.updateUser(mockUser);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}