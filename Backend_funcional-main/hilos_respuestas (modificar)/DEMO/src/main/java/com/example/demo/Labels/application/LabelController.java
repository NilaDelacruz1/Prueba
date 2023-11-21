package com.example.demo.Labels.application;

import com.example.demo.CapaSeguridad.exception.ItemNotFoundException;
import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Hilos.domain.HiloRepository;
import com.example.demo.Labels.domain.Label;
import com.example.demo.Labels.domain.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
public class LabelController {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private HiloRepository hiloRepository;

    @GetMapping
    public ResponseEntity<List<Label>> obtenerTodasLasEtiquetas() {
        List<Label> etiquetas = labelRepository.findAll();

        if (etiquetas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(etiquetas);
    }

    @GetMapping("/{valor}")
    public ResponseEntity<Label> obtenerEtiquetaPorValor(@PathVariable("valor") String valor) {
        Label etiqueta = labelRepository.findByValor(valor);

        if (etiqueta == null) {
            throw new ItemNotFoundException();
        }

        return ResponseEntity.ok(etiqueta);
    }

    @DeleteMapping("/{valor}")
    public ResponseEntity<String> borrarEtiquetaPorValor(@PathVariable("valor") String valor) {
        Label etiqueta = labelRepository.findByValor(valor);

        if (etiqueta == null) {
            throw new ItemNotFoundException();
        }

        List<Hilo> hilosConEtiqueta = hiloRepository.findByNombreEtiqueta(valor);
        for (Hilo hilo : hilosConEtiqueta) {
            hilo.removeLabel(etiqueta); // Elimina la etiqueta de la lista de etiquetas del hilo
            hiloRepository.save(hilo); // Guarda el hilo actualizado
        }

        labelRepository.delete(etiqueta); // Elimina la etiqueta de la base de datos
        return ResponseEntity.ok("Etiqueta y asociaciones eliminadas con éxito");
    }




}

