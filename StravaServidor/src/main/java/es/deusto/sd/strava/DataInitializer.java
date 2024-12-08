/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.deusto.sd.strava.entity.Deportes;
import es.deusto.sd.strava.entity.TipoUsuario;
import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.services.AuthService;
import es.deusto.sd.strava.services.StravaService;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Bean
    CommandLineRunner initData(StravaService stravaService, AuthService authService) {
        return args -> {

            // Crear usuarios
            User user1 = new User(TipoUsuario.GOOGLE, "user1@gmail.com", "Alice", createDate(1990, 5, 15), 60.5f, 165f, 180f, 70f);
            User user2 = new User(TipoUsuario.META, "user2@gmail.com", "Bob", createDate(1985, 2, 10), 75f, 178f, 190f, 80f);
            User user3 = new User(TipoUsuario.GOOGLE, "user3@gmail.com", "Charlie", createDate(2000, 7, 25), 68f, 172f, 185f, 75f);

            registerUser(authService, user1);
            registerUser(authService, user2);
            registerUser(authService, user3);

            // Crear y agregar sesiones de entrenamiento
            addSesionToService(stravaService, "Morning Run", 5.0f, createDate(2024, 10, 10), "06:30", 30.0f, Deportes.RUNNING);
            addSesionToService(stravaService, "Evening Cycling", 20.0f, createDate(2024, 10, 9), "18:00", 60.0f, Deportes.CICLISMO);
            addSesionToService(stravaService, "Weekend Hike", 10.0f, createDate(2024, 10, 8), "08:00", 120.0f, Deportes.CICLISMO_AND_RUNNING);
            addSesionToService(stravaService, "Night Run", 7.0f, createDate(2024, 10, 7), "20:30", 45.0f, Deportes.RUNNING);
            addSesionToService(stravaService, "Hill Cycling", 25.0f, createDate(2024, 10, 6), "09:00", 90.0f, Deportes.CICLISMO);

            // Crear y agregar retos
            addChallengeToService(stravaService, "Run 50km in November", createDate(2024, 10, 1), createDate(2025, 10, 30), 50.0f, 0.0f, Deportes.RUNNING);
            addChallengeToService(stravaService, "Cycle 100km this month", createDate(2024, 10, 1), createDate(2024, 10, 30), 100.0f, 0.0f, Deportes.CICLISMO);
            addChallengeToService(stravaService, "Triathlon Prep", createDate(2024, 10, 5), createDate(2025, 10, 20), 30.0f, 300.0f, Deportes.CICLISMO_AND_RUNNING);
            addChallengeToService(stravaService, "Morning Challenge", createDate(2024, 9, 1), createDate(2024, 9, 30), 20.0f, 60.0f, Deportes.RUNNING);
            addChallengeToService(stravaService, "Weekend Warrior", createDate(2024, 10, 7), createDate(2025, 10, 14), 15.0f, 120.0f, Deportes.CICLISMO);
        };
    }

    private void registerUser(AuthService authService, User user) {
        try {
            authService.register(user);
            logger.info("User registered: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to register user {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private void addSesionToService(StravaService stravaService, String titulo, float distancia, Date fechaInicio, String horaInicio, float duracion, Deportes deporte) {
        try {
            Date parsedHoraInicio = timeFormat.parse(horaInicio); // Convertir horaInicio a Date
            stravaService.crearSesion(titulo, distancia, fechaInicio, parsedHoraInicio, duracion, deporte);
            logger.info("Session added: {}", titulo);
        } catch (ParseException e) {
            logger.error("Invalid time format for session '{}': {}", titulo, e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to add session '{}': {}", titulo, e.getMessage());
        }
    }

    private void addChallengeToService(StravaService stravaService, String nombre, Date fechaInicio, Date fechaFin, float distancia, float tiempo, Deportes deporte) {
        try {
            stravaService.crearReto(nombre, fechaInicio, fechaFin, distancia, tiempo, deporte);
            logger.info("Challenge added: {}", nombre);
        } catch (Exception e) {
            logger.error("Failed to add challenge '{}': {}", nombre, e.getMessage());
        }
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }
}
