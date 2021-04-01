package com.example.demo.Security.Model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UsuarioPrincipal implements UserDetails {
    private String nombreUsuario;
    private String contrasenia;
    private Collection<? extends GrantedAuthority> authorities;


    public static UsuarioPrincipal build(Usuario usuario){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRol()));
        return new UsuarioPrincipal(usuario.getNombreUsuario(),usuario.getContrasenia(),authorities);
    }

    public UsuarioPrincipal(String nombreUsuario, String contrasenia,  Collection<? extends GrantedAuthority> authorities) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
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

}
