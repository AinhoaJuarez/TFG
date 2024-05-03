package com.dam.europea.entidades;


import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

	@Id
	@Column(name = "DNI", unique = true, nullable = false)
	private String dni;
	private String direccion;
	private String codPos;
	private String localidad;
	private String nombre;
	@OneToMany(mappedBy="numeroFactura", cascade= CascadeType.PERSIST)
	private ArrayList<Factura> facturas; 
	@OneToMany(mappedBy="numTicket", cascade= CascadeType.PERSIST)
	private ArrayList<Ticket> tickets; 
	
	
	public Cliente(String dni, String direccion, String codPos, String localidad, String nombre, ArrayList<Factura> factura,
			ArrayList<Ticket> ticket) {
		super();
		this.dni = dni;
		this.direccion = direccion;
		this.codPos = codPos;
		this.localidad = localidad;
		this.nombre = nombre;
		this.facturas = factura;
		this.tickets = ticket;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodPos() {
		return codPos;
	}

	public void setCodPos(String codPos) {
		this.codPos = codPos;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Lo pongo como String porque en principio al no querer operar con el nos puede
	// ahorrar errores
	

	// Para poder relacionarse con las clases de las que va a recibir/enviar Foreign
	// Key

	// Incluyo también contructores con varios parametros y vacio, por si nos
	// interesa recibir solo el DNI, todo o simplemente construir el objeto
	public Cliente(String dni, ArrayList<Factura> factura, ArrayList<Ticket> ticket) {
		super();
		this.dni = dni;
		this.facturas = factura;
		this.tickets = ticket;
	}

	public Cliente(String dni) {
		super();
		this.dni = dni;
	}

	public Cliente() {
		super();
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public ArrayList<Factura> getFactura() {
		return facturas;
	}

	public void setFactura(ArrayList<Factura> factura) {
		this.facturas = factura;
	}

	public ArrayList<Ticket> getTicket() {
		return tickets;
	}

	public void setTicket(ArrayList<Ticket> ticket) {
		this.tickets = ticket;
	}

}
