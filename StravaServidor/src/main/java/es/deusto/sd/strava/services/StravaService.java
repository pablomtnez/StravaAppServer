/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.deusto.sd.strava.entity.Sesion;
import es.deusto.sd.strava.entity.Deportes;
import es.deusto.sd.strava.entity.Reto;
import es.deusto.sd.strava.entity.UsuarioReto;
import es.deusto.sd.strava.entity.User;

@Service
public class StravaService {

    private static List<Sesion> sesiones = new ArrayList<>();
    private static List<Reto> retos = new ArrayList<>();
    private static List<UsuarioReto> usuariosRetos = new ArrayList<>();

    

    // --- Sesiones ---

    public void crearSesion(String titulo, float distancia, Date fechaInicio, Date horaInicio, float duracion, Deportes deporte) {
        // Validar los parámetros de entrada
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

        // Crear la sesión y agregarla a la lista de sesiones
        Sesion sesion = new Sesion(titulo, distancia, fechaInicio, horaInicio, duracion, deporte);
        sesiones.add(sesion);
    }


    public List<Sesion> consultarUltimasSesiones() {
        return sesiones.stream()
                .sorted((s1, s2) -> s2.getFechaInicio().compareTo(s1.getFechaInicio()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Sesion> consultarSesionesPorFechas(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null || fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("Las fechas deben ser válidas y fechaInicio no puede ser posterior a fechaFin.");
        }

        return sesiones.stream()
                .filter(s -> !s.getFechaInicio().before(fechaInicio) && !s.getFechaInicio().after(fechaFin))
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

    public List<Reto> consultarRetosActivos() {
        Date hoy = new Date();
        return retos.stream()
                .filter(r -> !r.getFechaFin().before(hoy))
                .sorted((r1, r2) -> r2.getFechaInicio().compareTo(r1.getFechaInicio()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Reto> consultarRetosPorFiltro(Date fechaInicio, Date fechaFin, Deportes deporte) {
        return retos.stream()
                .filter(r -> (fechaInicio == null || !r.getFechaInicio().before(fechaInicio)) &&
                             (fechaFin == null || !r.getFechaFin().after(fechaFin)) &&
                             (deporte == null || r.getDeporte() == deporte))
                .collect(Collectors.toList());
    }

    public void aceptarReto(String nombreReto, User usuario) {
        Reto reto = retos.stream()
                .filter(r -> r.getNombre().equals(nombreReto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        UsuarioReto usuarioReto = new UsuarioReto(usuario, reto);
        usuariosRetos.add(usuarioReto);
    }

    public List<UsuarioReto> consultarRetosAceptados(User usuario) {
        return usuariosRetos.stream()
                .filter(ur -> ur.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }

    public float consultarProgresoReto(String nombreReto, List<Sesion> sesionesUsuario) {
        Reto reto = retos.stream()
                .filter(r -> r.getNombre().equals(nombreReto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        float progresoDistancia = 0;
        float progresoTiempo = 0;

        for (Sesion sesion : sesionesUsuario) {
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
