package com.example.demo.Hilos.application;

import com.example.demo.CapaSeguridad.exception.HiloNotFoundException;
import com.example.demo.Hilos.hilosDTO.HiloDTO;
import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Hilos.domain.HiloRepository;
import com.example.demo.Labels.domain.Label;
import com.example.demo.Labels.domain.LabelRepository;
import com.example.demo.Respuesta.domain.Respuesta;
import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Usuario.domain.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hilos")
@CrossOrigin(origins = "http://localhost:3000")
public class HiloController {

    @Autowired
    private HiloRepository hiloRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LabelRepository labelRepository;

    // Convierte una entidad Hilo en un HiloDTO
    private HiloDTO convertToDTO(Hilo hilo) {
        HiloDTO dto = new HiloDTO();
        dto.setId(hilo.getId());
        dto.setTema(hilo.getTema());
        dto.setContenido(hilo.getContenido());
        dto.setFechaCreacion(hilo.getFechaCreacion());
        dto.setCantidadReaccciones(hilo.getCantidadReaccciones());

        if (hilo.getUsuario() != null) {
            dto.setUserId(hilo.getUsuario().getId());
            dto.setUserNickname(hilo.getUsuario().getNickname());
            if(hilo.getUsuario().getImage_path() != null){
                dto.setImage_path("http://localhost:8080/usuarios/" + hilo.getUsuario().getId() + "/profile_picture");
            }
        }
        // Obtener los valores de las etiquetas asociadas
        List<String> etiquetas = hilo.getLabels()
                .stream()
                .map(label -> label.getValor())
                .collect(Collectors.toList());
        dto.setLabelValores(etiquetas);

        // Obtiene solo los IDs de las respuestas asociadas
        List<Long> respuestaIds = hilo.getRespuestas()
                .stream()
                .map(Respuesta::getId)
                .collect(Collectors.toList());
        dto.setRespuestaIds(respuestaIds);

        return dto;
    }

