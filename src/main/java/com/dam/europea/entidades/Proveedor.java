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
	@Id
	private String codigo;
	@Column
	private String nombre;
	@OneToMany(mappedBy="proveedorProducto", cascade= CascadeType.PERSIST)
	private List<Producto> productosAsociados= new ArrayList<>();
	
	public List<Producto> getProductosAsociados() {
		return productosAsociados;
	}

	public void setProductosAsociados(List<Producto> productosAsociados) {
		this.productosAsociados = productosAsociados;
	}

	public Proveedor() {
		
	}
	
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
}
