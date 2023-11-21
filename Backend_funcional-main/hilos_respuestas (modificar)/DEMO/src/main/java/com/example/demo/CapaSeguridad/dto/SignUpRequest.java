package com.example.demo.CapaSeguridad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignUpRequest {

    @NotBlank(message = "El campo 'nickname' es obligatorio.")
    private String nickname;
    @NotBlank(message = "El campo 'email' es obligatorio.")
    @Email(message = "El campo 'email' debe ser una dirección de correo válida.")
    private String email;
    @NotBlank(message = "El campo 'password' es obligatorio.")
    private String password;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public SignUpRequest() {
    }

    public SignUpRequest( String nickname,String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }





    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
