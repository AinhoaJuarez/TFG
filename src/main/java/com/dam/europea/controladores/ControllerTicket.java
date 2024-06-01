package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.Factura;
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

	public ControllerTicket(SessionFactory sf) {
		this.sf = sf;
	}

	//En este código, definimos un controlador JavaFX que gestiona la visualización y manipulación de tickets en nuestra aplicación de inventario,
	//permitiendo también la asociación de clientes a través de una base de datos usando Hibernate.
	
	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		session = sf.openSession();
		session.beginTransaction();
		nuevoTicketProducto();

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
				
				Long lastId = (Long) session.createQuery("select max(tp.id) from TicketProductos tp").uniqueResult();

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
		return query.uniqueResult();

	}

	public void nuevoTicketProducto() {
		ticket = new Ticket();
		ticketProducto = new TicketProductos();
		Long lastId = (Long) session.createQuery("select max(tp.id) from TicketProductos tp").uniqueResult();

		// Increment the last ID by one and set it for the new TicketProducto
		if (lastId != null) {
			ticketProducto.setId(lastId + 1);
		} else {
			// If there are no existing TicketProductos, start with ID 1
			ticketProducto.setId(1L);
		}

		session.persist(ticket);
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
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();
	}

	public void addToticket(ActionEvent event) {
		if (!txt_codBarras.getText().isEmpty()) {
			producto = getProductByCodigoProducto(txt_codBarras.getText());
			ticketProducto.setProducto(producto);
			ticketProducto.setDescripcion(producto.getDescripcion());
			ticketProducto.setPrecioTotal(producto.getPrecioVenta());
			ticketProducto.setCantidad(Integer.valueOf(txt_cantidad.getText()));
			if (txt_descuento.getText().isEmpty()) {
				ticketProducto.setDescuento(0);
			} else {
				ticketProducto.setDescuento(Double.valueOf(txt_descuento.getText()));
			}
			if (txt_precioDes.getText().isEmpty()) {
				ticketProducto.setPrecioDescuento(0);
			} else {
				ticketProducto.setPrecioDescuento(Double.valueOf(txt_precioDes.getText()));
			}
			ticketProducto.setTicket(ticket);
			session.persist(ticketProducto);
			session.getTransaction().commit();
		
		} else {
			System.out.println("Numero de ticketproducto: " + ticketProducto.getId());
			ticketProducto.setDescripcion(txt_desArticulo.getText());
			ticketProducto.setPrecioTotal(Double.valueOf(txt_precio.getText()));
			ticketProducto.setCantidad(Integer.valueOf(txt_cantidad.getText()));
			if (txt_descuento.getText().isEmpty()) {
				ticketProducto.setDescuento(0);
			} else {
				ticketProducto.setDescuento(Double.valueOf(txt_descuento.getText()));
			}
			if (txt_precioDes.getText().isEmpty()) {
				ticketProducto.setPrecioDescuento(0);
			} else {
				ticketProducto.setPrecioDescuento(Integer.valueOf(txt_precioDes.getText()));
			}
			ticketProducto.setTicket(ticket);
			System.out.println(ticketProducto.toString());
			session.persist(ticketProducto);
			session.getTransaction().commit();
			

		}
	}

}
