package es.deusto.sd.strava.dto;

import java.util.Date;

import es.deusto.sd.strava.entity.Deportes;

public class SesionDTO {
    private String titulo;
    private float distancia;
    private Date fechaInicio;
    private String horaInicio; // Cambiado de Date a String
    private float duracion;
    private Deportes deporte;

    public SesionDTO() {
        super();
    }

    public SesionDTO(String titulo, float distancia, Date fechaInicio, String horaInicio, float duracion,
            Deportes deporte) {
        super();
        this.titulo = titulo;
        this.distancia = distancia;
        this.fechaInicio = fechaInicio;
        this.horaInicio = horaInicio;
        this.duracion = duracion;
        this.deporte = deporte;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public Deportes getDeporte() {
        return deporte;
    }

    public void setDeporte(Deportes deporte) {
        this.deporte = deporte;
    }

    @Override
    public String toString() {
        return "SesionDTO [titulo=" + titulo + ", distancia=" + distancia + ", fechaInicio=" + fechaInicio
                + ", horaInicio=" + horaInicio + ", duracion=" + duracion + ", deporte=" + deporte + "]";
    }
}
