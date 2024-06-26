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
import com.dam.europea.entidades.Factura;
import com.dam.europea.entidades.Producto;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

public class ControllerFactura implements Initializable {
	@FXML
	private Button botonSalir;
	@FXML
	private Button botonMenu;
	@FXML
	private Button botonAsociar;
	@FXML
	private Button botonDesasociar;
	@FXML
	private Button botonTicket;
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
	private TextField txt_TotalFactura;
	@FXML
	private TextField txt_NumFactura;
	@FXML
	private Button botonTotal;
	@FXML
	private Label lbl_Cliente;
	@FXML
	private Label lblFacturas;
	@FXML
	private Button btnDelFactura;
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
	@FXML
	private DatePicker botonFecha;
	private SessionFactory sf;
	private Session session;
	private Factura factura;
	private TicketProductos ticketProducto;
	private Producto producto;
	private Cliente clienteFactura;

	public ControllerFactura(SessionFactory sf, Factura factura) {
		this.sf = sf;
		this.factura = factura;
	}

	// Inicializamos el controlador, cargamos imágenes y configuramos los botones
	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		configurarTabla();
		cargarImagenes();
		if (factura != null) {
			loadFacturaDetails();
			loadFacturaProductos();
			cargarTabla();
		} else {
			nuevoTicketProducto();
		}
		lblFacturas.setOnMouseClicked(event -> {
			// Simulate ActionEvent
			try {
				modDatosEmpresa(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		botonAsociar.setOnAction(arg0 -> {
			try {
				abrirDialogoSeleccionCliente(arg0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botonTicket.setOnAction(arg0 -> {
			try {
				abrirDialogoSeleccionTicket(arg0);
				updateTotalFacturaPrice();
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
				if (tableView.getSelectionModel().getSelectedItem() != null) {
					updateProductInFactura(arg0);
				} else {
					addTofactura(arg0);
				}
				updateTotalFacturaPrice();
				session = sf.openSession();
				session.beginTransaction();
				cargarTabla();
				ticketProducto = new TicketProductos();

				Query<Long> query = session.createQuery("select max(tp.id) from TicketProductos tp", Long.class);
				Long lastId = query.getSingleResult();
				if (lastId != null) {
					ticketProducto.setId(lastId + 1);

				} else {
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
					txt_precioDes.setText("");
				}
			} else {
				txt_precioDes.setText("");
			}
		});
		botonTotal.setOnAction(this::mergeTotalFactura);
		botonFecha.setValue(LocalDate.now());
		botonBuscar.setOnAction(arg0 -> {
			try {
				abrirDialogoSeleccionProducto(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				populateFieldsFromSelectedProduct(newValue);
			}
		});
		botonDesasociar.setOnAction(this::handleDesasociar);
		botonDelTicket.setOnAction(this::desasociarTicket);

		btnDelFactura.setOnAction(event -> {
			try {
				borrarFactura(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void modDatosEmpresa(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoDatosEmpresa.fxml"));
		ControladorDialogoDatosEmpresa ct = new ControladorDialogoDatosEmpresa(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();
		
	}

	private void desasociarTicket(ActionEvent event) {
		session = sf.openSession();
		session.beginTransaction();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmar Desasociación de Ticket");
		alert.setHeaderText(null);
		alert.setContentText(
				"¿Está seguro de que desea desasociar el ticket? Esto eliminará todos los productos asociados a la factura.");
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				TypedQuery<TicketProductos> query = session.createQuery(
						"SELECT tp FROM TicketProductos tp WHERE tp.numeroFactura=:factura", TicketProductos.class);
				query.setParameter("factura", factura);
				List<TicketProductos> ticketProductosList = query.getResultList();

				for (TicketProductos ticketProducto : ticketProductosList) {
					ticketProducto.setFactura(null);
					session.merge(ticketProducto);
				}
				session.getTransaction().commit();
				tableView.getItems().clear();
				cargarTabla();
				Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
				successAlert.setTitle("Desasociación Exitosa");
				successAlert.setHeaderText(null);
				successAlert.setContentText("El ticket ha sido desasociado exitosamente.");
				successAlert.showAndWait();
			}
		});
		session.close();
	}

	private void handleDesasociar(ActionEvent event) {
		session = sf.openSession();
		session.beginTransaction();
		if (factura != null) {
			factura.setCliente(null);
			lbl_Cliente.setText("Cliente: No asignado");
			session.merge(factura);
			session.getTransaction().commit();
			session.beginTransaction();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Cliente Desasociado");
			alert.setHeaderText(null);
			alert.setContentText("El cliente ha sido desasociado del ticket.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Ticket No Encontrado");
			alert.setHeaderText(null);
			alert.setContentText("No hay un ticket activo para desasociar un cliente.");
			alert.showAndWait();
		}
		session.close();
	}

	private void populateFieldsFromSelectedProduct(TicketProductos selectedProduct) {
		if (selectedProduct.getProducto() != null) {
			txt_codBarras.setText(selectedProduct.getProducto().getCodigoBarras());
		} else {
			txt_desArticulo.setText(selectedProduct.getDescripcion());
			txt_precio.setText(String.valueOf(selectedProduct.getPrecioTotal()));
			txt_cantidad.setText(String.valueOf(selectedProduct.getCantidad()));
			txt_descuento.setText(String.valueOf(selectedProduct.getDescuento()));
			txt_precioDes.setText(String.valueOf(selectedProduct.getPrecioDescuento()));
		}
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
		ct.setParentController(null, this);

		stage.show();
	}

	private void mergeTotalFactura(ActionEvent event) {
		session = sf.openSession();
		session.beginTransaction();
		try {
			double totalFactura = Double.parseDouble(txt_TotalFactura.getText().replace(',', '.'));
			if (factura != null) {
				LocalDate fechaExpedicion = botonFecha.getValue();
				if (fechaExpedicion != null) {
					factura.setFechaExpedicion(LocalDate.now());
					factura.setFechaOperacion(fechaExpedicion);
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("Por favor, seleccione una fecha de expedición.");
					alert.showAndWait();
					return;
				}
				TypedQuery<TicketProductos> query = session.createQuery(
						"SELECT tp FROM TicketProductos tp WHERE tp.numeroFactura = :factura", TicketProductos.class);
				query.setParameter("factura", factura);
				List<TicketProductos> ticketProductosList = query.getResultList();
				double totalSinIva = 0.0;
				double IVA = 0.0;
				for (TicketProductos tp : ticketProductosList) {
					double calculo = tp.getProducto().getPrecioVenta() * tp.getProducto().getFamiliaArticulo().getIVA()
							/ 100;
					IVA = IVA + calculo;

				}
				totalSinIva = totalFactura - IVA;
				factura.setTotalConIVA(totalFactura);
				factura.setIVA(IVA);
				factura.setTotalSinIVA(totalSinIva);

				factura.setCliente(clienteFactura);
				session.merge(factura);
				session.getTransaction().commit();

				txt_codBarras.clear();
				txt_desArticulo.clear();
				txt_precio.clear();
				txt_cantidad.clear();
				txt_descuento.clear();
				txt_precioDes.clear();
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Factura Actualizada");
				alert.setHeaderText(null);
				alert.setContentText("La factura ha sido actualizada con éxito en la base de datos.");
				alert.showAndWait();
				saveFacturaToPDF();

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("No se pudo actualizar la factura. La factura es nula.");
				alert.showAndWait();
			}
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("El valor total de la factura no es válido.");
			alert.showAndWait();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Se produjo un error al actualizar la factura: " + e.getMessage());
			alert.showAndWait();
		}
		session.close();
	}

	public void saveFacturaToPDF() {
		String dest = "factura_" + factura.getNumeroFactura() + ".pdf";
		PDFGenerator.createFacturaPDF(factura, dest, sf);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("PDF Generado");
		alert.setHeaderText(null);
		alert.setContentText("La factura ha sido guardada como PDF en " + dest);
		alert.showAndWait();
	}

	public void updateTotalFacturaPrice() {
		session = sf.openSession();
		TypedQuery<Double> query = session.createQuery(
				"SELECT SUM(tp.precioTotal) FROM TicketProductos tp WHERE tp.numeroFactura = :factura", Double.class);
		query.setParameter("factura", factura);
		Double total = query.getSingleResult();
		if (total != null) {
			txt_TotalFactura.setText(String.format("%.2f", total));
		} else {
			txt_TotalFactura.setText("0.00");
		}
		session.close();
	}

	private void showProductoNotFoundError() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Producto no encontrado");
		alert.setHeaderText(null);
		alert.setContentText("No se encontró ningún producto con el código ingresado.");
		alert.showAndWait();
	}

	public void cargarTabla() {
		session = sf.openSession();
		tableView.getItems().clear();

		TypedQuery<TicketProductos> query = session
				.createQuery("SELECT e FROM TicketProductos e WHERE e.numeroFactura = :factura", TicketProductos.class);
		query.setParameter("factura", factura);

		ArrayList<TicketProductos> entityData = (ArrayList<TicketProductos>) query.getResultList();
		if (entityData != null) {
			tableView.getItems().addAll(entityData);
		}
		session.close();
	}

	public Producto getProductByCodigoProducto(String codigoProducto) {
		session = sf.openSession();
		Query<Producto> query = session.createQuery("FROM Producto p WHERE p.codigoBarras = :codigoBarras",
				Producto.class);
		query.setParameter("codigoBarras", codigoProducto);
		Producto return2 = query.uniqueResult();
		setProductDetails(return2);
		session.close();
		return return2;

	}

	private void loadFacturaDetails() {
		txt_TotalFactura.setText(String.valueOf(factura.getTotalConIVA()).replace('.', ','));
		if (factura.getCliente() != null) {
			lbl_Cliente.setText("Cliente: " + factura.getCliente().getNombre());
		}

		botonFecha.setValue(factura.getFechaOperacion());
		txt_NumFactura.setText(String.valueOf(factura.getNumeroFactura()));
	}

	public void setProductDetails(Producto product) {
		txt_codBarras.setText(product.getCodigoBarras());
		txt_desArticulo.setText(product.getDescripcion());
		txt_precio.setText(String.valueOf(product.getPrecioVenta()));
	}

	public void nuevoTicketProducto() {
		try {
			session = sf.openSession();
			session.beginTransaction();

			factura = new Factura();
			ticketProducto = new TicketProductos();
			Query<Long> query = session.createQuery("select max(tp.id) from TicketProductos tp", Long.class);
			Long lastTicketProductoId = query.uniqueResult();

			if (lastTicketProductoId != null) {
				ticketProducto.setId(lastTicketProductoId + 1);
			} else {
				ticketProducto.setId(1L);
			}
			Query<Integer> query2 = session.createQuery("select max(tp.numeroFactura) from Factura tp", Integer.class);
			Integer lastFacturaId = query2.uniqueResult();

			if (lastFacturaId != null) {
				factura.setNumeroFactura(lastFacturaId + 1);
			} else {
				factura.setNumeroFactura(1);
			}
			session.persist(factura);
			session.getTransaction().commit();
			txt_NumFactura.setText(String.valueOf(factura.getNumeroFactura()));
		} catch (Exception e) {
			if (session != null && session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
			showAlert("Error", "An error occurred while creating a new TicketProducto: " + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void loadFacturaProductos() {
		session = sf.openSession();

		TypedQuery<TicketProductos> query = session.createQuery(
				"SELECT tp FROM TicketProductos tp WHERE tp.numeroFactura = :factura", TicketProductos.class);
		query.setParameter("factura", factura);

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
		session.close();
	}

	private void configurarTabla() {
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
		InputStream archivoQuitLinea = getClass().getResourceAsStream("/ticketAsoc.png");
		Image imagenQuitLinea = new Image(archivoQuitLinea, 100, 100, true, true);
		botonTicket.setGraphic(new ImageView(imagenQuitLinea));
		InputStream archivoQuitTicket = getClass().getResourceAsStream("/eliminar-ticket.png");
		Image imagenQuitTicket = new Image(archivoQuitTicket, 100, 100, true, true);
		botonDelTicket.setGraphic(new ImageView(imagenQuitTicket));
		InputStream archivoBuscar = getClass().getResourceAsStream("/lupa.png");
		Image imagenBuscar = new Image(archivoBuscar, 15, 15, true, true);
		botonBuscar.setGraphic(new ImageView(imagenBuscar));
		InputStream archivoMenos = getClass().getResourceAsStream("/eliminar-linea.png");
		Image imagenMenos = new Image(archivoMenos, 50, 50, true, true);
		btnDelFactura.setGraphic(new ImageView(imagenMenos));
	}

	// Método para cambiar a la vista del menú principal
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

	// Método para cambiar a la vista de inicio de sesión
	public void switchToInicioSesion(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
		ControladorInicioSesion ct = new ControladorInicioSesion(sf);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - scene.getWidth()) * 0.17;
		double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight()) * 0.12;
		stage.setX(centerX);
		stage.setY(centerY);
		stage.setScene(scene);
		stage.show();
	}

	// Método para abrir el diálogo de selección de cliente
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
		ct.setParentController(null, this);

		stage.show();
	}

	// Método para abrir el diálogo de selección de ticket
	private void abrirDialogoSeleccionTicket(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoSelFact.fxml"));
		ControladorSelTicket ct = new ControladorSelTicket(sf, this, factura);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();
	}

	public void addTofactura(ActionEvent event) {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
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
			if (missingFields.length() > 0) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Campos Faltantes");
				alert.setHeaderText("Faltan los siguientes campos:");
				alert.setContentText(missingFields.toString());
				alert.showAndWait();
				return;
			}

			try {
				int cantidad = 0;
				cantidad = Integer.valueOf(txt_cantidad.getText());
				ticketProducto.setCantidad(cantidad);
				double precio = 0.0;

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
						precio = Double.valueOf(txt_precio.getText().replace(',', '.'));
						ticketProducto.setPrecioTotal(precio * cantidad);
					} else {
						ticketProducto.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
						precio = Double.valueOf(txt_precioDes.getText().replace(',', '.'));
						ticketProducto.setPrecioTotal(precio * cantidad);
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
						precio = Double.valueOf(txt_precio.getText().replace(',', '.'));
						ticketProducto.setPrecioTotal(precio * cantidad);
					} else {
						ticketProducto.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
						precio = Double.valueOf(txt_precioDes.getText().replace(',', '.'));
						ticketProducto.setPrecioTotal(precio * cantidad);
					}
				}

				ticketProducto.setFactura(factura);
				session.persist(ticketProducto);
				session.getTransaction().commit();

				txt_codBarras.clear();
				txt_desArticulo.clear();
				txt_precio.clear();
				txt_cantidad.clear();
				txt_descuento.clear();
				txt_precioDes.clear();
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error de Formato");
				alert.setHeaderText(null);
				alert.setContentText(
						"Por favor, ingrese un valor numérico válido en los campos de cantidad, descuento y precio.");
				alert.showAndWait();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Se produjo un error al agregar el producto a la factura: " + e.getMessage());
				alert.showAndWait();
			}
		}
	}

	public void updateProductInFactura(ActionEvent event) {
		try (Session session = sf.openSession()) {
			session.beginTransaction();
			TicketProductos selectedProduct = tableView.getSelectionModel().getSelectedItem();
			try {
				int cantidad = 0;
				cantidad = Integer.valueOf(txt_cantidad.getText());
				selectedProduct.setCantidad(cantidad);
				double precio = 0.0;
				if (!txt_codBarras.getText().isEmpty()) {
					producto = getProductByCodigoProducto(txt_codBarras.getText());
					selectedProduct.setProducto(producto);
					selectedProduct.setDescripcion(producto.getDescripcion());

					if (txt_descuento.getText().isEmpty()) {
						selectedProduct.setDescuento(0);
					} else {
						selectedProduct.setDescuento(Double.valueOf(txt_descuento.getText()));
					}

					if (txt_precioDes.getText().isEmpty() || Double.valueOf(txt_precioDes.getText()) == 0.0) {
						selectedProduct.setPrecioDescuento(0);
						precio = Double.valueOf(txt_precio.getText().replace(',', '.'));
						double finalPrecio = precio * cantidad;
						String formattedPrice = String.format("%.2f", finalPrecio);
						selectedProduct.setPrecioTotal(Double.parseDouble(formattedPrice));
					} else {
						selectedProduct.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
						precio = Double.valueOf(txt_precioDes.getText().replace(',', '.'));
						double finalPrecio = precio * cantidad;
						String formattedPrice = String.format("%.2f", finalPrecio);
						selectedProduct.setPrecioTotal(Double.parseDouble(formattedPrice));
					}
				} else {
					selectedProduct.setDescripcion(txt_desArticulo.getText());

					if (txt_descuento.getText().isEmpty()) {
						selectedProduct.setDescuento(0);
					} else {
						selectedProduct.setDescuento(Double.valueOf(txt_descuento.getText()));
					}

					if (txt_precioDes.getText().isEmpty() || Double.valueOf(txt_precioDes.getText()) == 0.0) {
						selectedProduct.setPrecioDescuento(0);
						precio = Double.valueOf(txt_precio.getText().replace(',', '.'));
						double finalPrecio = precio * cantidad;
						String formattedPrice = String.format("%.2f", finalPrecio);
						selectedProduct.setPrecioTotal(Double.parseDouble(formattedPrice));
					} else {
						selectedProduct.setPrecioDescuento(Double.valueOf(txt_precioDes.getText().replace(',', '.')));
						precio = Double.valueOf(txt_precioDes.getText().replace(',', '.'));
						double finalPrecio = precio * cantidad;
						String formattedPrice = String.format("%.2f", finalPrecio);
						selectedProduct.setPrecioTotal(Double.parseDouble(formattedPrice));
					}
				}
				System.out.println(selectedProduct.toString());
				session.merge(selectedProduct);
				session.getTransaction().commit();

				txt_codBarras.clear();
				txt_desArticulo.clear();
				txt_precio.clear();
				txt_cantidad.clear();
				txt_descuento.clear();
				txt_precioDes.clear();
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error de Formato");
				alert.setHeaderText(null);
				alert.setContentText(
						"Por favor, ingrese un valor numérico válido en los campos de cantidad, descuento y precio.");
				alert.showAndWait();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Se produjo un error al actualizar el producto en la factura: " + e.getMessage());
				alert.showAndWait();
			}
		}
	}

	public void setClienteDetails(Cliente selectedCli) {
		clienteFactura = selectedCli;
		lbl_Cliente.setText("Cliente: " + clienteFactura.getNombre());

	}

	public void borrarFactura(ActionEvent event) throws IOException {
		session = sf.openSession();
		session.beginTransaction();
		TicketProductos selectedFactura = tableView.getSelectionModel().getSelectedItem();
		if (selectedFactura != null) {
			tableView.getItems().remove(selectedFactura);
			if (selectedFactura.getTicket() != null) {
				selectedFactura.setFactura(null);
				session.merge(selectedFactura);
			} else {
				session.remove(selectedFactura);
			}

			session.getTransaction().commit();
			session.close();
			updateTotalFacturaPrice();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Registro Eliminado");
			alert.setHeaderText(null);
			alert.setContentText("El registro ha sido eliminado.");
			alert.showAndWait();
			txt_codBarras.clear();
			txt_desArticulo.clear();
			txt_precio.clear();
			txt_cantidad.clear();
			txt_descuento.clear();
			txt_precioDes.clear();
			txt_TotalFactura.clear();
			txt_NumFactura.clear();
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Factura No Seleccionada");
			alert.setHeaderText(null);
			alert.setContentText("Por favor, seleccione una factura para eliminar.");
			alert.showAndWait();
		}
	}
}