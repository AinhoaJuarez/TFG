package com.dam.europea.entidades;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "familiaProducto")
public class FamiliaProducto {

	@Id
	private String codFamilia;
	@Column(name = "Familia Producto", unique = true, nullable = false)
	private String familiaProducto;
	@OneToMany(mappedBy="familiaProducto", cascade= CascadeType.PERSIST)
	private ArrayList<Producto> productosAsociados;

	public FamiliaProducto(String codFamilia, String familiaProducto) {
		super();
		this.codFamilia = codFamilia;
		this.familiaProducto = familiaProducto;
	}

	

	public FamiliaProducto() {
		productosAsociados = new ArrayList<Producto>();
	}
	public String getFamiliaProducto() {
		return familiaProducto;
	}

	public void setFamiliaProducto(String familiaProducto) {
		this.familiaProducto = familiaProducto;
	}

	

}
