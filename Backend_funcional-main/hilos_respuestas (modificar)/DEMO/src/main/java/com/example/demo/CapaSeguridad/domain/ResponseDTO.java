package com.example.demo.CapaSeguridad.domain;

public class ResponseDTO {
    private Long id;
    private String token;
    private String nickName;

    private String image_path;
    private String background_picture;

    public ResponseDTO(Long id, String token, String nickName, String image_path, String background_picture) {
        this.id = id;
        this.token = token;
        this.nickName = nickName;
        this.image_path = image_path;
        this.background_picture = background_picture;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public ResponseDTO(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getBackground_picture() {
        return background_picture;
    }

    public void setBackground_picture(String background_picture) {
        this.background_picture = background_picture;
    }
}
