package com.dam.europea.entidades;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Factura {
	@Id
	private int numeroFactura;
	
	@ManyToOne
    @JoinColumn(name = "ClienteAsociado_fk", referencedColumnName= "DNI")
	
	private Cliente cliente;
	@Column
	private String fechaExpedicion;
	private String fechaOperacion;
	private String NIF;
	private String direccion;
	private int telefono;
	private String nombreApellidos;
	@OneToOne(mappedBy = "numerofactura", cascade = CascadeType.ALL, orphanRemoval = true)
	private TicketProductos ticketProductos;
	private int IVA;
	private double totalSinIVA;
	private double totalConIVA;
	public Factura(int numeroFactura, Cliente cliente, String fechaExpedicion, String fechaOperacion, String nIF,
			String direccion, int telefono, String nombreApellidos, TicketProductos ticketProductos, int iVA,
			double totalSinIVA, double totalConIVA, double descuento) {
		super();
		this.numeroFactura = numeroFactura;
		this.cliente = cliente;
		this.fechaExpedicion = fechaExpedicion;
		this.fechaOperacion = fechaOperacion;
		NIF = nIF;
		this.direccion = direccion;
		this.telefono = telefono;
		this.nombreApellidos = nombreApellidos;
		this.ticketProductos = ticketProductos;
		IVA = iVA;
		this.totalSinIVA = totalSinIVA;
		this.totalConIVA = totalConIVA;
		this.descuento = descuento;
	}
	public Factura() {
	}
	private double descuento;
	public int getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getFechaExpedicion() {
		return fechaExpedicion;
	}
	public void setFechaExpedicion(String fechaExpedicion) {
		this.fechaExpedicion = fechaExpedicion;
	}
	public String getFechaOperacion() {
		return fechaOperacion;
	}
	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	public String getNIF() {
		return NIF;
	}
	public void setNIF(String nIF) {
		NIF = nIF;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public String getNombreApellidos() {
		return nombreApellidos;
	}
	public void setNombreApellidos(String nombreApellidos) {
		this.nombreApellidos = nombreApellidos;
	}
	public TicketProductos getListadoProductos() {
		return ticketProductos;
	}
	public void setListadoProductos(TicketProductos ticketProductos) {
		this.ticketProductos = ticketProductos;
	}
	public int getIVA() {
		return IVA;
	}
	public void setIVA(int iVA) {
		IVA = iVA;
	}
	public double getTotalSinIVA() {
		return totalSinIVA;
	}
	public void setTotalSinIVA(double totalSinIVA) {
		this.totalSinIVA = totalSinIVA;
	}
	public double getTotalConIVA() {
		return totalConIVA;
	}
	public void setTotalConIVA(double totalConIVA) {
		this.totalConIVA = totalConIVA;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

}
