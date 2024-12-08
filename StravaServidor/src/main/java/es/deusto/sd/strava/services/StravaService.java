package es.deusto.sd.strava.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.strava.dto.RetoDTO;
import es.deusto.sd.strava.dto.SesionDTO;
import es.deusto.sd.strava.dto.UserDTO;
import es.deusto.sd.strava.dto.UsuarioRetoDTO;
import es.deusto.sd.strava.entity.Deportes;
import es.deusto.sd.strava.entity.Reto;
import es.deusto.sd.strava.entity.Sesion;
import es.deusto.sd.strava.entity.UsuarioReto;
import es.deusto.sd.strava.entity.User;

@Service
public class StravaService {

    private static List<Sesion> sesiones = new ArrayList<>();
    private static List<Reto> retos = new ArrayList<>();
    private static List<UsuarioReto> usuariosRetos = new ArrayList<>();

    // --- Sesiones ---

    public void crearSesion(String titulo, float distancia, Date fechaInicio, Date horaInicio, float duracion, Deportes deporte) {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("El título de la sesión no puede estar vacío.");
        }
        if (distancia <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor a 0.");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula.");
        }
        if (horaInicio == null) {
            throw new IllegalArgumentException("La hora de inicio no puede ser nula.");
        }
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0.");
        }
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser nulo.");
        }

        Sesion sesion = new Sesion(titulo, distancia, fechaInicio, horaInicio, duracion, deporte);
        sesiones.add(sesion);
    }

    public List<SesionDTO> consultarUltimasSesiones() {
        return sesiones.stream()
                .sorted((s1, s2) -> s2.getFechaInicio().compareTo(s1.getFechaInicio()))
                .limit(5)
                .map(s -> new SesionDTO(
                        s.getTitulo(),
                        s.getDistancia(),
                        s.getFechaInicio(),
                        new SimpleDateFormat("HH:mm").format(s.getHoraInicio()),
                        s.getDuracion(),
                        s.getDeporte()
                ))
                .collect(Collectors.toList());
    }

    public List<SesionDTO> consultarSesionesPorFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null || fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("Las fechas deben ser válidas y fechaInicio no puede ser posterior a fechaFin.");
        }

        return sesiones.stream()
                .filter(s -> !s.getFechaInicio().before(fechaInicio) && !s.getFechaInicio().after(fechaFin))
                .map(s -> new SesionDTO(
                        s.getTitulo(),
                        s.getDistancia(),
                        s.getFechaInicio(),
                        new SimpleDateFormat("HH:mm").format(s.getHoraInicio()),
                        s.getDuracion(),
                        s.getDeporte()
                ))
                .collect(Collectors.toList());
    }

    // --- Retos ---

    public void crearReto(String nombre, Date fechaInicio, Date fechaFin, float distancia, float tiempo, Deportes deporte) {
        if (nombre == null || nombre.isEmpty() || fechaInicio == null || fechaFin == null || fechaInicio.after(fechaFin) || (distancia <= 0 && tiempo <= 0) || deporte == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios y deben tener valores válidos.");
        }

        Reto reto = new Reto(nombre, fechaInicio, fechaFin, distancia, tiempo, deporte);
        retos.add(reto);
    }

    public List<RetoDTO> consultarRetosActivos() {
        Date hoy = new Date();
        return retos.stream()
                .filter(r -> !r.getFechaFin().before(hoy))
                .sorted((r1, r2) -> r2.getFechaInicio().compareTo(r1.getFechaInicio()))
                .map(r -> new RetoDTO(
                        r.getNombre(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        r.getDistancia(),
                        r.getTiempo(),
                        r.getDeporte()
                ))
                .collect(Collectors.toList());
    }

    public void aceptarReto(String nombreReto, UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), userDTO.getNombre());
        Reto reto = retos.stream()
                .filter(r -> r.getNombre().equals(nombreReto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        UsuarioReto usuarioReto = new UsuarioReto(user, reto);
        usuariosRetos.add(usuarioReto);
    }

    public List<UsuarioRetoDTO> consultarRetosAceptados(UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), userDTO.getNombre());
        return usuariosRetos.stream()
                .filter(ur -> ur.getUsuario().equals(user))
                .map(ur -> new UsuarioRetoDTO(
                        ur.getUsuario(),
                        ur.getReto(),
                        ur.isCompletado()
                ))
                .collect(Collectors.toList());
    }

    public float consultarProgresoReto(String nombreReto, List<SesionDTO> sesionesUsuario) {
        Reto reto = retos.stream()
                .filter(r -> r.getNombre().equals(nombreReto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        float progresoDistancia = 0;
        float progresoTiempo = 0;

        for (SesionDTO sesionDTO : sesionesUsuario) {
            Sesion sesion = new Sesion(
                sesionDTO.getTitulo(),
                sesionDTO.getDistancia(),
                sesionDTO.getFechaInicio(),
                null, // Hora no necesaria para el progreso
                sesionDTO.getDuracion(),
                sesionDTO.getDeporte()
            );

            if (sesion.getDeporte() == reto.getDeporte() &&
                !sesion.getFechaInicio().before(reto.getFechaInicio()) &&
                !sesion.getFechaInicio().after(reto.getFechaFin())) {
                progresoDistancia += sesion.getDistancia();
                progresoTiempo += sesion.getDuracion();
            }
        }

        float porcentajeDistancia = reto.getDistancia() > 0 ? (progresoDistancia / reto.getDistancia()) * 100 : 0;
        float porcentajeTiempo = reto.getTiempo() > 0 ? (progresoTiempo / reto.getTiempo()) * 100 : 0;

        return Math.max(porcentajeDistancia, porcentajeTiempo);
    }
}
