package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.DatosEmpresa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControladorDialogoDatosEmpresa implements Initializable {
	private SessionFactory sf;
	private String nif;
	@FXML
	private TextField txtNombreEmpresa;
	@FXML
	private TextField txtNombreDueno;
	@FXML
	private TextField txtDireccion;
	@FXML
	private TextField txtLocalidad;
	@FXML
	private TextField txtCodPost;
	@FXML
	private TextField txtNIF;

	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;

	public ControladorDialogoDatosEmpresa(SessionFactory sf) {
		this.sf = sf;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {

					modDatosEmpresa();

				closeWindow();
			} else {
				showWarning();
			}
		});

		btnCancelar.setOnAction(event -> closeWindow());
	}

	private boolean areFieldsValid() {
		return !txtNombreEmpresa.getText().isEmpty() && !txtNombreDueno.getText().isEmpty() && !txtDireccion.getText().isEmpty()
				&& !txtLocalidad.getText().isEmpty() && !txtCodPost.getText().isEmpty()  && !txtNIF.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}


	public void modDatosEmpresa() {
		DatosEmpresa de = new DatosEmpresa();
		de.setId(1);
		DatosEmpresa sel = session.find(DatosEmpresa.class, de);
		sel.setNif(txtNIF.getText());
		sel.setNombreEmpresa(txtNombreEmpresa.getText());
		sel.setNombreDueno(txtNombreDueno.getText());
		sel.setDireccion(txtDireccion.getText());
		sel.setLocalidad(txtLocalidad.getText());
		sel.setCodigoPostal(txtCodPost.getText());
		session.beginTransaction();
		session.merge(de);
		session.getTransaction().commit();
	}

	private void closeWindow() {
		if (session != null && session.isOpen()) {
			session.close();
		}
		Stage stage = (Stage) btnAceptar.getScene().getWindow();
		stage.close();
	}

}
