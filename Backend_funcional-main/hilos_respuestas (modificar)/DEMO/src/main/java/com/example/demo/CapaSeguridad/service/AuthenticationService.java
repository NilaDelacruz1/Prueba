package com.example.demo.CapaSeguridad.service;

import com.example.demo.CapaSeguridad.domain.ResponseDTO;
import com.example.demo.CapaSeguridad.domain.Role;
import com.example.demo.CapaSeguridad.dto.JwtAuthenticationResponse;
import com.example.demo.CapaSeguridad.dto.SignUpRequest;
import com.example.demo.CapaSeguridad.dto.SigninRequest;
import com.example.demo.CapaSeguridad.exception.EmailAlreadyExitsException;
import com.example.demo.CapaSeguridad.exception.EmailPasswordException;
import com.example.demo.CapaSeguridad.exception.ErrorMessage;
import com.example.demo.CapaSeguridad.exception.UserAlreadyExistsException;
import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Usuario.domain.UsuarioRepository;
import com.example.demo.Usuario.domain.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class AuthenticationService {
    private final UsuarioRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;

    @Autowired
    public AuthenticationService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, UsuarioService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioService = userService;
    }

    public ResponseDTO signup(SignUpRequest request) {
        if (usuarioService.existsUserByEmail(request.getEmail())) {
            throw new EmailAlreadyExitsException();
        }
        if (usuarioService.existUserByNickname(request.getNickname())) {
            throw new UserAlreadyExistsException();
        }

        var user = new Usuario();
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        ResponseDTO info = new ResponseDTO();
        info.setId(user.getId());
        info.setToken(jwt);
        return info;
    }

    public ResponseDTO signin(SigninRequest request) {
        Usuario user = usuarioService.getUserByEmail(request.getEmail());

        //ResponseDTO responseDTO = new ResponseDTO();

        // Verificar si la contraseña proporcionada coincide con la del usuario en la base de datos
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EmailPasswordException();
        }

        // Construir el DTO de respuesta con la información del usuario y el token JWT
        ResponseDTO info = new ResponseDTO();
        info.setId(user.getId());
        info.setNickName(user.getNickname());

        // Aquí puedes agregar la lógica para configurar las imágenes si es necesario
        if (user.getImage_path() != null) {
            info.setImage_path("http://localhost:8080/usuarios/" + user.getId() + "/profile_picture");
        }
        if (user.getBackground_picture() != null) {
            info.setBackground_picture("http://localhost:8080/usuarios/" + user.getId() + "/banner_picture");
        }

        // Generar el token JWT para el usuario autenticado
        String jwt = jwtService.generateToken(user);
        info.setToken(jwt);

        return info;
    }


}
