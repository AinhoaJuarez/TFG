package com.dam.europea.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    

	@Id
    private int idUsuario;
    @Column
    private String userName;
    private String pass;
    
    public Usuario(String userName, String pass) {
		this.userName = userName;
		this.pass = pass;
	}
    
	public Usuario() {

	}
    
	public int getIdUsr() {
		return idUsuario;
	}
	public void setIdUsr(int idUsr) {
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