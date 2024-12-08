/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.dto;

import es.deusto.sd.strava.entity.Sesion;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SesionAssembler {

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public static SesionDTO toDTO(Sesion sesion) {
        return new SesionDTO(
            sesion.getTitulo(),
            sesion.getDistancia(),
            sesion.getFechaInicio(),
            timeFormatter.format(sesion.getHoraInicio()), // Formateo de la hora como String
            sesion.getDuracion(),
            sesion.getDeporte()
        );
    }

    public static Sesion toEntity(SesionDTO sesionDTO) {
        if (sesionDTO == null) {
            throw new IllegalArgumentException("El objeto SesionDTO no puede ser nulo.");
        }

        Date horaInicio;
        try {
            horaInicio = parseTime(sesionDTO.getHoraInicio()); // Conversión de hora de String a Date
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido para horaInicio. Se esperaba HH:mm.", e);
        }

        return new Sesion(
            sesionDTO.getTitulo(),
            sesionDTO.getDistancia(),
            sesionDTO.getFechaInicio(),
            horaInicio,
            sesionDTO.getDuracion(),
            sesionDTO.getDeporte()
        );
    }

    // Método estático para parsear hora en formato HH:mm a un objeto Date
    public static Date parseTime(String time) throws ParseException {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("El campo horaInicio no puede ser nulo o vacío.");
        }
        return timeFormatter.parse(time);
    }

}
