package es.deusto.sd.strava.entity;

import java.util.Date;

public class Sesion {
	
	private String titulo;
	private float distancia;
	private Date fechaInicio;
	private Date horaInicio; // Mantenemos Date para la lógica interna
	private float duracion;
	private Deportes deporte;
	
	public Sesion(String titulo, float distancia, Date fechaInicio, Date horaInicio, float duracion, Deportes deporte) {
		super();
		this.titulo = titulo;
		this.distancia = distancia;
		this.fechaInicio = fechaInicio;
		this.horaInicio = horaInicio;
		this.duracion = duracion;
		this.deporte = deporte;
	}

	public Sesion() {
		super();
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

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
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
		return "Sesion [titulo=" + titulo + ", distancia=" + distancia + ", fechaInicio=" + fechaInicio
				+ ", horaInicio=" + horaInicio + ", duracion=" + duracion + ", deporte=" + deporte + "]";
	}	

}
