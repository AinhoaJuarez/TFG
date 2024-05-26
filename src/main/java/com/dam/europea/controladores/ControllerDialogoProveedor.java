package com.dam.europea.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerDialogoProveedor implements Initializable {

	private SessionFactory sf;
	
	@FXML
	private TextField txtCodigoProveedor;
	@FXML
	private TextField txtNombreProveedor;
	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;
	//Plantillas rellenables de clases
	private Proveedor pv;
	private String codigoProveedor;
	private ControllerGI_Prov ct2;
	public ControllerDialogoProveedor(SessionFactory sf, String codigoProveedor, ControllerGI_Prov ct2) {
		this.sf = sf;
		this.codigoProveedor = codigoProveedor;
		this.ct2 = ct2;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();
		session.beginTransaction();
		if (codigoProveedor != null) {
			pv = session.find(Proveedor.class, codigoProveedor);
			if (pv != null) {
				txtCodigoProveedor.setText(pv.getCodigo());
				txtNombreProveedor.setText(pv.getNombre());
				
			}
		}

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {
				if (codigoProveedor == null) {
					crearProveedor();
				} else {
					modFamiliaProducto(pv);
				}
				closeWindow();
			} else {
				showWarning();
			}
		});

		btnCancelar.setOnAction(event -> closeWindow());
	}

	private boolean areFieldsValid() {
		return !txtCodigoProveedor.getText().isEmpty() && !txtNombreProveedor.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}

	public void crearProveedor() {

		Proveedor pv = new Proveedor();
		pv.setCodigo(txtCodigoProveedor.getText());
		pv.setNombre(txtNombreProveedor.getText());
		session.persist(pv);
		session.getTransaction().commit();
	}

	public void modFamiliaProducto(Proveedor pv) {
		pv = new Proveedor();
		pv.setCodigo(txtCodigoProveedor.getText());
		pv.setNombre(txtNombreProveedor.getText());
		session.merge(pv);
		session.getTransaction().commit();
	}

	private void closeWindow() {
		ct2.cargarTabla();
		if (session != null && session.isOpen()) {
			session.close();
		}
		Stage stage = (Stage) btnAceptar.getScene().getWindow();
		stage.close();
		
	}

}
