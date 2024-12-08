/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.dto;

import es.deusto.sd.strava.entity.User;
import es.deusto.sd.strava.entity.TipoUsuario;

public class UserAssembler {

    public static UserDTO toDTO(User user) {
        if (user.getTipoUsuario() == TipoUsuario.META) {
            return new UserDTO(
                1, // TipoUsuario META representado como 1
                user.getEmail(),
                user.getNombre(),
                user.getFechaNacimiento(),
                user.getPeso(),
                user.getAltura(),
                user.getFrecuenciaCardiacaMaxima(),
                user.getFrecuenciaCardiacaReposo()
            );
        } else if (user.getTipoUsuario() == TipoUsuario.GOOGLE) {
            return new UserDTO(
                0, // TipoUsuario GOOGLE representado como 0
                user.getEmail(),
                user.getNombre(),
                user.getFechaNacimiento(),
                user.getPeso(),
                user.getAltura(),
                user.getFrecuenciaCardiacaMaxima(),
                user.getFrecuenciaCardiacaReposo()
            );
        } else {
            throw new IllegalArgumentException("Unknown TipoUsuario: " + user.getTipoUsuario());
        }
    }

    public static User toEntity(UserDTO userDTO) {
        if (userDTO.getTipo() == 1) {
            return new User(
                TipoUsuario.META, // TipoUsuario META
                userDTO.getEmail(),
                userDTO.getNombre(),
                userDTO.getFechaNacimiento(),
                userDTO.getPeso(),
                userDTO.getAltura(),
                userDTO.getFrecuenciaCardiacaMaxima(),
                userDTO.getFrecuenciaCardiacaReposo()
            );
        } else if (userDTO.getTipo() == 0) {
            return new User(
                TipoUsuario.GOOGLE, // TipoUsuario GOOGLE
                userDTO.getEmail(),
                userDTO.getNombre(),
                userDTO.getFechaNacimiento(),
                userDTO.getPeso(),
                userDTO.getAltura(),
                userDTO.getFrecuenciaCardiacaMaxima(),
                userDTO.getFrecuenciaCardiacaReposo()
            );
        } else {
            throw new IllegalArgumentException("Unknown TipoUsuario value: " + userDTO.getTipo());
        }
    }
}
