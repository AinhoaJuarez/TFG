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
import org.hibernate.query.Query;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Ticket;
import com.dam.europea.entidades.TicketProductos;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ControllerTicket implements Initializable {
	@FXML
	private Button botonSalir;
	@FXML
	private Button botonMenu;
	@FXML
	private Button botonAsociar;
	@FXML
	private Button botonDesasociar;
	@FXML
	private Button botonDelLinea;
	@FXML
	private Button botonDelTicket;
	@FXML
	private Button botonBuscar;
	@FXML
	private Button botonAgregar;
	@FXML
	private Button botonTotal;
	@FXML
	private TextField txt_codBarras;
	@FXML
	private TextField txt_desArticulo;
	@FXML
	private TextField txt_precio;
	@FXML
	private TextField txt_cantidad;
	@FXML
	private TextField txt_descuento;
	@FXML
	private TextField txt_precioDes;
	@FXML
	private TextField txt_totalTicket;
	@FXML
	private Label lbl_Cliente;
	@FXML
	private Label lbl_NumTicket;
	@FXML
	private TableView<TicketProductos> tableView;
	@FXML
	private TableColumn<TicketProductos, String> colCodBarras;
	@FXML
	private TableColumn<TicketProductos, String> colDescripcion;
	@FXML
	private TableColumn<TicketProductos, Integer> colCantidad;
	@FXML
	private TableColumn<TicketProductos, Double> colDescuento;
	@FXML
	private TableColumn<TicketProductos, Double> colPrecio;
	private SessionFactory sf;
	private Session session;
	private Producto producto;
	private TicketProductos ticketProducto;
	private Ticket ticket;
	private Cliente clienteTicket;

	public ControllerTicket(SessionFactory sf, Ticket ticket) {
		this.sf = sf;
		this.ticket = ticket;
	}

	// En este código, definimos un controlador JavaFX que gestiona la visualización
	// y manipulación de tickets en nuestra aplicación de inventario,
	// permitiendo también la asociación de clientes a través de una base de datos
	// usando Hibernate.

	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		session = sf.openSession();
		session.beginTransaction();

		if (ticket != null) {
			loadTicketDetails();
			loadTicketProductos();
		} else {
			nuevoTicketProducto();
		}
		cargarImagenes();
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
		botonBuscar.setOnAction(arg0 -> {
			try {
				abrirDialogoSeleccionProducto(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botonAsociar.setOnAction(arg0 -> {
			try {
				abrirDialogoSeleccionCliente(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txt_codBarras.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				producto = getProductByCodigoProducto(newValue);
				if (producto != null) {
					txt_desArticulo.setText(producto.getDescripcion());
					txt_precio.setText(String.valueOf(producto.getPrecioVenta()));
				} else {
					showProductoNotFoundError();

				}
			} else {
				txt_desArticulo.clear();
			}
		});
		botonAgregar.setOnAction(arg0 -> {
			try {
				addToticket(arg0);
				updateTotalTicketPrice();
				session.beginTransaction();
				cargarTabla();
				ticketProducto = new TicketProductos();

				Query<Long> query = session.createQuery("select max(tp.id) from TicketProductos tp", Long.class);
	            Long lastId = query.getSingleResult();

				// Increment the last ID by one and set it for the new TicketProducto
				if (lastId != null) {
					ticketProducto.setId(lastId + 1);

				} else {
					// If there are no existing TicketProductos, start with ID 1
					ticketProducto.setId(1L);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txt_descuento.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				try {
					double discount = Double.parseDouble(newValue);
					double originalPrice = Double.parseDouble(txt_precio.getText());
					double discountedPrice = originalPrice * (1 - (discount / 100));
					txt_precioDes.setText(String.format("%.2f", discountedPrice));
				} catch (NumberFormatException e) {
					// Handle non-numeric input
					txt_precioDes.setText("");
				}
			} else {
				txt_precioDes.setText("");
			}
		});
		botonTotal.setOnAction(arg0 -> {
			try {
				handleShowTotalDialog(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void loadTicketDetails() {
		txt_totalTicket.setText(String.valueOf(ticket.getImporteTotal()));
		// Add any other fields you need to populate from the ticket object
	}

	public void loadTicketProductos() {
		colCodBarras.setCellValueFactory(cellData -> {
			if (cellData.getValue().getProducto() != null) {
				return new javafx.beans.property.SimpleStringProperty(
						cellData.getValue().getProducto().getCodigoBarras());
			} else {
				return new javafx.beans.property.SimpleStringProperty("");
			}
		});
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));

		TypedQuery<TicketProductos> query = session
				.createQuery("SELECT tp FROM TicketProductos tp WHERE tp.numTicket = :ticket", TicketProductos.class);
		query.setParameter("ticket", ticket);

		List<TicketProductos> ticketProductosList = query.getResultList();
		tableView.getItems().setAll(ticketProductosList);
		ticketProducto = new TicketProductos();
		Query<Long> query2 = session.createQuery("select max(tp.id) from TicketProductos tp", Long.class);
        Long lastId = query2.getSingleResult();

		if (lastId != null) {
			ticketProducto.setId(lastId + 1);
		} else {
			ticketProducto.setId(1L);
		}

	}

	private void updateTotalTicketPrice() {
		TypedQuery<Double> query = session.createQuery(
				"SELECT SUM(tp.precioTotal) FROM TicketProductos tp WHERE tp.numTicket = :ticket", Double.class);
		query.setParameter("ticket", ticket);
		Double total = query.getSingleResult();
		if (total != null) {
			txt_totalTicket.setText(String.format("%.2f", total));
		} else {
			txt_totalTicket.setText("0.00");
		}
	}

	private void showProductoNotFoundError() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Producto no encontrado");
		alert.setHeaderText(null);
		alert.setContentText("No se encontró ningún producto con el código ingresado.");
		alert.showAndWait();
	}

	public void cargarTabla() {
		tableView.getItems().clear();
		colCodBarras.setCellValueFactory(cellData -> {
			if (cellData.getValue().getProducto() != null) {
				return new javafx.beans.property.SimpleStringProperty(
						cellData.getValue().getProducto().getCodigoBarras());
			} else {
				return new javafx.beans.property.SimpleStringProperty("");
			}
		});
		colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));
		colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));

		TypedQuery<TicketProductos> query = session
				.createQuery("SELECT e FROM TicketProductos e WHERE e.numTicket = :ticket", TicketProductos.class);
		query.setParameter("ticket", ticket);

		ArrayList<TicketProductos> entityData = (ArrayList<TicketProductos>) query.getResultList();
		if (entityData != null) {
			tableView.getItems().addAll(entityData);
		}
	}

	public Producto getProductByCodigoProducto(String codigoProducto) {

		Query<Producto> query = session.createQuery("FROM Producto p WHERE p.codigoBarras = :codigoBarras",
				Producto.class);
		query.setParameter("codigoBarras", codigoProducto);
		Producto return2 = query.uniqueResult();
		setProductDetails(return2);
		return return2;

	}

	public void nuevoTicketProducto() {
		ticket = new Ticket();
		ticketProducto = new TicketProductos();
		Query<Long> query = session.createQuery("select max(tp.id) from TicketProductos tp", Long.class);
        Long lastId = query.getSingleResult();

		if (lastId != null) {
			ticketProducto.setId(lastId + 1);
		} else {
			ticketProducto.setId(1L);
		}
		session.persist(ticket);
		session.getTransaction().commit();
		lbl_NumTicket.setText("Nº de ticket: " + String.valueOf(ticket.getNumTicket()));
		session.beginTransaction();
	}

	public void cargarImagenes() {
		InputStream archivoCerrar = getClass().getResourceAsStream("/cerrar-sesion.png");
		Image imagenCerrar = new Image(archivoCerrar, 25, 25, true, true);
		botonSalir.setGraphic(new ImageView(imagenCerrar));
		InputStream archivoMenu = getClass().getResourceAsStream("/menu.png");
		Image imagenMenu = new Image(archivoMenu, 25, 25, true, true);
		botonMenu.setGraphic(new ImageView(imagenMenu));
		InputStream archivoAsoc = getClass().getResourceAsStream("/asociar-cliente.png");
		Image imagenAsoc = new Image(archivoAsoc, 100, 100, true, true);
		botonAsociar.setGraphic(new ImageView(imagenAsoc));
		InputStream archivoDesAsoc = getClass().getResourceAsStream("/quitar-cliente.png");
		Image imagenDesAsc = new Image(archivoDesAsoc, 100, 100, true, true);
		botonDesasociar.setGraphic(new ImageView(imagenDesAsc));
		InputStream archivoQuitLinea = getClass().getResourceAsStream("/eliminar-linea.png");
		Image imagenQuitLinea = new Image(archivoQuitLinea, 100, 100, true, true);
		botonDelLinea.setGraphic(new ImageView(imagenQuitLinea));
		InputStream archivoQuitTicket = getClass().getResourceAsStream("/eliminar-ticket.png");
		Image imagenQuitTicket = new Image(archivoQuitTicket, 100, 100, true, true);
		botonDelTicket.setGraphic(new ImageView(imagenQuitTicket));
		InputStream archivoBuscar = getClass().getResourceAsStream("/lupa.png");
		Image imagenBuscar = new Image(archivoBuscar, 15, 15, true, true);
		botonBuscar.setGraphic(new ImageView(imagenBuscar));
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

	private void abrirDialogoSeleccionCliente(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoSelCliente.fxml"));
		ControladorSelCliente ct = new ControladorSelCliente(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		ct.setParentController(this, null);

		stage.show();
	}

	public void abrirDialogoSeleccionProducto(ActionEvent arg0) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoSelProducto.fxml"));
		ControladorSelProducto ct = new ControladorSelProducto(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) arg0.getSource()).getScene().getWindow());
		stage.setScene(scene);
		ct.setParentController(this, null);

		stage.show();
	}

	public void setProductDetails(Producto product) {
		txt_codBarras.setText(product.getCodigoBarras());
		txt_desArticulo.setText(product.getDescripcion());
		txt_precio.setText(String.valueOf(product.getPrecioVenta()));
	}

	@FXML
	private void handleShowTotalDialog(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoFinalTicket.fxml"));
			// Load the FXML file for the dialog
			TicketDialogController controller = new TicketDialogController();

			// Set the controller on the loader
			loader.setController(controller);
			Parent root = loader.load();

			controller.setTotalTicket(Double.valueOf(txt_totalTicket.getText().replace(',', '.'))); // Pass the total ticket amount

			// Create the dialog stage
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setScene(new Scene(root));

			// Show the dialog and wait for it to close
			dialogStage.showAndWait();

			if (controller.isAccepted()) {
				double descuentoTicket = controller.getDiscount();
				double importeFinal = controller.getFinalAmount();

				// Perform actions with descuentoTicket and importeFinal
				System.out.println("Descuento: " + descuentoTicket);
				System.out.println("Importe Final: " + importeFinal);

				// Call mergeTotalFactura with the new values
				mergeTotalFactura(event, descuentoTicket, importeFinal);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mergeTotalFactura(ActionEvent event, double descuentoTicket, double importeFinal) {
		try {
			double totalTicket = Double.parseDouble(txt_totalTicket.getText().replace(',', '.'));

			// Ensure that the factura object is not null

			if (ticket != null) {
				LocalDate currentDate = LocalDate.now();
				ticket.setFecha(java.sql.Date.valueOf(currentDate));
				ticket.setImporteTotal(totalTicket);
				ticket.setCliente(clienteTicket);
				ticket.setDescuentoTicket(descuentoTicket);
				ticket.setImporteTicket(importeFinal);
				session.merge(ticket);

				// Commit transaction
				session.getTransaction().commit();

				// Show a confirmation message to the user
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Factura Actualizada");
				alert.setHeaderText(null);
				alert.setContentText("El ticket ha sido agregado con éxito.");
				alert.showAndWait();
			} else {
				// If factura object is null, show an error message
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("No se pudo actualizar el ticket. El ticket es nulo.");
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			// Handle the case where the total factura value cannot be parsed as a double
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("El valor total de la factura no es válido.");
			alert.showAndWait();
		} catch (Exception e) {
			// Handle any other exceptions
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Se produjo un error al actualizar la factura: " + e.getMessage());
			alert.showAndWait();
		}
	}

	public void addToticket(ActionEvent event) {
		StringBuilder missingFields = new StringBuilder();

		if (txt_cantidad.getText().isEmpty()) {
			missingFields.append("Cantidad\n");
		}
		if (txt_precioDes.getText().isEmpty() && txt_precio.getText().isEmpty()) {
			missingFields.append("Precio Descuento o Precio\n");
		}
		if (txt_desArticulo.getText().isEmpty() && txt_codBarras.getText().isEmpty()) {
			missingFields.append("Descripción Artículo\n");
		}

		// If there are missing fields, show an alert and return early
		if (missingFields.length() > 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Campos Faltantes");
			alert.setHeaderText("Faltan los siguientes campos:");
			alert.setContentText(missingFields.toString());
			alert.showAndWait();
			return;
		}

		try {
			ticketProducto.setCantidad(Integer.valueOf(txt_cantidad.getText()));

			if (!txt_codBarras.getText().isEmpty()) {
				producto = getProductByCodigoProducto(txt_codBarras.getText());
				ticketProducto.setProducto(producto);
				ticketProducto.setDescripcion(producto.getDescripcion());

				if (txt_descuento.getText().isEmpty()) {
					ticketProducto.setDescuento(0);
				} else {
					ticketProducto.setDescuento(Double.valueOf(txt_descuento.getText()));
				}

				if (txt_precioDes.getText().isEmpty()) {
					ticketProducto.setPrecioDescuento(0);
					ticketProducto.setPrecioTotal(Double.valueOf(txt_precio.getText().replace(',', '.')));
				} else {
					ticketProducto.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
					ticketProducto.setPrecioTotal(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
				}
			} else {
				ticketProducto.setDescripcion(txt_desArticulo.getText());

				if (txt_descuento.getText().isEmpty()) {
					ticketProducto.setDescuento(0);
				} else {
					ticketProducto.setDescuento(Double.valueOf(txt_descuento.getText()));
				}

				if (txt_precioDes.getText().isEmpty()) {
					ticketProducto.setPrecioDescuento(0);
					ticketProducto.setPrecioTotal(Double.valueOf(txt_precio.getText().replace(',', '.')));
				} else {
					ticketProducto.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
					ticketProducto.setPrecioTotal(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
				}
			}

			ticketProducto.setTicket(ticket);
			session.persist(ticketProducto);
			session.getTransaction().commit();
		} catch (NumberFormatException e) {
			// Handle number format exceptions
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error de Formato");
			alert.setHeaderText(null);
			alert.setContentText(
					"Por favor, ingrese un valor numérico válido en los campos de cantidad, descuento y precio.");
			alert.showAndWait();
		} catch (Exception e) {
			// Handle any other exceptions
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Se produjo un error al agregar el producto al ticket: " + e.getMessage());
			alert.showAndWait();
		}
	}

	public void setClienteDetails(Cliente selectedCli) {
		clienteTicket = selectedCli;
		lbl_Cliente.setText("Cliente: " + clienteTicket.getNombre());

	}

}
