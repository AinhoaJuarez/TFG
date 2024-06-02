package com.dam.europea.controladores;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;
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

public class ControladorSelCliente implements Initializable{
	private SessionFactory sf;
	private Session session;
    @FXML
    private TableView<Cliente> tablaCli;
    @FXML
	private TableColumn<Cliente, String> colDNI;
    @FXML
	private TextField txtNombre;
	@FXML
	private TextField txtDNI;
	@FXML
	private TableColumn<Cliente, String> colNombre;

    private Producto productoSeleccionado;
    private ControllerTicket parentController;
    private ControllerFactura parentController2;
    
    public ControladorSelCliente(SessionFactory sf) {
		this.sf = sf;
	}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	session = sf.openSession();
    	cargarTabla();
    	txtDNI.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
    	txtNombre.textProperty().addListener((observable, oldValue, newValue) -> searchProductos());
    	tablaCli.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	            if (mouseEvent.getClickCount() == 2 && tablaCli.getSelectionModel().getSelectedItem() != null) {
	            	Cliente selectedCli = tablaCli.getSelectionModel().getSelectedItem();
	                if(parentController!=null) {
		                parentController.setClienteDetails(selectedCli);
	                }else {
	                	parentController2.setClienteDetails(selectedCli);
	                }
	                ((Stage) tablaCli.getScene().getWindow()).close();
	            }
	        }
	    });
    }
    
    private void searchProductos() {
		String DNI = txtDNI.getText().trim();
		String nombre = txtNombre.getText().trim();
		StringBuilder hql = new StringBuilder("SELECT c FROM Cliente c WHERE 1=1");

		if (!DNI.isEmpty()) {
			hql.append(" AND LOWER(c.dni) LIKE :dni");
		}
		if (!nombre.isEmpty()) {
			hql.append(" AND LOWER(c.nombre) LIKE :nombre");
		}

		Session session = sf.openSession();
		TypedQuery<Cliente> query = session.createQuery(hql.toString(), Cliente.class);

		if (!DNI.isEmpty()) {
			query.setParameter("dni", "%" + DNI + "%");
		}
		
		if (!nombre.isEmpty()) {
			query.setParameter("nombre", "%" + nombre + "%");
		}
		System.out.println(query);
		List<Cliente> results = query.getResultList();

		tablaCli.getItems().clear();

		tablaCli.getItems().addAll(results);

		session.close();
	}
    
    public void cargarTabla() {
    	tablaCli.getItems().clear();
		colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

		Session session = sf.openSession();
		TypedQuery<Cliente> query = session.createQuery("SELECT e FROM Cliente e", Cliente.class);
		ArrayList<Cliente> entityData = (ArrayList<Cliente>) query.getResultList();
		if (entityData != null) {
			tablaCli.getItems().addAll(entityData);
		}
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
