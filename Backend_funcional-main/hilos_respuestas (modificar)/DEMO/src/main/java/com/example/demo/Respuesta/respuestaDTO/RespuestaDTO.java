package com.example.demo.Respuesta.respuestaDTO;

import com.example.demo.Respuesta.domain.Respuesta;
import com.example.demo.Usuario.usuarioDTO.UsuarioDTO_thread;
import jakarta.validation.constraints.Size;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RespuestaDTO {

    private Long id;
    @Size(max = 3000)
    private String contenido;
    private List<Long> subRespuestaIds = new ArrayList<>();

    private Long RespuestaPadreId;

    private Long HiloId;

    private String nickname;

    private String imagen;

    private Long report = 0L ;

    private int cantidadReacciones = 0;

    private Date fechaCreacion;

    public RespuestaDTO() {
    }


    public RespuestaDTO(String contenido, List<Long> subRespuestaIds, Long hiloId, String nickname, String imagen,Long RespuestaPadreId,Long report,int cantidadReacciones,Long id,
                        Date fechaCreacion) {
        this.contenido = contenido;
        this.subRespuestaIds = subRespuestaIds;
        HiloId = hiloId;
        this.nickname = nickname;
        this.imagen = imagen;
        this.RespuestaPadreId = RespuestaPadreId;
        this.report = report;
        this.cantidadReacciones = cantidadReacciones;
        this.id = id;
        this.fechaCreacion = fechaCreacion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<Long> getSubRespuestaIds() {
        return subRespuestaIds;
    }

    public void setSubRespuestaIds(List<Long> subRespuestaIds) {
        this.subRespuestaIds = subRespuestaIds;
    }

    public Long getHiloId() {
        return HiloId;
    }

    public void setHiloId(Long hiloId) {
        HiloId = hiloId;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Long getRespuestaPadreId() {
        return RespuestaPadreId;
    }

    public void setRespuestaPadreId(Long respuestaPadreId) {
        RespuestaPadreId = respuestaPadreId;
    }

    public Long getReport() {
        return report;
    }

    public void setReport(Long report) {
        this.report = report;
    }

    public int getCantidadReacciones() {
        return cantidadReacciones;
    }

    public void setCantidadReacciones(int cantidadReacciones) {
        this.cantidadReacciones = cantidadReacciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
