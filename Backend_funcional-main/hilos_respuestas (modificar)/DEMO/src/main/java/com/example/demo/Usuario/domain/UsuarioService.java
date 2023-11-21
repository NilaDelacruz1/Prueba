package com.example.demo.Usuario.domain;

import com.example.demo.CapaSeguridad.exception.EmailPasswordException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }



    public boolean existsUserByEmail(String email) {
        return usuarioRepository.findByEmail(email) != null;
    }
    public boolean existUserByNickname(String nickname){
        return usuarioRepository.findByNickname(nickname) != null;
    }

    public Usuario getUserByEmail(String email) {
        Usuario user = usuarioRepository.findByEmail(email);
        if(user == null){
            throw new EmailPasswordException();
        }
        else{
            return user;
        }
    }


    public List<Usuario> getUsuarios(){
        return usuarioRepository.findAll();
    }
    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public List<Long> getFavoriteAnimeIdsByUser(@NotNull Usuario usuario) {
        return new ArrayList<>(usuario.getFavoriteAnimeIds());
    }

    public void deleteUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            throw new EntityNotFoundException("User not found");
        }

        usuarioRepository.delete(usuario);
    }

    public Usuario createUser(Usuario user) {
        return usuarioRepository.save(user);
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepository.findByEmail(username);
            }
        };
    }
    public Usuario uploadPicture(Long userId, MultipartFile file, String pictureType) throws IOException {
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();

            // Obtén la ruta del directorio del proyecto
            String projectDirectory = System.getProperty("user.dir");

            // Verifica el tipo de imagen (profile o background)
            String relativeFolderPath;
            String pictureAttribute;
            if ("profile".equals(pictureType)) {
                relativeFolderPath = "/src/main/resources/profile-pictures/";
                pictureAttribute = "image_path";
            } else if ("background".equals(pictureType)) {
                relativeFolderPath = "/src/main/resources/background-pictures/";
                pictureAttribute = "background_picture";
            } else {
                throw new IllegalArgumentException("Invalid pictureType. Use 'profile' or 'background'.");
            }

            // Combina la ruta del proyecto con la ruta relativa
            java.nio.file.Path folderPath = Paths.get(projectDirectory, relativeFolderPath);

            // Asegúrate de que el directorio exista, si no, créalo
            Files.createDirectories(folderPath);

            // Elimina imágenes antiguas asociadas al usuario
            eliminarImagenesAntiguas(user, pictureType, folderPath);

            // Verifica la extensión del archivo
            String[] allowedExtensions = { "jpg", "jpeg", "png", "gif" };
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();

            if (Arrays.asList(allowedExtensions).contains(fileExtension)) {
                // Construye el nombre del archivo
                String fileName = "picture_" + pictureType + "_" + userId + "_" + System.currentTimeMillis() + "." + fileExtension;

                // Combina la ruta del directorio con el nombre del archivo
                Path filePath = folderPath.resolve(fileName);

                // Guarda el archivo en el sistema de archivos
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Actualiza la entidad de usuario con la ruta del archivo
                if ("profile".equals(pictureType)) {
                    user.setImage_path(filePath.toString());
                } else if ("background".equals(pictureType)) {
                    user.setBackground_picture(filePath.toString());
                }

                // Guarda el usuario actualizado en el repositorio
                return usuarioRepository.save(user);
            } else {
                throw new IllegalArgumentException("Tipo de archivo no admitido. Use imágenes con extensiones: " + Arrays.toString(allowedExtensions));
            }
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }


    //**CODIGO ACTUAL EN USO**
    /*public Usuario uploadPicture(Long userId, MultipartFile file, String pictureType) throws IOException {
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();

            // Obtén la ruta del directorio del proyecto
            String projectDirectory = System.getProperty("user.dir");

            // Verifica el tipo de imagen (profile o background)
            String relativeFolderPath;
            String pictureAttribute;
            if ("profile".equals(pictureType)) {
                relativeFolderPath = "/src/main/resources/profile-pictures/";
                pictureAttribute = "image_path";
            } else if ("background".equals(pictureType)) {
                relativeFolderPath = "/src/main/resources/background-pictures/";
                pictureAttribute = "background_picture";
            } else {
                throw new IllegalArgumentException("Invalid pictureType. Use 'profile' or 'background'.");
            }

            // Combina la ruta del proyecto con la ruta relativa
            java.nio.file.Path folderPath = Paths.get(projectDirectory, relativeFolderPath);

            // Asegúrate de que el directorio exista, si no, créalo
            Files.createDirectories(folderPath);

            // Elimina imágenes antiguas asociadas al usuario
            eliminarImagenesAntiguas(user, pictureType, folderPath);

            // Construye el nombre del archivo
            String fileName = "picture_" + pictureType + "_" + userId + "_" + System.currentTimeMillis() + ".jpg";

            // Combina la ruta del directorio con el nombre del archivo
            Path filePath = folderPath.resolve(fileName);

            // Guarda el archivo en el sistema de archivos
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Actualiza la entidad de usuario con la ruta del archivo
            if ("profile".equals(pictureType)) {
                user.setImage_path(filePath.toString());
            } else if ("background".equals(pictureType)) {
                user.setBackground_picture(filePath.toString());
            }

            // Guarda el usuario actualizado en el repositorio
            return usuarioRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }*/


    /*public Usuario uploadPicture(Long userId, MultipartFile file, String pictureType) throws IOException {
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();

            // Verifica el tipo de imagen (profile o background)
            String relativeFolderPath;
            String pictureAttribute;
            if ("profile".equals(pictureType)) {
                relativeFolderPath = "profile-pictures/";
                pictureAttribute = "image_path";
            } else if ("background".equals(pictureType)) {
                relativeFolderPath = "background-pictures/";
                pictureAttribute = "background_picture";
            } else {
                throw new IllegalArgumentException("Invalid pictureType. Use 'profile' or 'background'.");
            }

            // Combina la ruta relativa con el nombre del archivo
            String fileName = "picture_" + pictureType + "_" + userId + "_" + System.currentTimeMillis() + ".jpg";

            // Guarda el archivo en el sistema de archivos
            String filePath = relativeFolderPath + fileName;

            // Actualiza la entidad de usuario con la ruta del archivo
            if ("profile".equals(pictureType)) {
                user.setImage_path(filePath);
            } else if ("background".equals(pictureType)) {
                user.setBackground_picture(filePath);
            }

            // Guarda el usuario actualizado en el repositorio
            return usuarioRepository.save(user);
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }*/

    public void eliminarImagenesAntiguas(Usuario usuario, String pictureType, Path folderPath) throws IOException {
        // Elimina imágenes antiguas
        if ("profile".equals(pictureType)) {
            eliminarImagenAntigua(usuario.getImage_path(), folderPath);
        } else if ("background".equals(pictureType)) {
            eliminarImagenAntigua(usuario.getBackground_picture(), folderPath);
        }
    }

    public void eliminarImagenAntigua(String imagePath, Path folderPath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path oldFilePath = folderPath.resolve(Paths.get(imagePath).getFileName());
            Files.deleteIfExists(oldFilePath);
        }
    }






}
