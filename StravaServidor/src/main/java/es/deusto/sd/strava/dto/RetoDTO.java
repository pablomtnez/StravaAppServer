package es.deusto.sd.strava.dto;

import java.util.Date;

import es.deusto.sd.strava.entity.Deportes;

public class RetoDTO {
	
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	private float distancia;
	private float tiempo;
	private Deportes deporte;
	
	public RetoDTO(String nombre, Date fechaInicio, Date fechaFin, float distancia, float tiempo, Deportes deporte) {
		super();
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.distancia = distancia;
		this.tiempo = tiempo;
		this.deporte = deporte;
	}

	public RetoDTO() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public float getDistancia() {
		return distancia;
	}

	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}

	public float getTiempo() {
		return tiempo;
	}

	public void setTiempo(float tiempo) {
		this.tiempo = tiempo;
	}

	public Deportes getDeporte() {
		return deporte;
	}

	public void setDeporte(Deportes deporte) {
		this.deporte = deporte;
	}

	@Override
	public String toString() {
		return "RetoDTO [nombre=" + nombre + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", distancia="
				+ distancia + ", tiempo=" + tiempo + ", deporte=" + deporte + "]";
	}
	
	
	
	
	
}
