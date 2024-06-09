package com.dam.europea.entidades;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DatosEmpresa {

	@Id
	private int id;
	@Column
	private String nif;
	private String nombreEmpresa;
	private String nombreDueno;
	private String direccion;
	private String localidad;
	private String codigoPostal;
	
	
	public DatosEmpresa(String nif, String nombreEmpresa, String nombreDueno, String direccion, String localidad,
			String codigoPostal) {
		super();
		this.nif = nif;
		this.nombreEmpresa = nombreEmpresa;
		this.nombreDueno = nombreDueno;
		this.direccion = direccion;
		this.localidad = localidad;
		this.codigoPostal = codigoPostal;
	}


	public String getNif() {
		return nif;
	}


	public void setNif(String nif) {
		this.nif = nif;
	}


	public String getNombreEmpresa() {
		return nombreEmpresa;
	}


	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}


	public String getNombreDueno() {
		return nombreDueno;
	}


	public void setNombreDueno(String nombreDueno) {
		this.nombreDueno = nombreDueno;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public String getLocalidad() {
		return localidad;
	}


	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}


	public String getCodigoPostal() {
		return codigoPostal;
	}


	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}


	public DatosEmpresa() {
		super();
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
}

