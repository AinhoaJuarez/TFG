package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.Cliente;

import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerGI_Clientes implements Initializable {
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

	// Botones sin fotos
	@FXML
	private Button btnBuscar;
	@FXML
	private Button btnBuscarNombre;
	@FXML
	private TextField txtCodPostal;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtLocalidad;
	private SessionFactory sf;

	@FXML
	private TableView<Cliente> tableView;
	@FXML
	private TableColumn<Cliente, String> columnDNI;

	@FXML
	private TableColumn<Cliente, String> columnNombre;

	@FXML
	private TableColumn<Cliente, String> columnDireccion;

	@FXML
	private TableColumn<Cliente, String> columnLocalidad;

	@FXML
	private TableColumn<Cliente, Integer> columnCodPostal;
	private Session session;

	public ControllerGI_Clientes(SessionFactory sf) {
		this.sf = sf;
	}

	// En el código, definimos un controlador JavaFX que gestiona la visualización y
	// navegación de clientes en nuestra aplicación de inventario,
	// interactuando con una base de datos a través de Hibernate.
	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarTabla();
		cargarImagenes();

		tableView.setRowFactory(tv -> {
			TableRow<Cliente> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
					Cliente rowData = row.getItem();
					try {
						abrirDialogoCrearCliente(event, rowData.getDni());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			return row;
		});
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
		btnFacturas.setOnAction(arg0 -> {
			try {
				switchToFacturas(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnNew.setOnAction(arg0 -> {
			try {
				abrirDialogoCrearCliente(arg0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnDel.setOnAction(arg0 -> {
			try {
				deleteSelectedClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		addSearchListeners();

	}

	private void deleteSelectedClient() {
		session = sf.openSession();
		session.beginTransaction();
		Cliente selectedCliente = tableView.getSelectionModel().getSelectedItem();
		if (selectedCliente != null) {
			Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
					"¿Estás seguro de que deseas eliminar este cliente?", ButtonType.YES, ButtonType.NO);
			confirmationAlert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					session.remove(selectedCliente);
					session.getTransaction().commit();
					session.close();
					tableView.getItems().remove(selectedCliente);
					showAlert(Alert.AlertType.INFORMATION, "Cliente Eliminado", "El cliente ha sido eliminado.");
				}
			});
		} else {
			showAlert(Alert.AlertType.WARNING, "Ninguna selección", "Por favor seleccione un cliente para eliminar.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void addSearchListeners() {
		txtCodPostal.textProperty().addListener((observable, oldValue, newValue) -> buscarClientes());
		txtNombre.textProperty().addListener((observable, oldValue, newValue) -> buscarClientes());
		txtLocalidad.textProperty().addListener((observable, oldValue, newValue) -> buscarClientes());
	}

	public void cargarTabla() {
		session=sf.openSession();
		tableView.getItems().clear();
		columnDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
		columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		columnDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
		columnLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
		columnCodPostal.setCellValueFactory(new PropertyValueFactory<>("codPos"));

		TypedQuery<Cliente> query = session.createQuery("SELECT e FROM Cliente e", Cliente.class);
		ArrayList<Cliente> entityData = (ArrayList<Cliente>) query.getResultList();
		if (entityData != null) {
			tableView.getItems().addAll(entityData);
		}
		session.close();
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
		stage.setScene(scene);
		stage.show();
	}

	public void switchToFacturas(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Facturas.fxml"));
		ControllerGI_Facturas ct = new ControllerGI_Facturas(sf);
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

	private void abrirDialogoCrearCliente(Event event, String dni) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoCliente.fxml"));
		ControllerDialogoCliente ct = new ControllerDialogoCliente(sf, dni, this);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();
	}

	private void buscarClientes() {
		session = sf.openSession();
		
		String codPostal = txtCodPostal.getText().trim();
		String nombre = txtNombre.getText().trim();
		String localidad = txtLocalidad.getText().trim();

		StringBuilder hql = new StringBuilder("SELECT c FROM Cliente c WHERE 1=1");

		if (!codPostal.isEmpty()) {
			hql.append(" AND STR(c.codPos) LIKE :codPostal");
		}
		if (!nombre.isEmpty()) {
			hql.append(" AND LOWER(c.nombre) LIKE :nombre");
		}
		if (!localidad.isEmpty()) {
			hql.append(" AND LOWER(c.localidad) LIKE :localidad");
		}

		Query<Cliente> query = session.createQuery(hql.toString(), Cliente.class);

		if (!codPostal.isEmpty()) {
			query.setParameter("codPostal", "%" + codPostal + "%");
		}
		if (!nombre.isEmpty()) {
			query.setParameter("nombre", "%" + nombre.toLowerCase() + "%");
		}
		if (!localidad.isEmpty()) {
			query.setParameter("localidad", "%" + localidad.toLowerCase() + "%");
		}

		List<Cliente> results = query.getResultList();
		tableView.getItems().clear();
		tableView.getItems().addAll(results);
		session.close();
	}

}
