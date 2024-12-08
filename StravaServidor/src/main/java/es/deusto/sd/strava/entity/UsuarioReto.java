/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.entity;

public class UsuarioReto {

    private User usuario; // Referencia al objeto User
    private Reto reto;    // Referencia al objeto Reto
    private boolean completado; // Para marcar si el reto ha sido completado

    public UsuarioReto(User usuario, Reto reto) {
        this.usuario = usuario;
        this.reto = reto;
        this.completado = false;
    }
    
    

    public UsuarioReto() {
		super();
	}



	public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Reto getReto() {
        return reto;
    }

    public void setReto(Reto reto) {
        this.reto = reto;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    @Override
    public String toString() {
        return "UsuarioReto [usuario=" + usuario.getNombre() + ", reto=" + reto.getNombre() + ", completado=" + completado + "]";
    }
}
