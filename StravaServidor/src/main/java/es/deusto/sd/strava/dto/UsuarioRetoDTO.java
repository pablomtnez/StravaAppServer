/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.dto;

public class UsuarioRetoDTO {
	
	private UserDTO usuario;
	private RetoDTO reto;
	private boolean completado;
	
	public UsuarioRetoDTO(UserDTO usuario, RetoDTO reto, boolean completado) {
		super();
		this.usuario = usuario;
		this.reto = reto;
		this.completado = completado;
	}
	public UserDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UserDTO usuario) {
		this.usuario = usuario;
	}
	public RetoDTO getReto() {
		return reto;
	}
	public void setReto(RetoDTO reto) {
		this.reto = reto;
	}
	public boolean isCompletado() {
		return completado;
	}
	public void setCompletado(boolean completado) {
		this.completado = completado;
	}
	
	

}
