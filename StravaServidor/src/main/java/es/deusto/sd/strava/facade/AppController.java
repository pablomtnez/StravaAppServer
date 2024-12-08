package es.deusto.sd.strava.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.strava.dto.RetoDTO;
import es.deusto.sd.strava.dto.SesionDTO;
import es.deusto.sd.strava.dto.UserDTO;
import es.deusto.sd.strava.dto.UsuarioRetoDTO;
import es.deusto.sd.strava.services.StravaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Strava Controller", description = "Operations for managing sessions and challenges")
public class AppController {

    private final StravaService stravaService;

    public AppController(StravaService stravaService) {
        this.stravaService = stravaService;
    }

    @Operation(
        summary = "Create a new training session",
        description = "Allows creating a new manual training session.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: Session created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid input")
        }
    )
    @PostMapping("/sessions")
    public ResponseEntity<String> createSession(@Parameter(description = "Training session data", required = true)
                                                @RequestBody SesionDTO sesionDTO) {
        try {
            Date horaInicio = new SimpleDateFormat("HH:mm").parse(sesionDTO.getHoraInicio());
            stravaService.crearSesion(
                sesionDTO.getTitulo(),
                sesionDTO.getDistancia(),
                sesionDTO.getFechaInicio(),
                horaInicio,
                sesionDTO.getDuracion(),
                sesionDTO.getDeporte()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("Session created successfully.");
        } catch (IllegalArgumentException | ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get the latest training sessions",
        description = "Retrieves the last 5 training sessions.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Sessions retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No sessions found")
        }
    )
    @GetMapping("/sessions/latest")
    public ResponseEntity<List<SesionDTO>> getLatestSessions() {
        List<SesionDTO> sessions = stravaService.consultarUltimasSesiones();
        if (sessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sessions);
    }

    @Operation(
        summary = "Create a new challenge",
        description = "Allows creating a new challenge with specific goals.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Created: Challenge created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid input")
        }
    )
    @PostMapping("/challenges")
    public ResponseEntity<String> createChallenge(@RequestBody RetoDTO retoDTO) {
        try {
            stravaService.crearReto(
                retoDTO.getNombre(),
                retoDTO.getFechaInicio(),
                retoDTO.getFechaFin(),
                retoDTO.getDistancia(),
                retoDTO.getTiempo(),
                retoDTO.getDeporte()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Challenge created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
        summary = "Get active challenges",
        description = "Retrieves all active challenges that have not ended.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Challenges retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No Content: No active challenges found")
        }
    )
    @GetMapping("/challenges/active")
    public ResponseEntity<List<RetoDTO>> getActiveChallenges() {
        List<RetoDTO> challenges = stravaService.consultarRetosActivos();
        if (challenges.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(challenges);
    }

    @Operation(
        summary = "Accept a challenge",
        description = "Allows a user to accept a challenge.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Challenge accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Not Found: Challenge not found")
        }
    )
    @PostMapping("/challenges/accept")
    public ResponseEntity<String> acceptChallenge(
            @RequestParam(name = "challengeName") String challengeName, 
            @RequestBody UserDTO userDTO) {
        try {
            stravaService.aceptarReto(challengeName, userDTO);
            return ResponseEntity.ok("Challenge accepted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
        summary = "Get accepted challenges",
        description = "Retrieves all challenges accepted by a user.",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Challenges retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No challenges accepted by the user")
        }
    )
    @GetMapping("/challenges/accepted")
    public ResponseEntity<List<UsuarioRetoDTO>> getAcceptedChallenges(@RequestBody UserDTO userDTO) {
        List<UsuarioRetoDTO> acceptedChallenges = stravaService.consultarRetosAceptados(userDTO);
        if (acceptedChallenges.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(acceptedChallenges);
    }
}
