package com.example.demo.Hilos.hilosDTO;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HiloDTO {
    private Long id;


    @Size(max = 300)
    private String tema;

    @Size(max = 3000)
    private String contenido;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    private List<Long> respuestaIds = new ArrayList<>();
    private Long userId;
    private String userNickname;
    private String image_path;

    private List<String> labelValores = new ArrayList<>();

    private Long cantidadReaccciones;





    public HiloDTO() {
    }

    public HiloDTO(Long id, String tema, String contenido, Date fechaCreacion, List<Long> respuestaIds, Long userId, String userNickname, String image_path, List<String> labelValores, Long cantidadReaccciones) {
        this.id = id;
        this.tema = tema;
        this.contenido = contenido;
        this.fechaCreacion = fechaCreacion;
        this.respuestaIds = respuestaIds;
        this.userId = userId;
        this.userNickname = userNickname;
        this.image_path = image_path;
        this.labelValores = labelValores;
        this.cantidadReaccciones = cantidadReaccciones;
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

    public List<Long> getRespuestaIds() {
        return respuestaIds;
    }

    public void setRespuestaIds(List<Long> respuestaIds) {
        this.respuestaIds = respuestaIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public List<String> getLabelValores() {
        return labelValores;
    }

    public void setLabelValores(List<String> labelValores) {
        this.labelValores = labelValores;
    }

    public String getImage_path() {
        return image_path;
    }

    public Long getCantidadReaccciones() {
        return cantidadReaccciones;
    }

    public void setCantidadReaccciones(Long cantidadReaccciones) {
        this.cantidadReaccciones = cantidadReaccciones;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    @PrePersist
    protected void onCreate() {
        fechaCreacion = convertirFechaHoraATimeZonePeru(ZonedDateTime.now(ZoneId.of("UTC")).minusHours(6));
    }

    @PreUpdate
    protected void onUpdate() {
        fechaCreacion = convertirFechaHoraATimeZonePeru(ZonedDateTime.now(ZoneId.of("America/Lima")));
    }

    private Date convertirFechaHoraATimeZonePeru(ZonedDateTime fechaHoraUTC) {
        return Date.from(fechaHoraUTC.toInstant());
    }
}
