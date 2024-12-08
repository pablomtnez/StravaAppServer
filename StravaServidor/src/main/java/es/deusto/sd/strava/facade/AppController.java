/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.RetoDTO;
import es.deusto.sd.strava.dto.SesionDTO;
import es.deusto.sd.strava.dto.UsuarioRetoDTO;
import es.deusto.sd.strava.entity.Sesion;
import es.deusto.sd.strava.dto.UserDTO;
import es.deusto.sd.strava.dto.RetoAssembler;
import es.deusto.sd.strava.dto.SesionAssembler;
import es.deusto.sd.strava.dto.UsuarioRetoAssembler;
import es.deusto.sd.strava.dto.UserAssembler;
import es.deusto.sd.strava.services.StravaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
            // Convertir el string de horaInicio a un objeto Date utilizando el método parseTime
            Date horaInicio = SesionAssembler.parseTime(sesionDTO.getHoraInicio());

            // Llamar al servicio con los parámetros correctamente convertidos
            stravaService.crearSesion(
                sesionDTO.getTitulo(),
                sesionDTO.getDistancia(),
                sesionDTO.getFechaInicio(),
                horaInicio, // Usamos la hora convertida
                sesionDTO.getDuracion(),
                sesionDTO.getDeporte()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body("Session created successfully.");
        } catch (IllegalArgumentException | ParseException e) {
            // Manejar excepciones y devolver un mensaje de error adecuado
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
        List<SesionDTO> sessions = stravaService.consultarUltimasSesiones().stream()
                .map(SesionAssembler::toDTO)
                .toList();

        if (sessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sessions);
    }

    @Operation(
    	    summary = "Get training sessions by date range",
    	    description = "Retrieves all training sessions within a specified date range.",
    	    responses = {
    	        @ApiResponse(responseCode = "200", description = "OK: Sessions retrieved successfully"),
    	        @ApiResponse(responseCode = "400", description = "Bad Request: Invalid date range")
    	    }
    	)
    	@GetMapping("/sessions")
    	public ResponseEntity<List<SesionDTO>> getSessionsByDateRange(
    	        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
    	        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
    	    try {
    	        List<SesionDTO> sessions = stravaService.consultarSesionesPorFechas(startDate, endDate).stream()
    	                .map(SesionAssembler::toDTO)
    	                .toList();
    	        return ResponseEntity.ok(sessions);
    	    } catch (IllegalArgumentException e) {
    	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	    }
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
    public ResponseEntity<String> createChallenge(@Parameter(description = "Challenge data", required = true)
                                                   @RequestBody RetoDTO retoDTO) {
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
        try {
            List<RetoDTO> challenges = stravaService.consultarRetosActivos().stream()
                    .map(RetoAssembler::toDTO)
                    .toList();

            if (challenges.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(challenges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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
            stravaService.aceptarReto(challengeName, UserAssembler.toEntity(userDTO));
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
            @ApiResponse(responseCode = "204", description = "No Content: No challenges accepted by the user")
        }
    )
    @GetMapping("/challenges/accepted")
    public ResponseEntity<List<UsuarioRetoDTO>> getAcceptedChallenges(@RequestBody UserDTO userDTO) {
        try {
            List<UsuarioRetoDTO> acceptedChallenges = stravaService.consultarRetosAceptados(UserAssembler.toEntity(userDTO)).stream()
                    .map(UsuarioRetoAssembler::toDTO)
                    .toList();

            if (acceptedChallenges.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(acceptedChallenges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(
    	    summary = "Get challenge progress",
    	    description = "Retrieves the progress of a user on a specific challenge.",
    	    responses = {
    	        @ApiResponse(responseCode = "200", description = "OK: Progress retrieved successfully"),
    	        @ApiResponse(responseCode = "404", description = "Not Found: Challenge not found")
    	    }
    	)
    	@GetMapping("/challenges/progress")
    	public ResponseEntity<Float> getChallengeProgress(
    	    @RequestParam(name = "challengeName", required = true) String challengeName,
    	    @RequestBody List<SesionDTO> userSessions) {
    	    try {
    	        List<Sesion> sessions = userSessions.stream()
    	                .map(SesionAssembler::toEntity)
    	                .toList();
    	        float progress = stravaService.consultarProgresoReto(challengeName, sessions);
    	        return ResponseEntity.ok(progress);
    	    } catch (RuntimeException e) {
    	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	    }
    	}

}
