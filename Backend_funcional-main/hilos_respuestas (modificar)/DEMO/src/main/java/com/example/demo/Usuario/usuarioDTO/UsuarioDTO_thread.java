package com.example.demo.Usuario.usuarioDTO;

public class UsuarioDTO_thread {
    private String nickname;
    private  String image_path;

    public UsuarioDTO_thread(String nickname, String image_path) {
        this.nickname = nickname;
        this.image_path = image_path;
    }

    public UsuarioDTO_thread() {
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
}
