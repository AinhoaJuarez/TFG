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
import jakarta.persistence.Table;

@Entity
@Table(name = "Tickets")
public class Ticket {
	@Id
	private int numTicket;
	@Column
	private String fecha;
	private double importeTotal;
	@ManyToOne
	@JoinColumn(name = "ClienteAsociado_fk", referencedColumnName= "DNI")
	private Cliente cliente;
	@JoinTable(name = "rel_prod_tick", joinColumns = @JoinColumn(name = "Numero de ticket", nullable = false),
	        inverseJoinColumns = @JoinColumn(name="Codigo de producto", nullable = false)
	    )
	@ManyToMany
	private ArrayList<Producto> productos;
	@Convert(converter = MapToJsonConverter.class)
    @Column(name = "listado_productos", columnDefinition = "CLOB")
	private Map<Producto, Integer> listadoProductos;
	public Ticket(int numTicket, String fecha, double importeTotal, Cliente cliente, Map<Producto, Integer> listadoProductos) {
		super();
		this.numTicket = numTicket;
		this.fecha = fecha;
		this.importeTotal = importeTotal;
		this.cliente = cliente;
		this.listadoProductos = listadoProductos;
	}
	public Ticket() {
		this.listadoProductos = new HashMap <Producto, Integer>();
	}
	public int getNumTicket() {
		return numTicket;
	}
	public void setNumTicket(int numTicket) {
		this.numTicket = numTicket;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Map<Producto, Integer> getListadoProductos() {
		return listadoProductos;
	}
	public void setListadoProductos(Map<Producto, Integer> listadoProductos) {
		this.listadoProductos = listadoProductos;
	}
	public void setProductos() {
		productos.addAll(listadoProductos.keySet());
	}
	

}
