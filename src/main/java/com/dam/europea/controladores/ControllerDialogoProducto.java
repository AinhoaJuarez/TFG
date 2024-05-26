package com.dam.europea.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
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

public class ControllerDialogoProducto implements Initializable {

	private SessionFactory sf;
	private String codigoBarras;
	@FXML
	private TextField txtCodigoBarras;
	@FXML
	private ComboBox<List<FamiliaProducto>> comboBoxFamiliaProducto;
	@FXML
	private TextField txtDescripcion;
	@FXML
	private TextField txtPrecioCompra;
	@FXML
	private TextField txtPrecioVenta;
	@FXML
	private TextField txtMargen;
	@FXML
	private TextField txtCantidad;
	@FXML
	private TextField txtStock;
	@FXML
	private ComboBox<List<Proveedor>> comboBoxProveedor;
	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;

	public ControllerDialogoProducto(SessionFactory sf, String codigoBarras) {
		this.sf = sf;
		this.codigoBarras = codigoBarras;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();
		if (codigoBarras != null) {
			session.beginTransaction();
			Producto p = session.find(Producto.class, codigoBarras);
			if (p != null) {
				txtCodigoBarras.setText(p.getCodigoBarras());
				comboBoxFamiliaProducto.setValue(p.getFamiliaArticulo());
				txtDescripcion.setText(p.getDescripcion());
				txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
				txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
				txtMargen.setText(String.valueOf(p.getMargen()));
				txtCantidad.setText(String.valueOf(p.getCantidad()));
				txtStock.setText(String.valueOf(p.getStock()));
				comboBoxProveedor.setValue(p.getProveedorProducto());
				;
			}
		}

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {
				if (codigoBarras == null) {
					crearProducto();
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
		return !txtCodigoBarras.getText().isEmpty() && !txtDescripcion.getText().isEmpty()
				&& !txtPrecioCompra.getText().isEmpty() && !txtPrecioVenta.getText().isEmpty()
				&& !txtMargen.getText().isEmpty() && !txtCantidad.getText().isEmpty() && !txtStock.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}

	public void crearProducto() {
		ObservableList<FamiliaProducto> familiasProducto = FXCollections.observableArrayList(); // Esto es lo que
																								// contiene los objetos
																								// que se muestran en el
																								// ComboBox

		familiasProducto.add(new FamiliaProducto("1213121", "Libros")); // Objetos de Ejemplo de las posibles familia de
																		// Productos
		familiasProducto.add(new FamiliaProducto("4124211", "Material Escolar"));
		FamiliaProducto familiaProductoSeleccionada = comboBoxFamiliaProducto.getValue();
		
		// SEGURAMENTE ESTAS LINEAS DE CÓDIGOS SE PUEDEN DECLARAR ARRIBA PARA ACCEDER
		// GLOBALMENTE Y NO CREARLO EN CADA MÉTODO, INVESTIGAR

		Producto p = new Producto();
		p.setCodigoBarras(txtCodigoBarras.getText());
		p.setFamiliaProducto(txtNombreFamiliaProductos.getText());
		p.setFamiliaArticulo(familiaProductoSeleccionada);
		session.beginTransaction();
		session.persist(p);
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
