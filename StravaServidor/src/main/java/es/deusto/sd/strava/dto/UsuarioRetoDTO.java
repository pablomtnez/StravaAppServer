package es.deusto.sd.strava.dto;

import es.deusto.sd.strava.entity.Reto;
import es.deusto.sd.strava.entity.User;

public class UsuarioRetoDTO {

    private User usuario;
    private Reto reto;
    private boolean completado;

    // Constructor vacío
    public UsuarioRetoDTO() {
    }

    // Constructor con todos los campos
    public UsuarioRetoDTO(User usuario, Reto reto, boolean completado) {
        this.usuario = usuario;
        this.reto = reto;
        this.completado = completado;
    }

    // Getters y setters
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
}
