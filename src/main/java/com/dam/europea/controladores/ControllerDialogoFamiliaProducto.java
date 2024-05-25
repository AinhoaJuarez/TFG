package com.dam.europea.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;

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

public class ControllerDialogoFamiliaProducto implements Initializable {

	private SessionFactory sf;
	private String codigoFamilia;
	@FXML
	private TextField txtCodigoFamilia;
	@FXML
	private TextField txtNombreFamiliaProductos;
	@FXML
	private ComboBox<List<Producto>> comboBoxProductos;
	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;

	public ControllerDialogoFamiliaProducto(SessionFactory sf, String codigoFamilia) {
		this.sf = sf;
		this.codigoFamilia = codigoFamilia;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();
		if (codigoFamilia != null) {
			session.beginTransaction();
			FamiliaProducto fp = session.find(FamiliaProducto.class, codigoFamilia);
			if (fp != null) {
				txtCodigoFamilia.setText(fp.getCodFamilia());
				txtNombreFamiliaProductos.setText(fp.getFamiliaProducto());
				comboBoxProductos.setValue(fp.getProductosAsociados());
				;
			}
		}

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {
				if (codigoFamilia == null) {
					crearFamiliaProducto();
				} else {
					modFamiliaProducto();
				}
				closeWindow();
			} else {
				showWarning();
			}
		});

		btnCancelar.setOnAction(event -> closeWindow());
	}

	private boolean areFieldsValid() {
		return !txtCodigoFamilia.getText().isEmpty() && !txtNombreFamiliaProductos.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}

	public void crearFamiliaProducto() {
		ObservableList<FamiliaProducto> familiasProducto = FXCollections.observableArrayList(); // Esto es lo que
																								// contiene los objetos
																								// que se muestran en el
																								// ComboBox

		familiasProducto.add(new FamiliaProducto("1213121", "Libros")); // Objetos de Ejemplo de las posibles familia de
																		// Productos
		familiasProducto.add(new FamiliaProducto("4124211", "Material Escolar"));
		List<Producto> familiaProductoSeleccionada = comboBoxProductos.getValue();
		// SEGURAMENTE ESTAS LINEAS DE CÓDIGOS SE PUEDEN DECLARAR ARRIBA PARA ACCEDER
		// GLOBALMENTE Y NO CREARLO EN CADA MÉTODO, INVESTIGAR

		FamiliaProducto fp = new FamiliaProducto();
		fp.setCodFamilia(txtCodigoFamilia.getText());
		fp.setFamiliaProducto(txtNombreFamiliaProductos.getText());
		fp.setProductosAsociados(familiaProductoSeleccionada);
		session.beginTransaction();
		session.persist(fp);
		session.getTransaction().commit();
	}

	public void modFamiliaProducto() {
		ObservableList<FamiliaProducto> familiasProducto = FXCollections.observableArrayList(); // Esto es lo que
																								// contiene los objetos
																								// que se muestran en el
																								// ComboBox

		familiasProducto.add(new FamiliaProducto("1213121", "Libros")); // Objetos de Ejemplo de las posibles familia de
																		// Productos
		familiasProducto.add(new FamiliaProducto("4124211", "Material Escolar"));
		List<Producto> familiaProductoSeleccionada = comboBoxProductos.getValue(); // Aquí sin embargo lo recojo como
																					// List Producto para poder luego
																					// hacer setProductosAsociados
		// SEGURAMENTE ESTAS LINEAS DE CÓDIGOS SE PUEDEN DECLARAR ARRIBA PARA ACCEDER
		// GLOBALMENTE Y NO CREARLO EN CADA MÉTODO, INVESTIGAR

		FamiliaProducto fp = new FamiliaProducto();
		fp.setCodFamilia(txtCodigoFamilia.getText());
		fp.setFamiliaProducto(txtNombreFamiliaProductos.getText());
		fp.setProductosAsociados(familiaProductoSeleccionada);
		session.beginTransaction();
		session.merge(fp);
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
