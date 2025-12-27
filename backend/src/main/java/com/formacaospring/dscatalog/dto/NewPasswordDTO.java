package com.formacaospring.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

    @NotBlank(message = "Campo Obrigatório")
    private String token;

    @NotBlank(message = "Campo Obrigatório")
    @Size(min = 8, message = "Senha precisa ter no mínimo 8 caracteres")
    private String password;

    public NewPasswordDTO(){
    }

    public NewPasswordDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
