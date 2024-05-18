package com.dam.europea.entidades;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {
    @Id
    private String codigoBarras;
    @Column
    private String descripcion;
    @ManyToOne
	@JoinColumn(name = "familiaArt_fk", referencedColumnName = "Familia Producto")
    private FamiliaProducto familiaProducto;
    private double precioCompra;
    private double precioVenta;
    private double margen;
    private int cantidad;
    private int stock;
    @ManyToOne
	@JoinColumn(name = "proveedor_fk", referencedColumnName = "Proveedor Producto")
    private Proveedor proveedor;
    
    
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public FamiliaProducto getFamiliaArticulo() {
		return familiaProducto;
	}
	public void setFamiliaArticulo(FamiliaProducto familiaArticulo) {
		this.familiaProducto = familiaArticulo;
	}
	public double getPrecioCompra() {
		return precioCompra;
	}
	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}
	public double getPrecioVenta() {
		return precioVenta;
	}
	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}
	public double getMargen() {
		return margen;
	}
	public void setMargen(double margen) {
		this.margen = margen;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

   
}