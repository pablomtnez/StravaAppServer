/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.entity.TipoUsuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    // User repository for storing registered users
    private static final Map<String, User> userRepository = new HashMap<>();
    // Map to associate tokens with email addresses
    private static final Map<String, String> tokenToEmailMap = new HashMap<>();

    /**
     * Logs in a user and generates a token if successful.
     *
     * @param email       the user's email
     * @param tipoUsuario the user's account type (e.g., GOOGLE, META)
     * @return an Optional containing the generated token if login is successful
     */
    public Optional<String> login(String email, TipoUsuario tipoUsuario) {
        logger.info("Attempting to login user with email: {}", email);
        User user = userRepository.get(email);

        if (user != null && user.getTipoUsuario() == tipoUsuario) {
            String token = generateToken();
            tokenToEmailMap.put(token, email); // Store email associated with the token
            logger.info("Login successful for user: {}. Token generated: {}", email, token);
            return Optional.of(token);
        } else {
            logger.warn("Login failed for user: {}. Invalid credentials.", email);
            return Optional.empty();
        }
    }

    /**
     * Logs out a user by invalidating their token.
     *
     * @param token the token to invalidate
     * @return an Optional indicating if the logout was successful
     */
    public Optional<Boolean> logout(String token) {
        logger.info("Attempting to logout with token: {}", token);
        String email = tokenToEmailMap.get(token);

        if (email != null) {
            tokenToEmailMap.remove(token);
            logger.info("Logout successful for token: {} (email: {})", token, email);
            return Optional.of(true);
        } else {
            logger.warn("Logout failed. Token not found or invalid: {}", token);
            return Optional.empty();
        }
    }

    /**
     * Registers a new user.
     *
     * @param user the user to register
     */
    public void register(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
            logger.error("Registration failed. User or email is null/empty.");
            throw new IllegalArgumentException("User and email must not be null or empty");
        }

        if (userRepository.containsKey(user.getEmail())) {
            logger.error("Registration failed. User with email {} already exists.", user.getEmail());
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        userRepository.put(user.getEmail(), user);
        logger.info("User registered successfully: {}", user.getEmail());
    }

    /**
     * Fetches a user associated with a given token.
     *
     * @param token the token to search
     * @return the User associated with the token, or null if not found
     */
    public User getUserByToken(String token) {
        logger.info("Fetching user by token: {}", token);
        String email = tokenToEmailMap.get(token);
        if (email == null) {
            logger.warn("No user found for token: {}", token);
            return null;
        }
        return userRepository.get(email);
    }

    /**
     * Fetches a user by their email address.
     *
     * @param email the email to search
     * @return the User associated with the email, or null if not found
     */
    public User getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        User user = userRepository.get(email);
        if (user == null) {
            logger.warn("No user found with email: {}", email);
        }
        return user;
    }

    /**
     * Generates a unique token for a user.
     *
     * @return a unique token
     */
    private static synchronized String generateToken() {
        String token = Long.toHexString(System.currentTimeMillis()) + "-" + Math.random();
        logger.debug("Generated token: {}", token);
        return token;
    }

    /**
     * Validates if a token is still active.
     *
     * @param token the token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        boolean valid = tokenToEmailMap.containsKey(token);
        logger.info("Token validation for {}: {}", token, valid);
        return valid;
    }
}