    // Endpoint para obtener todos los hilos como DTOs
    @GetMapping
    public List<HiloDTO> getHilos() {
        List<Hilo> hilos = hiloRepository.findAll();
        return hilos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{hiloId}")
    public ResponseEntity<?> getHilobyId(@PathVariable Long hiloId) {
        Optional<Hilo> optionalHilo = hiloRepository.findById(hiloId);

        if (optionalHilo.isPresent()) {
            Hilo hilo = optionalHilo.get();
            HiloDTO newHilo = new HiloDTO();
            newHilo.setId(hilo.getId());
            newHilo.setTema(hilo.getTema());
            newHilo.setContenido(hilo.getContenido());
            newHilo.setCantidadReaccciones(hilo.getCantidadReaccciones());
            if (hilo.getUsuario() != null) {
                newHilo.setUserId(hilo.getUsuario().getId());
                newHilo.setUserNickname(hilo.getUsuario().getNickname());
                if(hilo.getUsuario().getImage_path() != null){
                    newHilo.setImage_path("http://localhost:8080/usuarios/" + hilo.getUsuario().getId() + "/profile_picture");
                }

            }
            newHilo.setFechaCreacion(hilo.getFechaCreacion());
            for (Respuesta respuesta : hilo.getRespuestas()) {
                newHilo.getRespuestaIds().add(respuesta.getId());
            }
            return ResponseEntity.ok(newHilo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hilo con ID " + hiloId + " no encontrado");
        }
    }

    @PostMapping("/{idUser}")
    public ResponseEntity<HiloDTO> createHilo(@RequestBody HiloDTO hiloDTO, @PathVariable Long idUser) {
        Usuario usuario = usuarioRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Hilo hilo = new Hilo();
        hilo.setUsuario(usuario);
        hilo.setTema(hiloDTO.getTema());
        hilo.setContenido(hiloDTO.getContenido());
        hilo.setFechaCreacion(new Date());

        List<String> etiquetasAsociadas = hiloDTO.getLabelValores();

        List<Label> etiquetas = new ArrayList<>();

        for (String valor : etiquetasAsociadas) {
            Label label = labelRepository.findByValor(valor);

            if (label == null) {
                label = new Label(valor);
                label = labelRepository.save(label);
            }

            etiquetas.add(label);
        }

        hilo.setLabels(etiquetas);
        Hilo nuevoHilo = hiloRepository.save(hilo);

        hiloDTO.setId(nuevoHilo.getId());
        hiloDTO.setUserId(usuario.getId());
        hiloDTO.setUserNickname(usuario.getNickname());
        if(usuario.getImage_path() != null){
            hiloDTO.setImage_path("http://localhost:8080/usuarios/" + usuario.getId() + "/profile_picture");
        }
        hiloDTO.setFechaCreacion(nuevoHilo.getFechaCreacion());

        return new ResponseEntity<>(hiloDTO, HttpStatus.CREATED);
    }

    @GetMapping("/por_etiqueta/{nombreEtiqueta}")
    public ResponseEntity<List<HiloDTO>> obtenerHilosPorEtiqueta(@PathVariable("nombreEtiqueta") String nombreEtiqueta) {
        List<Hilo> hilos = hiloRepository.findByNombreEtiqueta(nombreEtiqueta);

        if (hilos.isEmpty()) {
            throw new HiloNotFoundException();
        }

        List<HiloDTO> hiloDTOs = hilos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(hiloDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarHilo(@PathVariable("id") Long id) {
        Optional<Hilo> optionalHilo = hiloRepository.findById(id);

        if (optionalHilo.isPresent()) {
            Hilo hilo = optionalHilo.get();
            hiloRepository.delete(hilo);
            return ResponseEntity.ok("Hilo eliminado exitosamente");
        } else {
            throw new HiloNotFoundException();
        }
    }

    @PatchMapping("/{hiloId}")
    public ResponseEntity<HiloDTO> updateHilo(@PathVariable Long hiloId, @RequestBody HiloDTO hiloDTO) {
        Hilo hilo = hiloRepository.findById(hiloId)
                .orElseThrow(() -> new EntityNotFoundException("Hilo not found"));

        // Actualiza los campos del hilo con los valores proporcionados en el DTO
        hilo.setTema(hiloDTO.getTema());
        hilo.setContenido(hiloDTO.getContenido());

        // Obtener los valores de las etiquetas desde el DTO
        List<String> etiquetasAsociadas = hiloDTO.getLabelValores();

        List<Label> etiquetas = new ArrayList<>();

        // Iterar sobre los valores de las etiquetas proporcionados en el DTO
        for (String valor : etiquetasAsociadas) {
            Label label = labelRepository.findByValor(valor);

            if (label == null) {
                label = new Label(valor);
                label = labelRepository.save(label);
            }

            etiquetas.add(label);
        }

        // Actualizar las etiquetas del hilo
        hilo.setLabels(etiquetas);

        // Guardar los cambios en la base de datos
        Hilo updatedHilo = hiloRepository.save(hilo);

        // Crear el DTO actualizado del hilo
        HiloDTO updatedHiloDTO = new HiloDTO();
        updatedHiloDTO.setId(updatedHilo.getId());
        updatedHiloDTO.setTema(updatedHilo.getTema());
        updatedHiloDTO.setContenido(updatedHilo.getContenido());
        updatedHiloDTO.setFechaCreacion(updatedHilo.getFechaCreacion());
        updatedHiloDTO.setCantidadReaccciones(updatedHilo.getCantidadReaccciones());
        updatedHiloDTO.setUserId(updatedHilo.getUsuario().getId());
        updatedHiloDTO.setUserNickname(updatedHilo.getUsuario().getNickname());
        if(updatedHilo.getUsuario().getImage_path() != null){
            updatedHiloDTO.setImage_path("http://localhost:8080/usuarios/" + updatedHilo.getUsuario().getId() + "/profile_picture");
        }
        updatedHiloDTO.setLabelValores(etiquetasAsociadas);

        return ResponseEntity.ok(updatedHiloDTO);
    }

}
