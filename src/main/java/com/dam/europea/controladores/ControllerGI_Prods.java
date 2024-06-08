package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;
import com.dam.europea.entidades.Ticket;

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

public class ControllerGI_Prods implements Initializable {
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
	private ComboBox<Proveedor> comboProv;
	@FXML
	private ComboBox<Integer> comboIVA;
	@FXML
	private ComboBox<FamiliaProducto> comboFam;
	@FXML
	private TextField txtDescripcion;
	@FXML
	private TextField txtCod;

	private SessionFactory sf;
	private Session session;
	@FXML
	private TableView<Producto> tableView;
	@FXML
	private TableColumn<Producto, String> codigoBarrasColumn;

	@FXML
	private TableColumn<Producto, String> descripcionColumn;

	@FXML
	private TableColumn<Producto, String> familiaColumn;

	@FXML
	private TableColumn<Producto, Integer> stockColumn;

	@FXML
	private TableColumn<Producto, String> proveedorColumn;

	@FXML
	private TableColumn<Producto, Double> pvpColumn;

	public ControllerGI_Prods(SessionFactory sf) {
		this.sf = sf;
	}
	
	//En el código, definimos un controlador JavaFX que gestiona la visualización y navegación de productos en nuestra aplicación de inventario,
	//interactuando con una base de datos a través de Hibernate.

	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarImagenes();
		cargarTabla();
		addItemsComboBoxIVA();
		addItemsComboBoxFam();
		addItemsComboBoxProv();
		tableView.setRowFactory(tv -> {
			TableRow<Producto> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
					Producto rowData = row.getItem();
					try {
						abrirDialogoCrearProd(event, rowData.getCodigoBarras());
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

		btnNew.setOnAction(arg0 -> {
			try {
				abrirDialogoCrearProd(arg0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnDel.setOnAction(arg0 -> {
			try {
				deleteSelectedTicket();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txtCod.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		txtDescripcion.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		comboProv.valueProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		comboFam.valueProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		comboIVA.valueProperty().addListener((observable, oldValue, newValue) -> searchProductos());
	}
	
	private void deleteSelectedTicket() {
		session = sf.openSession();
		session.beginTransaction();
        Producto selectedProducto = tableView.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que deseas eliminar este producto?", ButtonType.YES, ButtonType.NO);
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    session.remove(selectedProducto);
                    session.getTransaction().commit();
                    session.close();
                    tableView.getItems().remove(selectedProducto);
                    showAlert(Alert.AlertType.INFORMATION, "Producto Eliminado", "El producto ha sido eliminado.");
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Ninguna selección", "Por favor seleccione un producto para eliminar.");
        }
    }
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

	private void searchProductos() {
		session = sf.openSession();
		String cod = txtCod.getText().trim();
		String descripcion = txtDescripcion.getText().trim();
		Proveedor proveedor = comboProv.getValue();
		FamiliaProducto familia = comboFam.getValue();
		String iva = null;
		if (comboIVA.getValue() != null) {
			iva = comboIVA.getValue().toString();
		}
		StringBuilder hql = new StringBuilder("SELECT p FROM Producto p WHERE 1=1");

		if (!cod.isEmpty()) {
			hql.append(" AND LOWER(p.codigoBarras) LIKE :cod");
		}
		if (!descripcion.isEmpty()) {
			hql.append(" AND LOWER(p.descripcion) LIKE :descripcion");
		}
		if (proveedor != null) {
			hql.append(" AND p.proveedorProducto = :proveedorProducto");
		}
		if (familia != null) {
			hql.append(" AND p.familiaProducto = :familiaProducto");
		}
		if (iva != null) {
			hql.append(" AND STR(p.familiaProducto.IVA) = :iva");
		}

		TypedQuery<Producto> query = session.createQuery(hql.toString(), Producto.class);

		if (!cod.isEmpty()) {
			query.setParameter("cod", "%" + cod + "%");
		}
		
		if (!descripcion.isEmpty()) {
			query.setParameter("descripcion", "%" + descripcion + "%");
		}
		if (proveedor != null) {
			query.setParameter("proveedorProducto", proveedor);
		}
		if (familia != null) {
			query.setParameter("familiaProducto", familia);
		}
		if (iva != null) {
			query.setParameter("iva", iva);
		}
		System.out.println(query);
		List<Producto> results = query.getResultList();

		tableView.getItems().clear();

		tableView.getItems().addAll(results);

		session.close();
	}

	public void addItemsComboBoxProv() {
		session = sf.openSession();
		comboProv.getItems().clear();

		comboProv.getItems().add(null);
		Session session = sf.openSession();
		TypedQuery<Proveedor> query = session.createQuery("SELECT c FROM Proveedor c", Proveedor.class);
		List<Proveedor> clientes = query.getResultList();
		comboProv.getItems().addAll(clientes);
		session.close();
	}

	public void addItemsComboBoxFam() {
		session = sf.openSession();
		comboFam.getItems().clear();

		comboFam.getItems().add(null);
		Session session = sf.openSession();
		TypedQuery<FamiliaProducto> query = session.createQuery("SELECT c FROM FamiliaProducto c",
				FamiliaProducto.class);
		List<FamiliaProducto> clientes = query.getResultList();
		comboFam.getItems().addAll(clientes);
		session.close();
		
	}

	public void addItemsComboBoxIVA() {
		ArrayList<Integer> ivas = new ArrayList<Integer>();
		ivas.add(null);
		ivas.add(4);
		ivas.add(21);
		comboIVA.getItems().addAll(ivas);

	}

	private void abrirDialogoCrearProd(Event event, String codBarras) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoProducto.fxml"));
		ControllerDialogoProducto ct = new ControllerDialogoProducto(sf, codBarras, this);
		loader.setController(ct);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.setScene(scene);
		stage.show();

	}

	public void cargarTabla() {
		session = sf.openSession();
		tableView.getItems().clear();
		codigoBarrasColumn.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
		descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		familiaColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getProveedorProducto() != null) {
				return new javafx.beans.property.SimpleStringProperty(
						cellData.getValue().getFamiliaArticulo().getFamiliaProducto());
			} else {
				return new javafx.beans.property.SimpleStringProperty("");
			}
		});
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
		proveedorColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getProveedorProducto() != null) {
				return new javafx.beans.property.SimpleStringProperty(
						cellData.getValue().getProveedorProducto().getNombre());
			} else {
				return new javafx.beans.property.SimpleStringProperty("");
			}
		});
		pvpColumn.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
		Session session = sf.openSession();
		TypedQuery<Producto> query = session.createQuery("SELECT e FROM Producto e", Producto.class);
		ArrayList<Producto> entityData = (ArrayList<Producto>) query.getResultList();
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
