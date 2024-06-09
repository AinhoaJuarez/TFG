package com.dam.europea.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

	@Override
	public String toString() {
		return nombre;
	}

	@Id
	private String dni;
	@Column
	private String direccion;
	private String codPos;
	private String localidad;
	private String nombre;
	@OneToMany(mappedBy = "numeroFactura", cascade = {CascadeType.MERGE})
	private List<Factura> facturas = new ArrayList<Factura>();
	@OneToMany(mappedBy = "numTicket", cascade = { CascadeType.MERGE})
	private List<Ticket> tickets = new ArrayList<Ticket>();

	public Cliente(String dni, String direccion, String codPos, String localidad, String nombre, List<Factura> facturas,
			List<Ticket> tickets) {
		super();
		this.dni = dni;
		this.direccion = direccion;
		this.codPos = codPos;
		this.localidad = localidad;
		this.nombre = nombre;
		this.facturas = facturas;
		this.tickets = tickets;
	}

	// Constructor con parámetros para inicializar todos los campos de la clase
	// Cliente
	public Cliente(String dni, String direccion, String codPos, String localidad, String nombre,
			ArrayList<Factura> facturas, ArrayList<Ticket> tickets) {
		super();
		this.dni = dni;
		this.direccion = direccion;
		this.codPos = codPos;
		this.localidad = localidad;
		this.nombre = nombre;
		this.facturas = facturas;
		this.tickets = tickets;
	}

	// Métodos getters y setters para cada uno de los campos de la clase Cliente
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

	// Constructor con parámetros para inicializar DNI y listas de facturas y
	// tickets
	public Cliente(String dni, ArrayList<Factura> facturas, ArrayList<Ticket> tickets) {
		super();
		this.dni = dni;
		this.facturas = facturas;
		this.tickets = tickets;
	}


	// Constructor vacío necesario para JPA
	public Cliente() {
		super();
	}


	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getDni() {
		return dni;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}

	public List<Factura> getFactura() {
		return facturas;
	}

	public void setFactura(ArrayList<Factura> factura) {
		this.facturas = factura;
	}

	public List<Ticket> getTicket() {
		return tickets;
	}

	public void setTicket(ArrayList<Ticket> ticket) {
		this.tickets = ticket;
	}
}
