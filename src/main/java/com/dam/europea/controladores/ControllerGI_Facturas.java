package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Factura;

import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerGI_Facturas implements Initializable {
	// Botones fotos
	@FXML
	private Button btnProductos;
	@FXML
	private Button btnFamilia;
	@FXML
	private Button btnClientes;
	@FXML
	private Button btnProveedores;
	@FXML
	private Button btnUsers;
	@FXML
	private Button btnTickets;
	@FXML
	private Button btnFacturas;
	@FXML
	private Button botonMenu;
	@FXML
	private Button botonSalir;
	@FXML
	private Button btnNew;
	@FXML
	private Button btnDel;
	@FXML
	private TextField txtCod;
	@FXML
	private TextField txtTotal;
	@FXML
	private ComboBox<Cliente> comboCliente;

	// Botones sin fotos
	@FXML
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;

	private SessionFactory sf;

	@FXML
	private TableView<Factura> tableViewFacturas;
	@FXML
	private TableColumn<Factura, Integer> codFacturaColumn;

	@FXML
	private TableColumn<Factura, String> fechaFacturaColumn;

	@FXML
	private TableColumn<Factura, String> clienteAsocColumn;

	@FXML
	private TableColumn<Factura, Double> totalColumn;

	public ControllerGI_Facturas(SessionFactory sf) {
		this.sf = sf;
	}

	//En el código, definimos un controlador JavaFX que gestiona la visualización y navegación de facturas en nuestra aplicación de inventario,
	//interactuando con una base de datos a través de Hibernate.
	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarImagenes();
		cargarClientesComboBox();
		botonSalir.setOnAction(arg0 -> {
			try {
				switchToInicioSesion(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botonMenu.setOnAction(arg0 -> {
			try {
				switchToMenu(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnProductos.setOnAction(arg0 -> {
			try {
				switchToProds(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnFamilia.setOnAction(arg0 -> {
			try {
				switchToFamilia(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnProveedores.setOnAction(arg0 -> {
			try {
				switchToProv(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnUsers.setOnAction(arg0 -> {
			try {
				switchToUsers(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnTickets.setOnAction(arg0 -> {
			try {
				switchToTickets(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnClientes.setOnAction(arg0 -> {
			try {
				switchToClientes(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		cargarTabla();
		btnNew.setOnAction(arg0 -> {
			try {
				switchToFacturas(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txtCod.textProperty().addListener((observable, oldValue, newValue) -> searchFacturas());
		txtTotal.textProperty().addListener((observable, oldValue, newValue) -> searchFacturas());
		comboCliente.valueProperty().addListener((observable, oldValue, newValue) -> searchFacturas());
		tableViewFacturas.setRowFactory(tv -> {
		    TableRow<Factura> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (!row.isEmpty())) {
		            Factura rowData = row.getItem();
		            switchToPantallaFacturas(rowData);
		        }
		    });
		    return row;
		});
	}

	private void switchToPantallaFacturas(Factura selectedFactura) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Facturas.fxml"));
	        ControllerFactura pantallaTicketsController = new ControllerFactura(sf, selectedFactura);
	        loader.setController(pantallaTicketsController);
	        Parent root = loader.load();
	        Scene scene = new Scene(root);
	        Stage stage = (Stage) tableViewFacturas.getScene().getWindow();
	        stage.setScene(scene);
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void cargarClientesComboBox() {
		comboCliente.getItems().clear();
		comboCliente.getItems().add(null);

		Session session = sf.openSession();
		TypedQuery<Cliente> query = session.createQuery("SELECT c FROM Cliente c", Cliente.class);
		List<Cliente> clientes = query.getResultList();
		comboCliente.getItems().addAll(clientes);
		session.close();
	}

	private void searchFacturas() {
		String cod = txtCod.getText().trim();
		String total = txtTotal.getText().trim();
		Cliente cliente = comboCliente.getValue();

		StringBuilder hql = new StringBuilder("SELECT f FROM Factura f WHERE 1=1");

		if (!cod.isEmpty()) {
			hql.append(" AND STR(f.numeroFactura) LIKE :cod");
		}
		if (!total.isEmpty()) {
			hql.append(" AND STR(f.totalConIVA) LIKE :total");
		}
		if (cliente != null) {
			hql.append(" AND f.cliente = :cliente");
		}

		Session session = sf.openSession();
		TypedQuery<Factura> query = session.createQuery(hql.toString(), Factura.class);

		if (!cod.isEmpty()) {
			query.setParameter("cod", "%" + cod + "%");
		}
		if (!total.isEmpty()) {
			query.setParameter("total", "%" + total + "%");
		}
		if (cliente != null) {
			query.setParameter("cliente", cliente);
		}

		List<Factura> results = query.getResultList();
		tableViewFacturas.getItems().clear();
		tableViewFacturas.getItems().addAll(results);
		session.close();
	}

	public void switchToFacturas(ActionEvent arg0) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Facturas.fxml"));
		ControllerFactura ct = new ControllerFactura(sf, null);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);

		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

	}

	public void cargarTabla() {
		tableViewFacturas.getItems().clear();
		codFacturaColumn.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
		fechaFacturaColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		clienteAsocColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
		clienteAsocColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getCliente() != null) {
				return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCliente().getNombre());
			} else {
				return new javafx.beans.property.SimpleStringProperty("");
			}
		});
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalConIVA"));

		Session session = sf.openSession();
		TypedQuery<Factura> query = session.createQuery("SELECT e FROM Factura e", Factura.class);
		ArrayList<Factura> entityData = (ArrayList<Factura>) query.getResultList();
		if (entityData != null) {
			tableViewFacturas.getItems().addAll(entityData);
		}
	}

	public void cargarImagenes() {
		InputStream archivoProd = getClass().getResourceAsStream("/inventario.png");
		Image imagenProd = new Image(archivoProd, 75, 75, true, true);
		btnProductos.setGraphic(new ImageView(imagenProd));
		InputStream archivoFam = getClass().getResourceAsStream("/famProd.png");
		Image imagenFam = new Image(archivoFam, 75, 75, true, true);
		btnFamilia.setGraphic(new ImageView(imagenFam));
		InputStream archivoCli = getClass().getResourceAsStream("/cliente.png");
		Image imagenCli = new Image(archivoCli, 75, 75, true, true);
		btnClientes.setGraphic(new ImageView(imagenCli));
		InputStream archivoProv = getClass().getResourceAsStream("/prov.png");
		Image imagenProv = new Image(archivoProv, 75, 75, true, true);
		btnProveedores.setGraphic(new ImageView(imagenProv));
		InputStream archivoUsr = getClass().getResourceAsStream("/users.png");
		Image imagenUsr = new Image(archivoUsr, 75, 75, true, true);
		btnUsers.setGraphic(new ImageView(imagenUsr));
		InputStream archivoTick = getClass().getResourceAsStream("/recibo.png");
		Image imagenTick = new Image(archivoTick, 75, 75, true, true);
		btnTickets.setGraphic(new ImageView(imagenTick));
		InputStream archivoFac = getClass().getResourceAsStream("/factura.png");
		Image imagenFac = new Image(archivoFac, 75, 75, true, true);
		btnFacturas.setGraphic(new ImageView(imagenFac));
		InputStream archivoNew = getClass().getResourceAsStream("/plus.png");
		Image imagenNew = new Image(archivoNew, 35, 35, true, true);
		btnNew.setGraphic(new ImageView(imagenNew));
		InputStream archivoMin = getClass().getResourceAsStream("/minus.png");
		Image imagenMin = new Image(archivoMin, 35, 35, true, true);
		btnDel.setGraphic(new ImageView(imagenMin));
		InputStream archivoMenu = getClass().getResourceAsStream("/menu.png");
		Image imagenMenu = new Image(archivoMenu, 25, 25, true, true);
		botonMenu.setGraphic(new ImageView(imagenMenu));
		InputStream archivoCerrar = getClass().getResourceAsStream("/cerrar-sesion.png");
		Image imagenCerrar = new Image(archivoCerrar, 25, 25, true, true);
		botonSalir.setGraphic(new ImageView(imagenCerrar));
	}

	public void switchToMenu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/erae.fxml"));
		Controller ct = new Controller(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - scene.getWidth()) * 0.17;
		double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight() - scene.getHeight()) * 0.12;

		stage.setX(centerX);
		stage.setY(centerY);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToInicioSesion(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
		ControladorInicioSesion ct = new ControladorInicioSesion(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - scene.getWidth()) * 0.17;
		double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight() - scene.getHeight()) * 0.12;

		stage.setX(centerX);
		stage.setY(centerY);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToClientes(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Clientes.fxml"));
		ControllerGI_Clientes ct = new ControllerGI_Clientes(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}

	public void switchToTickets(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Tickets.fxml"));
		ControllerGI_Tickets ct = new ControllerGI_Tickets(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}

	public void switchToFamilia(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Fam.fxml"));
		ControllerGI_Fam ct = new ControllerGI_Fam(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}

	public void switchToProds(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Prods.fxml"));
		ControllerGI_Prods ct = new ControllerGI_Prods(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}

	public void switchToProv(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Prov.fxml"));
		ControllerGI_Prov ct = new ControllerGI_Prov(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}

	public void switchToUsers(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Users.fxml"));
		ControllerGI_Users ct = new ControllerGI_Users(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.initStyle(StageStyle.UNDECORATED);

		stage.show();
	}
}
