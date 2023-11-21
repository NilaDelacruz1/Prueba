package com.example.demo.Hilos.domain;

import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HiloRepository extends JpaRepository<Hilo, Long> {
    List<Hilo> findByUsuario(Usuario usuario);

    @Query("SELECT h FROM Hilo h JOIN h.labels l WHERE l.valor = :nombreEtiqueta")
    List<Hilo> findByNombreEtiqueta(@Param("nombreEtiqueta") String nombreEtiqueta);
}
