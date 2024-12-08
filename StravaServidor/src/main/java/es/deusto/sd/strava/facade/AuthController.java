package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.UserDTO;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        try {
            User user = new User();
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setNombre(userDTO.getNombre());

            User registeredUser = authService.register(user);

            UserDTO responseDTO = new UserDTO(registeredUser.getEmail(), registeredUser.getPassword(), registeredUser.getNombre());
            logger.info("User registered successfully: {}", registeredUser.getEmail());
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        boolean isValid = authService.validateCredentials(userDTO.getEmail(), userDTO.getPassword());
        if (isValid) {
            logger.info("Login successful for user: {}", userDTO.getEmail());
            return ResponseEntity.ok("Login successful!");
        } else {
            logger.warn("Login failed for user: {}", userDTO.getEmail());
            return new ResponseEntity<>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
        }
    }
}
