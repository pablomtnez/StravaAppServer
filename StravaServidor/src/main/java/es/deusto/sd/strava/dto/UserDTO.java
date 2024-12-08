package es.deusto.sd.strava.dto;

public class UserDTO {

    private String email;
    private String password; // NUEVO ATRIBUTO
    private String nombre;

    public UserDTO() {
        super();
    }

    public UserDTO(String email, String password, String nombre) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "UserDTO [email=" + email + ", password=" + password + ", nombre=" + nombre + "]";
    }
}
