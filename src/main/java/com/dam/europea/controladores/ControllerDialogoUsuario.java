package com.dam.europea.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;
import com.dam.europea.entidades.Usuario;

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

public class ControllerDialogoUsuario implements Initializable {
	

	private SessionFactory sf;
	private String IDUsuario;
	@FXML
	private TextField txtIDUsuario;
	@FXML
	private TextField txtNombreUsuario;
	@FXML
	private TextField txtContrasena;
	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;

	public ControllerDialogoUsuario(SessionFactory sf, String iDUsuario) {
		this.sf = sf;
		IDUsuario = iDUsuario;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();
		if (IDUsuario != null) {
			session.beginTransaction();
			Usuario u = session.find(Usuario.class, IDUsuario);
			if (u != null) {
				txtIDUsuario.setText(String.valueOf(u.getIdUsr()));
				txtNombreUsuario.setText(u.getUserName());
				txtContrasena.setText(u.getPass());
				
			}
		}

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {
				if (IDUsuario == null) {
					crearUsuario();
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
		return !txtIDUsuario.getText().isEmpty() && !txtNombreUsuario.getText().isEmpty() && !txtContrasena.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}

	public void crearUsuario() {

		Usuario u = new Usuario();
		u.setIdUsr(Integer.valueOf(txtIDUsuario.getText()));
		u.setUserName(txtNombreUsuario.getText());
		u.setPass(txtContrasena.getText());
		session.beginTransaction();
		session.persist(u);
		session.getTransaction().commit();
	}

	public void modFamiliaProducto() {
		ObservableList<Proveedor> proveedor = FXCollections.observableArrayList(); // Esto es lo que
		// contiene los objetos
		// que se muestran en el
		// ComboBox

		proveedor.add(new Proveedor("12312312", "CuadernosFinos S.L")); // Objetos de Ejemplo de las posibles familia de
// Productos
		proveedor.add(new Proveedor("63122312", "CuadernosGordos S.L"));
		List<Producto> proveedorSelecionado = comboBoxProductos.getValue(); // Aquí sin embargo lo recojo como
// List Producto para poder luego
// hacer setProductosAsociados
// SEGURAMENTE ESTAS LINEAS DE CÓDIGOS SE PUEDEN DECLARAR ARRIBA PARA ACCEDER
// GLOBALMENTE Y NO CREARLO EN CADA MÉTODO, INVESTIGAR

		Proveedor pv = new Proveedor();
		pv.setCodigo(txtCodigoProveedor.getText());
		pv.setNombre(txtNombreProveedor.getText());
		pv.setProductosAsociados(proveedorSelecionado);
		session.beginTransaction();
		session.merge(pv);
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
