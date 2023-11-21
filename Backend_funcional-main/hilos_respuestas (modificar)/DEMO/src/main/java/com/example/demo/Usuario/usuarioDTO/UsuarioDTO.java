package com.example.demo.Usuario.usuarioDTO;


import jakarta.persistence.Lob;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

public class UsuarioDTO {
    private Long id;
    private String nickname;
    private String correo;

    private  String image_path;

    private String background_picture;

    private Set<Long> favoriteAnimeIds;

    private Set<Long> hilosCreados = new HashSet<>();

    private Set<Long> respuestasParticipadas = new HashSet<>();



    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nickname, String correo, String image_path, String background_picture, Set<Long> favoriteAnimeIds, Set<Long> hilosCreados, Set<Long> respuestasParticipadas) {
        this.id = id;
        this.nickname = nickname;
        this.correo = correo;
        this.image_path = image_path;
        this.background_picture = background_picture;
        this.favoriteAnimeIds = favoriteAnimeIds;
        this.hilosCreados = hilosCreados;
        this.respuestasParticipadas = respuestasParticipadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Set<Long> getFavoriteAnimeIds() {
        return favoriteAnimeIds;
    }

    public void setFavoriteAnimeIds(Set<Long> favoriteAnimeIds) {
        this.favoriteAnimeIds = favoriteAnimeIds;
    }

    public Set<Long> getHilosCreados() {
        return hilosCreados;
    }

    public void setHilosCreados(Set<Long> hilosCreados) {
        this.hilosCreados = hilosCreados;
    }

    public Set<Long> getRespuestasParticipadas() {
        return respuestasParticipadas;
    }

    public void setRespuestasParticipadas(Set<Long> respuestasParticipadas) {
        this.respuestasParticipadas = respuestasParticipadas;
    }

    public String getBackground_picture() {
        return background_picture;
    }

    public void setBackground_picture(String background_picture) {
        this.background_picture = background_picture;
    }
}
