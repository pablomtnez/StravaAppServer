package es.deusto.sd.strava.dto;

import java.util.Date;

public class UserDTO {
	
	private int tipo;
	private String email;
	private String nombre;
    private Date fechaNacimiento;
    private float peso;
	private float altura;
	private float frecuenciaCardiacaMaxima;
	private float frecuenciaCardiacaReposo;
	
	public UserDTO(int tipo, String email, String nombre, Date fechaNacimiento, float peso, float altura,
			float frecuenciaCardiacaMaxima, float frecuenciaCardiacaReposo) {
		super();
		this.tipo = tipo;
		this.email = email;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.peso = peso;
		this.altura = altura;
		this.frecuenciaCardiacaMaxima = frecuenciaCardiacaMaxima;
		this.frecuenciaCardiacaReposo = frecuenciaCardiacaReposo;
	}

	public UserDTO() {
		super();
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getAltura() {
		return altura;
	}

	public void setAltura(float altura) {
		this.altura = altura;
	}

	public float getFrecuenciaCardiacaMaxima() {
		return frecuenciaCardiacaMaxima;
	}

	public void setFrecuenciaCardiacaMaxima(float frecuenciaCardiacaMaxima) {
		this.frecuenciaCardiacaMaxima = frecuenciaCardiacaMaxima;
	}

	public float getFrecuenciaCardiacaReposo() {
		return frecuenciaCardiacaReposo;
	}

	public void setFrecuenciaCardiacaReposo(float frecuenciaCardiacaReposo) {
		this.frecuenciaCardiacaReposo = frecuenciaCardiacaReposo;
	}

	@Override
	public String toString() {
		return "UserDTO [tipo=" + tipo + ", email=" + email + ", nombre=" + nombre + ", fechaNacimiento="
				+ fechaNacimiento + ", peso=" + peso + ", altura=" + altura + ", frecuenciaCardiacaMaxima="
				+ frecuenciaCardiacaMaxima + ", frecuenciaCardiacaReposo=" + frecuenciaCardiacaReposo + "]";
	}
	
	
}
