package com.example.demo.Respuesta.domain;

import com.example.demo.Hilos.domain.Hilo;
import com.example.demo.Usuario.domain.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.*;


@Entity
@Table(name = "respuesta")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_report", nullable = false)
    private Long isReport = 0L;

    @Size(max = 3000)
    @Column(nullable = false)
    private String contenido;

    @Column(name = "cantidad_reacciones", nullable = false)
    private int cantidadReacciones=0;

    @ManyToOne
    @JoinColumn(name = "hilo_id")
    private Hilo hilo;

    @ManyToOne
    @JoinColumn(name = "respuesta_padre_id") // Relación de autoreferencia@
    @JsonIgnore
    private Respuesta respuestaPadre;

    @OneToMany(mappedBy = "respuestaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> subrespuestas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "usuario_respuestas_participadas",
            joinColumns = @JoinColumn(name = "respuesta_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    @JsonIgnore
    private Set<Usuario> usuariosParticipantes = new HashSet<>();


    private String imagen;


    private Date fechaCreacion;


    // Constructor vacío
    public Respuesta() {
    }

    // Constructor con parámetros

    public Respuesta(Long id, Long isReport, String contenido, int cantidadReacciones, Hilo hilo, Respuesta respuestaPadre, List<Respuesta> subrespuestas, Usuario usuario, Set<Usuario> usuariosParticipantes, String imagen,
                     Date fechaCreacion) {
        this.id = id;
        this.isReport = isReport;
        this.contenido = contenido;
        this.cantidadReacciones = cantidadReacciones;
        this.hilo = hilo;
        this.respuestaPadre = respuestaPadre;
        this.subrespuestas = subrespuestas;
        this.usuario = usuario;
        this.usuariosParticipantes = usuariosParticipantes;
        this.imagen = imagen;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long isReport() {
        return isReport;
    }

    public void setReport(Long isReport) {
        this.isReport = isReport;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getCantidadReacciones() {
        return cantidadReacciones;
    }

    public void setCantidadReacciones(int cantidadReacciones) {
        this.cantidadReacciones = cantidadReacciones;
    }

    public Hilo getHilo() {
        return hilo;
    }

    public void setHilo(Hilo hilo) {
        this.hilo = hilo;
    }

    public Respuesta getRespuestaPadre() {
        return respuestaPadre;
    }

    public void setRespuestaPadre(Respuesta respuestaPadre) {
        this.respuestaPadre = respuestaPadre;
    }

    public List<Respuesta> getSubrespuestas() {
        return subrespuestas;
    }

    public void setSubrespuestas(List<Respuesta> subrespuestas) {
        this.subrespuestas = subrespuestas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Usuario> getUsuariosParticipantes() {
        return usuariosParticipantes;
    }

    public void setUsuariosParticipantes(Set<Usuario> usuariosParticipantes) {
        this.usuariosParticipantes = usuariosParticipantes;
    }

    public Long getIsReport() {
        return isReport;
    }

    public void setIsReport(Long isReport) {
        this.isReport = isReport;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}