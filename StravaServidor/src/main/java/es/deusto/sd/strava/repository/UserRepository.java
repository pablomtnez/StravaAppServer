package es.deusto.sd.strava.repository;

import es.deusto.sd.strava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email to search
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
}
