package com.dam.europea.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;

import jakarta.persistence.TypedQuery;
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
	private ComboBox<String> comboBoxFamiliaProducto;
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
	private ComboBox<String> comboBoxProveedor;
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
		TypedQuery<String> query = session.createQuery("SELECT fp.codFamilia FROM FamiliaProducto fp", String.class);
		List<String> codFamilias = query.getResultList();
		comboBoxFamiliaProducto.getItems().addAll(codFamilias);

		TypedQuery<String> query2 = session.createQuery("SELECT p.codigo FROM Proveedor p", String.class);
		List<String> codProveedor = query2.getResultList();
		comboBoxProveedor.getItems().addAll(codProveedor);

		if (codigoBarras != null) {
			session.beginTransaction();
			Producto p = session.find(Producto.class, codigoBarras);
			if (p != null) {
				txtCodigoBarras.setText(p.getCodigoBarras());
				comboBoxFamiliaProducto.setValue(p.getFamiliaArticulo().getCodFamilia());
				txtDescripcion.setText(p.getDescripcion());
				txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
				txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
				txtMargen.setText(String.valueOf(p.getMargen()));
				txtCantidad.setText(String.valueOf(p.getCantidad()));
				txtStock.setText(String.valueOf(p.getStock()));
				comboBoxProveedor.setValue(p.getProveedorProducto().getCodigo());
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

		Producto p = new Producto();
		p.setCodigoBarras(txtCodigoBarras.getText());
		String codFamilia = comboBoxFamiliaProducto.getValue();
		FamiliaProducto fp = session.find(FamiliaProducto.class, codFamilia);
		p.setFamiliaArticulo(fp);
		p.setDescripcion(txtDescripcion.getText());
		p.setPrecioCompra(Integer.valueOf(txtPrecioCompra.getText())); // MODIFICAR FXML PARA QUE EL TEXTFIELD SOLO
																		// ADMITA NUMEROS
		p.setPrecioVenta(Integer.valueOf(txtPrecioVenta.getText()));
		p.setMargen(Integer.valueOf(txtMargen.getText()));
		p.setCantidad(Integer.valueOf(txtCantidad.getText()));
		p.setStock(Integer.valueOf(txtStock.getText()));
		String codProveedor = comboBoxProveedor.getValue();
		Proveedor pv = session.find(Proveedor.class, codProveedor);
		p.setProveedorProducto(pv);

		session.beginTransaction();
		session.persist(p);
		session.getTransaction().commit();
	}

	public void modFamiliaProducto() {
		Producto p = new Producto();
		p.setCodigoBarras(txtCodigoBarras.getText());
		String codFamilia = comboBoxFamiliaProducto.getValue();
		FamiliaProducto fp = session.find(FamiliaProducto.class, codFamilia);
		p.setFamiliaArticulo(fp);
		p.setDescripcion(txtDescripcion.getText());
		p.setPrecioCompra(Integer.valueOf(txtPrecioCompra.getText())); // MODIFICAR FXML PARA QUE EL TEXTFIELD SOLO
																		// ADMITA NUMEROS
		p.setPrecioVenta(Integer.valueOf(txtPrecioVenta.getText()));
		p.setMargen(Integer.valueOf(txtMargen.getText()));
		p.setCantidad(Integer.valueOf(txtCantidad.getText()));
		p.setStock(Integer.valueOf(txtStock.getText()));
		String codProveedor = comboBoxProveedor.getValue();
		Proveedor pv = session.find(Proveedor.class, codProveedor);
		p.setProveedorProducto(pv);

		session.beginTransaction();
		session.merge(p);
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
