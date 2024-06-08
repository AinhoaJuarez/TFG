package com.dam.europea.controladores;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Producto;

import jakarta.persistence.TypedQuery;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControladorSelProducto implements Initializable{
	private SessionFactory sf;
	private Session session;
    @FXML
    private TableView<Producto> tablaProds;
    @FXML
	private TableColumn<Producto, String> colCod;
    @FXML
	private TextField txtDes;
	@FXML
	private TextField txtCod;
	@FXML
	private TableColumn<Producto, String> colDes;
	@FXML
	private TableColumn<Producto, Double> colPrecio;

    private Producto productoSeleccionado;
    private ControllerTicket parentController;
    private ControllerFactura parentController2;
    
    public ControladorSelProducto(SessionFactory sf) {
		this.sf = sf;
	}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	
    	cargarTabla();
    	txtCod.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		txtDes.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
		tablaProds.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	            if (mouseEvent.getClickCount() == 2 && tablaProds.getSelectionModel().getSelectedItem() != null) {
	                Producto selectedProduct = tablaProds.getSelectionModel().getSelectedItem();
	                if(parentController!=null) {
		                parentController.setProductDetails(selectedProduct);
	                }else {
	                	parentController2.setProductDetails(selectedProduct);
	                }
	                
	                ((Stage) tablaProds.getScene().getWindow()).close();
	            }
	        }
	    });
    }
    
    private void searchProductos() {
		String cod = txtCod.getText().trim();
		String descripcion = txtDes.getText().trim();
		StringBuilder hql = new StringBuilder("SELECT p FROM Producto p WHERE 1=1");

		if (!cod.isEmpty()) {
			hql.append(" AND LOWER(p.codigoBarras) LIKE :cod");
		}
		if (!descripcion.isEmpty()) {
			hql.append(" AND LOWER(p.descripcion) LIKE :descripcion");
		}

		Session session = sf.openSession();
		TypedQuery<Producto> query = session.createQuery(hql.toString(), Producto.class);

		if (!cod.isEmpty()) {
			query.setParameter("cod", "%" + cod + "%");
		}
		
		if (!descripcion.isEmpty()) {
			query.setParameter("descripcion", "%" + descripcion + "%");
		}
		System.out.println(query);
		List<Producto> results = query.getResultList();

		tablaProds.getItems().clear();

		tablaProds.getItems().addAll(results);

		session.close();
	}
    
    public void cargarTabla() {
    	session = sf.openSession();
    	tablaProds.getItems().clear();
    	colCod.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
    	colDes.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    	colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
		Session session = sf.openSession();
		TypedQuery<Producto> query = session.createQuery("SELECT e FROM Producto e", Producto.class);
		ArrayList<Producto> entityData = (ArrayList<Producto>) query.getResultList();
		if (entityData != null) {
			tablaProds.getItems().addAll(entityData);
		}
		session.close();
	}
    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }
    public void setParentController(ControllerTicket parentController, ControllerFactura parentController2) {
    	if(parentController!=null){
    		this.parentController = parentController;
    	}else {
    		this.parentController2 = parentController2;
    	}
        
    }
}
