package com.example.demo.CapaSeguridad.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EmailAlreadyExitsException.class)
    protected ResponseEntity<ErrorMessage> conflicto_email(){
        ErrorMessage error =  new ErrorMessage(409,"El correo electrónico proporcionado ya está registrado.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessage> conflicto_nickname(){
        ErrorMessage error = new ErrorMessage(409,"El nickname ya esta en uso. Por favor, elija un nickname diferente.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(EmailPasswordException.class)
    protected ResponseEntity<ErrorMessage> conflicto_signin(){
        ErrorMessage error = new ErrorMessage(400,"Incorrect Email or Password");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    protected ResponseEntity<ErrorMessage> itemNoEncontrado(){
        ErrorMessage error = new ErrorMessage(404,"Item no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(HiloNotFoundException.class)
    protected ResponseEntity<ErrorMessage> hiloNoEncontrado(){
        ErrorMessage error = new ErrorMessage(404,"Hilo no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorMessage> usuarioNoEncontrado(){
        ErrorMessage error = new ErrorMessage(404,"Usuario no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(NotNullPasswordException.class)
    protected ResponseEntity<ErrorMessage> passwordNoNulo(){
        ErrorMessage error = new ErrorMessage(400,"La contraseña no puede ser nula");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(ActualizacionException.class)
    protected ResponseEntity<ErrorMessage> actualizacionFallida(){
        ErrorMessage error = new ErrorMessage(403,"Solo se puede modificar el contenido de la respuesta");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    @ExceptionHandler(RespuestaNotFoundException.class)
    protected ResponseEntity<ErrorMessage> respuestaNoEncontrada(){
        ErrorMessage error = new ErrorMessage(404,"Respuesta no encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
