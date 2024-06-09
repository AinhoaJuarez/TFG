package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Ticket;

import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
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
import javafx.scene.control.DatePicker;
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

public class ControllerGI_Tickets implements Initializable {
	// Botones fotos
	@FXML
    private Button btnBorrarFecha;
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
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;
	@FXML
	private TableView<Ticket> tableViewTickets;
	private SessionFactory sf;
	@FXML
	private TextField txtCod;
	@FXML
	private TextField txtTotal;
	@FXML
	private DatePicker datePickerInicio;
	@FXML
	private DatePicker datePickerFinal;
	@FXML
	private ComboBox<Cliente> comboCliente;
	@FXML
	private TableColumn<Ticket, Integer> codigoTicketColumn;
	@FXML
	private TableColumn<Ticket, String> fechaTicketColumn;
	@FXML
	private TableColumn<Ticket, Cliente> clienteAsociadoColumn;
	@FXML
	private TableColumn<Ticket, Double> totalTicketColumn;
	private Session session;

	public ControllerGI_Tickets(SessionFactory sf) {
		this.sf = sf;
	}

	// En el código, definimos un controlador JavaFX que gestiona la visualización y
	// navegación de tickets en nuestra aplicación de inventario,
	// interactuando con una base de datos a través de Hibernate.

	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarTabla();
		cargarImagenes();
		cargarClientes();
		tableViewTickets.setRowFactory(tv -> {
			TableRow<Ticket> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Ticket rowData = row.getItem();
					switchToPantallaTickets(rowData);
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
		 btnBorrarFecha.setOnAction(arg0 -> {
	            datePickerInicio.setValue(null);
	            datePickerFinal.setValue(null);
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
		btnUsers.setOnAction(arg0 -> {
			try {
				switchToUsers(arg0);
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
				switchToTickets(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txtCod.textProperty().addListener((obs, oldVal, newVal) -> searchTickets());
		datePickerInicio.valueProperty().addListener((obs, oldVal, newVal) -> searchTickets());
		datePickerFinal.valueProperty().addListener((obs, oldVal, newVal) -> searchTickets());
		comboCliente.valueProperty().addListener((obs, oldVal, newVal) -> searchTickets());
		btnDel.setOnAction(arg0 -> deleteSelectedTicket());
		
	}
	private void deleteSelectedTicket() {
		session = sf.openSession();
		session.beginTransaction();
        Ticket selectedTicket = tableViewTickets.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar este ticket?", ButtonType.YES, ButtonType.NO);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    session.remove(selectedTicket);
                    session.getTransaction().commit();
                    session.close();
                    tableViewTickets.getItems().remove(selectedTicket);
                    showAlert(Alert.AlertType.INFORMATION, "Ticket Eliminado", "El ticket ha sido eliminado.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Ninguna selección", "Por favor seleccione un ticket para eliminar.");
        }
    }
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	private void switchToPantallaTickets(Ticket selectedTicket) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaTickets.fxml"));
			ControllerTicket pantallaTicketsController = new ControllerTicket(sf, selectedTicket);
			loader.setController(pantallaTicketsController);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = (Stage) tableViewTickets.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cargarClientes() {
		session = sf.openSession();
		TypedQuery<Cliente> query = session.createQuery("SELECT c FROM Cliente c", Cliente.class);
		List<Cliente> clientes = query.getResultList();
		if (clientes != null) {
			comboCliente.getItems().add(null);
			comboCliente.getItems().addAll(clientes);
		}
		session.close();
	}

	private void searchTickets() {
		session = sf.openSession();
		tableViewTickets.getItems().clear();
		LocalDate fechaInicioStr = datePickerInicio.getValue();
		LocalDate fechaFinStr = datePickerFinal.getValue();
		String codStr = txtCod.getText();
		Cliente selectedCliente = comboCliente.getValue();

		
		StringBuilder queryStr = new StringBuilder("SELECT t FROM Ticket t WHERE 1=1");

		if (fechaInicioStr!=null) {
			queryStr.append(" AND t.fecha >= :fechaInicio");
		}
		if (fechaFinStr!=null) {
			queryStr.append(" AND t.fecha <= :fechaFin");
		}
		if (selectedCliente != null) {
			queryStr.append(" AND t.cliente = :cliente");
		}
		if (!codStr.isEmpty()) {
			queryStr.append(" AND t.numTicket = :numTicket");
		}

		TypedQuery<Ticket> query = session.createQuery(queryStr.toString(), Ticket.class);

		if (fechaInicioStr!=null) {
			query.setParameter("fechaInicio", fechaInicioStr);
		}
		if (fechaFinStr!=null) {
			query.setParameter("fechaFin", fechaFinStr);
		}
		if (selectedCliente != null) {
			query.setParameter("cliente", selectedCliente);
		}
		if (!codStr.isEmpty()) {
			query.setParameter("numTicket", Integer.parseInt(codStr));
		}

		List<Ticket> result = query.getResultList();
		if (result != null) {
			tableViewTickets.getItems().addAll(result);
		}
		session.close();
	}

	public void switchToTickets(ActionEvent arg0) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaTickets.fxml"));
		ControllerTicket ct = new ControllerTicket(sf, null);
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
		session = sf.openSession();
		tableViewTickets.getItems().clear();
		codigoTicketColumn.setCellValueFactory(new PropertyValueFactory<>("numTicket"));
		fechaTicketColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		clienteAsociadoColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		totalTicketColumn.setCellValueFactory(new PropertyValueFactory<>("importeTotal"));
		
		TypedQuery<Ticket> query = session.createQuery("SELECT e FROM Ticket e", Ticket.class);
		ArrayList<Ticket> entityData = (ArrayList<Ticket>) query.getResultList();
		if (entityData != null) {
			tableViewTickets.getItems().addAll(entityData);
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
		ControllerGI_Facturas s = new ControllerGI_Facturas(sf);
		loader.setController(s);
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
