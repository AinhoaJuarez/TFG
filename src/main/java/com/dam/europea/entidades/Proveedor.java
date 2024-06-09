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
@Table(name = "Proveedores")
public class Proveedor {

	@Override
	public String toString() {
		return nombre;
	}

//Definimos el ID de la entidad Proveedor
	@Id
	private String codigo;
	@Column
	private String nombre;
	@OneToMany(mappedBy = "proveedorProducto", cascade = {CascadeType.MERGE})
	private List<Producto> productosAsociados = new ArrayList<>();

	public Proveedor() {

	}

	public Proveedor(String codigo, String nombre) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
	}
	// Métodos getters y setters para la lista de productos asociados
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public List<Producto> getProductosAsociados() {
		return productosAsociados;
	}

	public void setProductosAsociados(List<Producto> productosAsociados) {
		this.productosAsociados = productosAsociados;
	}

}
