package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Ticket;
import com.dam.europea.entidades.Usuario;

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
import javafx.scene.control.ComboBox;
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

public class ControllerGI_Users implements Initializable {
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
	private TextField txtID;
	@FXML
	private ComboBox<String> comboRol;
	// Botones sin fotos
	@FXML
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;

	private SessionFactory sf;

	@FXML
	private TableView<Usuario> tableView;
	@FXML
	private TableColumn<Usuario, String> idColumn;

	@FXML
	private TableColumn<Usuario, String> rolColumn;
	@FXML
	private TableColumn<Usuario, String> usrColumn;

	public ControllerGI_Users(SessionFactory sf) {
		this.sf = sf;
	}

	private Session session;

	public void cargarTabla() {
		session = sf.openSession();
		tableView.getItems().clear();
		idColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
		rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));
		usrColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

		TypedQuery<Usuario> query = session.createQuery("SELECT e FROM Usuario e", Usuario.class);
		ArrayList<Usuario> entityData = (ArrayList<Usuario>) query.getResultList();
		if (entityData != null) {
			tableView.getItems().addAll(entityData);
		}
		session.close();
	}

	// En el código, definimos un controlador JavaFX que gestiona la visualización y
	// navegación de familia de usuarios en nuestra aplicación de inventario,
	// interactuando con una base de datos a través de Hibernate.

	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarImagenes();
		cargarTabla();
		ArrayList<String> ivas = new ArrayList<String>();
		ivas.add(null);
		ivas.add("Administrador");
		ivas.add("Supervisor");
		ivas.add("Staff");
		comboRol.getItems().addAll(ivas);
		tableView.setRowFactory(tv -> {
			TableRow<Usuario> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
					Usuario rowData = row.getItem();
					try {
						abrirDialogoCrearUsuario(event, rowData.getIdUsuario());
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
		btnFamilia.setOnAction(arg0 -> {
			try {
				switchToFamilia(arg0);
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
		btnProductos.setOnAction(arg0 -> {
			try {
				switchToProductos(arg0);
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
		btnProveedores.setOnAction(arg0 -> {
			try {
				switchToProv(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnNew.setOnAction(arg0 -> {
			try {
				abrirDialogoCrearUsuario(arg0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txtID.textProperty().addListener((observable, oldValue, newValue) -> searchUsuarios());
		comboRol.valueProperty().addListener((observable, oldValue, newValue) -> searchUsuarios());
		btnDel.setOnAction(arg0 -> deleteSelectedUser());
	}

	private void deleteSelectedUser() {
		session = sf.openSession();
		session.beginTransaction();
		Usuario selectedUser = tableView.getSelectionModel().getSelectedItem();
		if (selectedUser != null) {
			Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
					"¿Estás seguro de que deseas eliminar este usuario?", ButtonType.YES, ButtonType.NO);
			confirmationAlert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.YES) {
					session.remove(selectedUser);
					session.getTransaction().commit();
					session.close();
					tableView.getItems().remove(selectedUser);
					showAlert(Alert.AlertType.INFORMATION, "Usuario Eliminado", "El usuario ha sido eliminado.");
				}
			});
		} else {
			showAlert(Alert.AlertType.WARNING, "Ninguna selección", "Por favor seleccione un usuario para eliminar.");
		}
	}

	private void showAlert(Alert.AlertType alertType, String title, String message) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void searchUsuarios() {
		session = sf.openSession();
		String id = txtID.getText().trim().toLowerCase();
		String rol = comboRol.getValue() != null ? comboRol.getValue().trim().toLowerCase() : "";

		StringBuilder hql = new StringBuilder("SELECT u FROM Usuario u WHERE 1=1");

		if (!id.isEmpty()) {
			hql.append(" AND LOWER(u.idUsuario) LIKE :id");
		}
		if (!rol.isEmpty()) {
			hql.append(" AND LOWER(u.rol) LIKE :rol");
		}

		TypedQuery<Usuario> query = session.createQuery(hql.toString(), Usuario.class);

		if (!id.isEmpty()) {
			query.setParameter("id", "%" + id + "%");
		}
		if (!rol.isEmpty()) {
			query.setParameter("rol", "%" + rol + "%");
		}

		List<Usuario> results = query.getResultList();

		tableView.getItems().clear();

		tableView.getItems().addAll(results);
		session.close();
	}

	private void abrirDialogoCrearUsuario(Event event, String idUsr) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoUsuario.fxml"));
		ControllerDialogoUsuario ct = new ControllerDialogoUsuario(sf, idUsr, this);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();

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

	public void switchToProductos(ActionEvent event) throws IOException {
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
}
