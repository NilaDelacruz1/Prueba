package com.example.demo.Usuario.domain;

import com.example.demo.CapaSeguridad.domain.Role;
import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Respuesta.domain.Respuesta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "_usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    private String nickname;
    private String password;
    @Column(unique = true)
    private String email;

    private String image_path;

    private String background_picture;

    private String enlace_imagen;

    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection
    @CollectionTable(name = "usuario_animeFavorito", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "anime_id")
    private Set<Long> favoriteAnimeIds = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Hilo> hilosCreados = new HashSet<>();

    @ManyToMany(mappedBy = "usuariosParticipantes", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Respuesta> respuestasParticipadas = new HashSet<>();

    public Usuario() {
    }

    public Usuario(Long id, String nickname, String password, String email, String image_path, String background_picture, String enlace_imagen, Role role, Set<Long> favoriteAnimeIds, Set<Hilo> hilosCreados, Set<Respuesta> respuestasParticipadas) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.image_path = image_path;
        this.background_picture = background_picture;
        this.enlace_imagen = enlace_imagen;
        this.role = role;
        this.favoriteAnimeIds = favoriteAnimeIds;
        this.hilosCreados = hilosCreados;
        this.respuestasParticipadas = respuestasParticipadas;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Set<Hilo> getHilosCreados() {
        return hilosCreados;
    }

    public void setHilosCreados(Set<Hilo> hilosCreados) {
        this.hilosCreados = hilosCreados;
    }

    public Set<Respuesta> getRespuestasParticipadas() {
        return respuestasParticipadas;
    }

    public void setRespuestasParticipadas(Set<Respuesta> respuestasParticipadas) {
        this.respuestasParticipadas = respuestasParticipadas;
    }

    public String getBackground_picture() {
        return background_picture;
    }

    public void setBackground_picture(String background_picture) {
        this.background_picture = background_picture;
    }

    public String getEnlace_imagen() {
        return enlace_imagen;
    }

    public void setEnlace_imagen(String enlace_imagen) {
        this.enlace_imagen = enlace_imagen;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

}
