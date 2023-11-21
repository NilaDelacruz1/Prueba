package com.example.demo.Respuesta.domain;

import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Usuario.domain.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespuestaService {
    @Autowired
    RespuestaRepository respuestaRepository;
    @Autowired
    UsuarioService usuarioService;

    public List<Respuesta> getResponsesParticipatedByUser(Usuario usuario) {
        return respuestaRepository.findByUsuariosParticipantes(usuario);
    }

}
