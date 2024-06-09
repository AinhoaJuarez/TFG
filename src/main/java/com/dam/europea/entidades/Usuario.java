package com.dam.europea.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", userName=" + userName + ", pass=" + pass + ", rol=" + rol + "]";
	}
	@Id
    private String idUsuario;
    @Column
    private String userName;
    private String pass;
    private String rol;
    
    public Usuario(String userName, String pass) {
		this.userName = userName;
		this.pass = pass;
	}
    
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Usuario() {

	}
    
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsr) {
		this.idUsuario = idUsr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

   
}