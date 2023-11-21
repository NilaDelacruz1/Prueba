package com.example.demo.Usuario.application;


import com.example.demo.CapaSeguridad.config.SecurityConfiguration;
import com.example.demo.CapaSeguridad.exception.EmailAlreadyExitsException;
import com.example.demo.CapaSeguridad.exception.NotNullPasswordException;
import com.example.demo.CapaSeguridad.exception.UserAlreadyExistsException;
import com.example.demo.CapaSeguridad.exception.UserNotFoundException;
import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Hilos.domain.HiloRepository;
import com.example.demo.Respuesta.domain.Respuesta;
import com.example.demo.Hilos.domain.HiloService;
import com.example.demo.Respuesta.domain.RespuestaService;
import com.example.demo.Usuario.domain.UsuarioRepository;
import com.example.demo.Usuario.usuarioDTO.UsuarioDTO;
import com.example.demo.Usuario.domain.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000") // Reemplaza con la URL de tu frontend
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    HiloService hiloService;

    @Autowired
    RespuestaService respuestaService;

    @Autowired
    HiloRepository hiloRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        List<Usuario> usuarios = usuarioService.getUsuarios();
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNickname(usuario.getNickname());
            usuarioDTO.setCorreo(usuario.getUsername());
            if (usuario.getImage_path() != null) {
                usuarioDTO.setImage_path("http://localhost:8080/usuarios/" + usuario.getId() + "/profile_picture");
            }
            if (usuario.getBackground_picture() != null) {
                usuarioDTO.setBackground_picture("http://localhost:8080/usuarios/" + usuario.getId() + "/banner_picture");
            }
            usuarioDTO.setFavoriteAnimeIds(usuario.getFavoriteAnimeIds());
            for (Hilo hilos : usuario.getHilosCreados()) {
                usuarioDTO.getHilosCreados().add(hilos.getId());
            }
            for (Respuesta respuesta : usuario.getRespuestasParticipadas()) {
                usuarioDTO.getRespuestasParticipadas().add(respuesta.getId());
            }
            usuarioDTOs.add(usuarioDTO);
        }
        return ResponseEntity.ok(usuarioDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUserById(id);
        if (usuario != null) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNickname(usuario.getNickname());
            usuarioDTO.setCorreo(usuario.getUsername());
            if (usuario.getImage_path() != null) {
                usuarioDTO.setImage_path("http://localhost:8080/usuarios/" + usuario.getId() + "/profile_picture");
            }
            if (usuario.getBackground_picture() != null) {
                usuarioDTO.setBackground_picture("http://localhost:8080/usuarios/" + usuario.getId() + "/banner_picture");
            }
            usuarioDTO.setFavoriteAnimeIds(usuario.getFavoriteAnimeIds());
            for (Hilo hilo : usuario.getHilosCreados()) {
                usuarioDTO.getHilosCreados().add(hilo.getId());
            }
            for (Respuesta respuesta : usuario.getRespuestasParticipadas()) {
                usuarioDTO.getRespuestasParticipadas().add(respuesta.getId());
            }
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/hilos-creados")
    public ResponseEntity<List<Hilo>> getThreadsCreatedByUser(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUserById(id);
        if (usuario != null) {
            List<Hilo> hilos_creados_usuario = hiloService.getThreadsCreatedByUser(usuario);
            return ResponseEntity.ok(hilos_creados_usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/respuestas-participadas")
    public ResponseEntity<List<Respuesta>> getResponsesParticipatedByUser(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUserById(id);
        if (usuario != null) {
            List<Respuesta> respuestas_usuario = respuestaService.getResponsesParticipatedByUser(usuario);
            return ResponseEntity.ok(respuestas_usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/animes-favoritos")
    public ResponseEntity<List<Long>> getFavoriteAnimesByUser(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUserById(id);
        if (usuario != null) {
            List<Long> favoriteAnimeIds = usuarioService.getFavoriteAnimeIdsByUser(usuario);
            return ResponseEntity.ok(favoriteAnimeIds);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            usuarioService.deleteUserById(id);
            return ResponseEntity.ok("User and associated data deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the user");
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario newUser = new Usuario();
            newUser.setNickname(usuarioDTO.getNickname());
            newUser.setEmail(usuarioDTO.getCorreo());
            newUser.setImage_path(usuarioDTO.getImage_path());
            newUser.setBackground_picture(usuarioDTO.getBackground_picture());
            usuarioService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the user");
        }
    }

    @PostMapping("/{usuarioId}/upload-picture")
    public ResponseEntity<Usuario> uploadPicture(
            @PathVariable("usuarioId") Long usuarioId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("pictureType") String pictureType
    ) {
        try {
            Usuario usuarioActualizado = usuarioService.uploadPicture(usuarioId, file, pictureType);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{usuario_id}/profile_picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable("usuario_id") Long usuarioId) {
        try {
            // Obtener el usuario por ID
            Usuario usuario = usuarioService.getUserById(usuarioId);

            if (usuario == null || usuario.getImage_path() == null) {
                // Manejar el caso en que el usuario o la imagen no existan
                return ResponseEntity.notFound().build();
            }

            // Leer la imagen como un array de bytes
            Path imagePath = Paths.get(usuario.getImage_path());
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Construir la respuesta con el contenido de la imagen y los encabezados adecuados
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Ajusta el tipo de contenido según el formato de tus imágenes

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{usuario_id}/banner_picture")
    public ResponseEntity<byte[]> getBanner_Picture(@PathVariable("usuario_id") Long usuarioId) {
        try {
            // Obtener el usuario por ID
            Usuario usuario = usuarioService.getUserById(usuarioId);

            if (usuario == null || usuario.getImage_path() == null) {
                // Manejar el caso en que el usuario o la imagen no existan
                return ResponseEntity.notFound().build();
            }

            // Leer la imagen como un array de bytes
            Path imagePath = Paths.get(usuario.getBackground_picture());
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Construir la respuesta con el contenido de la imagen y los encabezados adecuados
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Ajusta el tipo de contenido según el formato de tus imágenes

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Manejar la excepción de manera adecuada
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*@PatchMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long usuarioId, @RequestBody Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.getUserById(usuarioId);
        if(usuario == null) {
            throw new UserNotFoundException();
        }

        // Verificar si el nuevo nickname ya está asociado a otro usuario
        String newNickname = usuarioActualizado.getNickname();
        System.out.println(newNickname);
        if (usuario.getNickname().equals(newNickname) || usuarioService.existUserByNickname(newNickname) ) {
            throw new UserAlreadyExistsException(); // Puedes crear esta excepción personalizada
        }
        usuario.setNickname(usuarioActualizado.getNickname());



        // Verificar si el nuevo email ya está asociado a otro usuario
        String newEmail = usuarioActualizado.getEmail();
        System.out.println(newEmail);
        System.out.println(usuario.getEmail());
        if (usuario.getEmail().equals(newEmail) || usuarioService.existsUserByEmail(newEmail)) {
            System.out.println("entra");
            throw new EmailAlreadyExitsException(); // Puedes crear esta excepción personalizada
        }
        usuario.setEmail(usuarioActualizado.getEmail());



        // Verificar si se proporciona un nuevo password y codificarlo antes de guardarlo
        String newPassword = usuarioActualizado.getPassword();
        System.out.println(newPassword);
        if (newPassword == null || newPassword.isEmpty()) {
            throw new NotNullPasswordException();
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        System.out.println(encodedPassword);


        // Guardar los cambios en la base de datos
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        // Convertir el usuario actualizado a un DTO antes de devolverlo en la respuesta
        UsuarioDTO updatedUsuarioDTO = convertToDTO(updatedUsuario);

        return ResponseEntity.ok(updatedUsuarioDTO);
    }

    // Método para convertir un Usuario a un UsuarioDTO
    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNickname(usuario.getNickname());
        usuarioDTO.setCorreo(usuario.getEmail());
        if(usuario.getImage_path() != null){
            usuarioDTO.setImage_path("http://localhost:8080/usuarios/" + usuario.getId() + "/profile_picture");
        }
        if(usuario.getBackground_picture() != null){
            usuarioDTO.setBackground_picture("http://localhost:8080/usuarios/" + usuario.getId() + "/banner_picture");
        }
        return usuarioDTO;
    }*/
    /*@PatchMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long usuarioId, @RequestBody Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.getUserById(usuarioId);
        if (usuario == null) {
            throw new UserNotFoundException();
        }

        if (usuarioActualizado.getNickname() != null || !usuario.getNickname().equals(usuarioActualizado.getNickname())) {
            if (usuarioService.existUserByNickname(usuarioActualizado.getNickname())) {
                throw new UserAlreadyExistsException(); // Puedes crear esta excepción personalizada
            }
            usuario.setNickname(usuarioActualizado.getNickname());
        }

        if (usuarioActualizado.getEmail() != null || !usuario.getEmail().equals(usuarioActualizado.getEmail())) {
            if (usuarioService.existsUserByEmail(usuarioActualizado.getEmail())) {
                throw new EmailAlreadyExitsException(); // Puedes crear esta excepción personalizada
            }
            usuario.setEmail(usuarioActualizado.getEmail());
        }

        if (usuarioActualizado.getPassword() != null || !usuarioActualizado.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(usuarioActualizado.getPassword());
            usuario.setPassword(encodedPassword);
        }

        // Guardar los cambios en la base de datos
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        // Convertir el usuario actualizado a un DTO antes de devolverlo en la respuesta
        UsuarioDTO updatedUsuarioDTO = convertToDTO(updatedUsuario);

        return ResponseEntity.ok(updatedUsuarioDTO);
    }*/
    @PatchMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Long usuarioId, @RequestBody Usuario usuarioActualizado) {
        Usuario usuario = usuarioService.getUserById(usuarioId);
        if (usuario == null) {
            throw new UserNotFoundException();
        }

        // Verificar si el nuevo nickname ya está asociado a otro usuario
        String newNickname = usuarioActualizado.getNickname();
        if (newNickname != null && !usuario.getNickname().equals(newNickname) && usuarioService.existUserByNickname(newNickname)) {
            throw new UserAlreadyExistsException();
        }

        // Verificar si el nuevo email ya está asociado a otro usuario
        String newEmail = usuarioActualizado.getEmail();
        if (newEmail != null && !usuario.getEmail().equals(newEmail) && usuarioService.existsUserByEmail(newEmail)) {
            throw new EmailAlreadyExitsException();
        }

        // Actualizar los campos del usuario con los valores proporcionados
        if (usuarioActualizado.getNickname() != null) {
            usuario.setNickname(usuarioActualizado.getNickname());
        }

        if (usuarioActualizado.getEmail() != null) {
            usuario.setEmail(usuarioActualizado.getEmail());
        }

        if (usuarioActualizado.getPassword() != null) {
            String newPassword = usuarioActualizado.getPassword();
            if (!newPassword.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                usuario.setPassword(encodedPassword);
            } else {
                throw new NotNullPasswordException();
            }
        }

        // Guardar los cambios en la base de datos
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        // Convertir el usuario actualizado a un DTO antes de devolverlo en la respuesta
        UsuarioDTO updatedUsuarioDTO = convertToDTO(updatedUsuario);

        return ResponseEntity.ok(updatedUsuarioDTO);
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNickname(usuario.getNickname());
        usuarioDTO.setCorreo(usuario.getEmail());
        if (usuario.getImage_path() != null) {
            usuarioDTO.setImage_path("http://localhost:8080/usuarios/" + usuario.getId() + "/profile_picture");
        }
        if (usuario.getBackground_picture() != null) {
            usuarioDTO.setBackground_picture("http://localhost:8080/usuarios/" + usuario.getId() + "/banner_picture");
        }
        return usuarioDTO;
    }
}




















