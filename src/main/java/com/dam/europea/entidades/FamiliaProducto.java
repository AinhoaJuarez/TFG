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
@Table(name = "familiaProducto")
public class FamiliaProducto {

	@Id
	private String codFamilia;
	@Column(name = "Familia Producto", unique = true, nullable = false)
	private String familiaProducto;
	@OneToMany(mappedBy="familiaProducto", cascade = {CascadeType.MERGE})
	private List<Producto> productosAsociados= new ArrayList<>();
	private int IVA;

	@Override
	public String toString() {
		return familiaProducto ;
	}



	public int getIVA() {
		return IVA;
	}



	public void setIVA(int iVA) {
		IVA = iVA;
	}

    // Constructor con parámetros para inicializar los campos codFamilia y familiaProducto
    public FamiliaProducto(String codFamilia, String familiaProducto) {
        super();
        this.codFamilia = codFamilia;
        this.familiaProducto = familiaProducto;
    }

    // Constructor vacío necesario para JPA
    public FamiliaProducto() {
        productosAsociados = new ArrayList<Producto>();
    }

    // Métodos getters y setters para cada uno de los campos de la clase FamiliaProducto
    public String getFamiliaProducto() {
        return familiaProducto;
    }

    public void setFamiliaProducto(String familiaProducto) {
        this.familiaProducto = familiaProducto;
    }

    public String getCodFamilia() {
        return codFamilia;
    }

    public void setCodFamilia(String codFamilia) {
        this.codFamilia = codFamilia;
    }

    public List<Producto> getProductosAsociados() {
        return productosAsociados;
    }

    public void setProductosAsociados(List<Producto> productosAsociados) {
        this.productosAsociados = productosAsociados;
    }
}
