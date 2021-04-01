package com.example.demo.Security.Controllers;

import com.example.demo.Security.Model.Jwt;
import com.example.demo.Security.Model.Usuario;
import com.example.demo.Security.Service.UsuarioService;
import com.example.demo.Security.jwt.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
@SessionAttributes("usuarioController")

public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @GetMapping("/listar")
    public List<Usuario> listar(){
        return usuarioService.listarUsuarios();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registrar")
    public List<String> registrarUsuario (@Valid @RequestBody Usuario usuario, BindingResult bd, SessionStatus sd){
        //Verificar si hay errores
        List<String> messageList = new ArrayList<>();
        String message="";
        String[] field = {"nombreUsuario"};
        if (bd.hasErrors()){
            for (String s : field) {
                if (bd.hasFieldErrors(s)) {
                    messageList.add(bd.getFieldError(s).getDefaultMessage());
                } else {
                    messageList.add(" ");
                }
            }
        } else {
            try {
                message = (usuario.getIdUsuario()==0)?"Se ha guardado el Usuario": "Datos actualizados";
                usuarioService.registrarUsuario(usuario);
                sd.setComplete();
            } catch (DataIntegrityViolationException e) {
                //message = getConstraintMessage(e.getMostSpecificCause().getMessage());
            } catch (Exception e) {
                message = ((usuario.getIdUsuario()==0)) ? "Error al crear el Usuario" : "Error al realizar los cambios";
            }
            messageList.add(message);
        }
        return messageList;
    }

    @GetMapping("/consultar/{id}")
    public Usuario consultarUsuario (@PathVariable(value = "id") Long idUsuario){
        return usuarioService.consultarUsuario(idUsuario);
    }


    @PostMapping("/login")
    public ResponseEntity<Jwt> login(@Valid @RequestBody Usuario loginUsuario){
        String mensaje = "usuario y/o constaseña incorrectos";
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getContrasenia()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String tokenJwt = jwtProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            Jwt jwt = new Jwt(tokenJwt, userDetails.getUsername(), userDetails.getAuthorities());
            return new ResponseEntity(jwt, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(mensaje, HttpStatus.BAD_REQUEST);
        }
    }
}
