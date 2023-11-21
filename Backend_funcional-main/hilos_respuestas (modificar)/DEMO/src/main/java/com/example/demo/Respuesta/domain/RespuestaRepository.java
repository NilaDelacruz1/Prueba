package com.example.demo.Respuesta.domain;

import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    List<Respuesta> findByUsuariosParticipantes(Usuario usuario);
    List<Respuesta> findByHilo(Hilo hilo);
    List<Respuesta> findByRespuestaPadreIsNull();
    List<Respuesta> findByRespuestaPadreId(Long respuestaPadreId);
}