package es.deusto.sd.strava.services;

import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return the registered user
     */
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Registration failed. User with email {} already exists.", user.getEmail());
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    /**
     * Checks if an email is registered.
     *
     * @param email the email to check
     * @return true if the email is registered, false otherwise
     */
    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Validates user credentials.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return true if the credentials are valid, false otherwise
     */
    public boolean validateCredentials(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            logger.info("Credentials validated for user: {}", email);
            return true;
        }
        logger.warn("Invalid credentials for user: {}", email);
        return false;
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email to search
     * @return the User associated with the email, or null if not found
     */
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("No user found with email: {}", email);
            return null;
        }
        return user.get();
    }
}
