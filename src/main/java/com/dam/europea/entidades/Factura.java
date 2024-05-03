package com.dam.europea.entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

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
	@JoinTable(name = "rel_prod_fact", joinColumns = @JoinColumn(name = "Numero de factura", nullable = false),
	        inverseJoinColumns = @JoinColumn(name="Codigo de producto", nullable = false)
	    )
	@ManyToMany	
	private ArrayList<Producto> productos;
	@Convert(converter = MapToJsonConverter.class)
    @Column(name = "listado_productos", columnDefinition = "CLOB")
	private Map<Producto, Integer> listadoProductos;
	private int IVA;
	private double totalSinIVA;
	private double totalConIVA;
	public Factura(int numeroFactura, Cliente cliente, String fechaExpedicion, String fechaOperacion, String nIF,
			String direccion, int telefono, String nombreApellidos, Map<Producto, Integer> listadoProductos, int iVA,
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
		this.listadoProductos = listadoProductos;
		IVA = iVA;
		this.totalSinIVA = totalSinIVA;
		this.totalConIVA = totalConIVA;
		this.descuento = descuento;
	}
	public Factura() {
		this.listadoProductos = new HashMap<Producto, Integer>();
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
	public Map<Producto, Integer> getListadoProductos() {
		return listadoProductos;
	}
	public void setListadoProductos(Map<Producto, Integer> listadoProductos) {
		this.listadoProductos = listadoProductos;
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
	public void setProductos() {
		productos.addAll(listadoProductos.keySet());
	}

}
