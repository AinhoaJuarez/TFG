package com.dam.europea.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TicketProductos")
public class TicketProductos {
	@Id
	private Long id;

	
	@Column
	private int cantidad;
	private double descuento;
	private double precioDescuento;
	private double precioTotal;
	public double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@ManyToOne
	@JoinColumn(name = "numTicket")
	private Ticket numTicket;

	@ManyToOne
	@JoinColumn(name = "numeroFactura")
	private Factura numerofactura;

	@ManyToOne
	@JoinColumn(name = "codigoBarras")
	private Producto producto;
	@Override
	public String toString() {
		return "TicketProductos [id=" + id + ", cantidad=" + cantidad + ", descuento=" + descuento
				+ ", precioDescuento=" + precioDescuento + ", precioTotal=" + precioTotal + ", numTicket=" + numTicket
				+ ", numerofactura=" + numerofactura + ", producto=" + producto + ", descripcion=" + descripcion + "]";
	}

	private String descripcion;
	

	public TicketProductos() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return numTicket;
	}

	public void setTicket(Ticket ticket) {
		this.numTicket = ticket;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getdescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getPrecioDescuento() {
		return precioDescuento;
	}

	public void setPrecioDescuento(double precioDescuento) {
		this.precioDescuento = precioDescuento;
	}

}
