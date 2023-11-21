package com.example.demo.Hilos.domain;


import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Usuario.domain.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiloService {

    @Autowired
    HiloRepository hiloRepository;

    @Autowired
    UsuarioService usuarioService;

    public List<Hilo> getThreadsCreatedByUser(Usuario usuario) {
        return hiloRepository.findByUsuario(usuario);
    }



}
