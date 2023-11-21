package com.example.demo.Hilos.domain;

import com.example.demo.Respuesta.domain.Respuesta;
import com.example.demo.Usuario.domain.Usuario;
import com.example.demo.Labels.domain.Label;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "hilo") // Configura el nombre de la tabla
public class Hilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 300)
    @Column(nullable = false)
    private String tema;

    @Size(max = 3000)
    @Column(nullable = false)
    private String contenido;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false) // Configura el nombre de la columna
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "hilo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Respuesta> respuestas = new ArrayList<>();

    private Long cantidadReaccciones;

    private Long cantidadReports;

    @ManyToMany
    @JoinTable(
            name = "hilo_label",
            joinColumns = @JoinColumn(name = "hilo_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels = new ArrayList<>();
    public Hilo() {
    }

    public Hilo(Long id, String tema, String contenido, Date fechaCreacion, Usuario usuario, Long cantidadReaccciones, Long cantidadReports, List<Label> labels, List<Respuesta> respuestas) {
        this.id = id;
        this.tema = tema;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
        this.cantidadReaccciones = cantidadReaccciones;
        this.cantidadReports = cantidadReports;
        this.labels = labels;
        this.respuestas = respuestas;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {

        this.contenido = contenido;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getCantidadReaccciones() {
        return cantidadReaccciones;
    }

    public void setCantidadReaccciones(Long cantidadReaccciones) {
        this.cantidadReaccciones = cantidadReaccciones;
    }

    public Long getCantidadReports() {
        return cantidadReports;
    }

    public void setCantidadReports(Long cantidadReports) {
        this.cantidadReports = cantidadReports;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    // Métodos para manejar la relación
    public void addLabel(Label label) {
        labels.add(label);
        label.getHilos().add(this); // Establecer la relación inversa
    }

    public void removeLabel(Label label) {
        labels.remove(label);
        label.getHilos().remove(this); // Eliminar la relación inversa
    }
    @PrePersist
    protected void onCreate() {
        fechaCreacion = convertirFechaHoraATimeZonePeru(ZonedDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        fechaCreacion = convertirFechaHoraATimeZonePeru(ZonedDateTime.now());
    }

    private Date convertirFechaHoraATimeZonePeru(ZonedDateTime fechaHoraUTC) {
        ZonedDateTime fechaHoraPeru = fechaHoraUTC.withZoneSameInstant(ZoneId.of("America/Lima"));
        return Date.from(fechaHoraPeru.toInstant());
    }



}
