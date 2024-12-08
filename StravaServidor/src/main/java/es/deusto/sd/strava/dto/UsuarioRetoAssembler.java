/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */

package es.deusto.sd.strava.dto;

import es.deusto.sd.strava.entity.UsuarioReto;

public class UsuarioRetoAssembler {

    public static UsuarioRetoDTO toDTO(UsuarioReto usuarioReto) {
        return new UsuarioRetoDTO(
            UserAssembler.toDTO(usuarioReto.getUsuario()),
            RetoAssembler.toDTO(usuarioReto.getReto()),
            usuarioReto.isCompletado()
        );
    }

    public static UsuarioReto toEntity(UsuarioRetoDTO usuarioRetoDTO) {
        UsuarioReto usuarioReto = new UsuarioReto();
        usuarioReto.setUsuario(UserAssembler.toEntity(usuarioRetoDTO.getUsuario()));
        usuarioReto.setReto(RetoAssembler.toEntity(usuarioRetoDTO.getReto()));
        usuarioReto.setCompletado(usuarioRetoDTO.isCompletado());
        return usuarioReto;
    }
}
