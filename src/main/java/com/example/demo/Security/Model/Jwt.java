package com.example.demo.Security.Model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

public class Jwt {

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String bearer = "Bearer";

    @Getter
    @Setter
    private String nombreUsuario;

    @Getter
    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    public Jwt(String token, String nombreUsuario, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.nombreUsuario = nombreUsuario;
        this.authorities = authorities;
    }
}
