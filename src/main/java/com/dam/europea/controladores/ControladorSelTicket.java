package com.dam.europea.controladores;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;

import com.dam.europea.entidades.Factura;
import com.dam.europea.entidades.Ticket;

import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControladorSelTicket implements Initializable {
	@FXML
	private TextField txtNum;
	@FXML
	private TextField txtTotal;
	@FXML
	private DatePicker datePicker;
	@FXML
	private TableView<Ticket> tableViewTickets;

	@FXML
	private TableColumn<Ticket, Integer> colNumTicket;

	private Ticket ticketSeleccionado;

	@FXML
	private TableColumn<Ticket, Date> colFecha;

	@FXML
	private TableColumn<Ticket, String> colCliente;

	private SessionFactory sf;
	private Session session;
	private ControllerFactura controllerFactura;
	private Factura factura;

	public ControladorSelTicket(SessionFactory sf, ControllerFactura controllerFactura, Factura factura) {
		this.sf = sf;
		this.controllerFactura = controllerFactura;
		this.factura = factura;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		session = sf.openSession();

		colNumTicket.setCellValueFactory(new PropertyValueFactory<>("numTicket"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
		colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		TypedQuery<Ticket> query = session.createQuery("SELECT e FROM Ticket e", Ticket.class);
		ArrayList<Ticket> entityData = (ArrayList<Ticket>) query.getResultList();
		if (entityData != null) {
			tableViewTickets.getItems().addAll(entityData);
		}
		session.close();

		tableViewTickets.setRowFactory(tv -> {
			TableRow<Ticket> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Ticket rowData = row.getItem();
					associateProducts(rowData);
				}
			});
			return row;
		});
		txtNum.textProperty().addListener((observable, oldValue, newValue) -> filterTickets());
		txtTotal.textProperty().addListener((observable, oldValue, newValue) -> filterTickets());
		datePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterTickets());
	}

	private void filterTickets() {
	    session = sf.openSession();  // Open session here
	    try {
	        String num = txtNum.getText().trim();
	        String total = txtTotal.getText().trim();
	        LocalDate fecha = datePicker.getValue();

	        StringBuilder hql = new StringBuilder("SELECT tp FROM Ticket tp WHERE 1=1");

	        if (!num.isEmpty()) {
	            hql.append(" AND tp.numTicket = :num");
	        }
	        if (!total.isEmpty()) {
	            hql.append(" AND tp.total = :total");
	        }
	        if (fecha != null) {
	            hql.append(" AND tp.fecha = :fecha");
	        }

	        TypedQuery<Ticket> query = session.createQuery(hql.toString(), Ticket.class);

	        if (!num.isEmpty()) {
	            query.setParameter("num", Integer.parseInt(num));
	        }
	        if (!total.isEmpty()) {
	            query.setParameter("total", Double.parseDouble(total));
	        }
	        if (fecha != null) {
	            query.setParameter("fecha", fecha);
	        }

	        List<Ticket> results = query.getResultList();

	        tableViewTickets.getItems().clear();
	        tableViewTickets.getItems().addAll(results);
	    } catch (Exception e) {
	        System.out.println(e);
	        showAlert("Error", "An error occurred while filtering tickets: " + e.getMessage());
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	}

	private void associateProducts(Ticket selTicket) {
	    session = sf.openSession();
	    try {
	        session.beginTransaction();


	        // Create the update query using createMutationQuery
	        MutationQuery updateQuery = session.createMutationQuery(
	                "UPDATE TicketProductos tp SET tp.numeroFactura = :numeroFactura WHERE tp.numTicket = :numTicket");
	        updateQuery.setParameter("numeroFactura", factura);
	        updateQuery.setParameter("numTicket", selTicket);

	        int updatedRows = updateQuery.executeUpdate();

	        if (updatedRows > 0) {
	            session.getTransaction().commit();
	            controllerFactura.cargarTabla();
	            controllerFactura.updateTotalFacturaPrice();
	            Stage stage = (Stage) tableViewTickets.getScene().getWindow();
	            stage.close();
	        } else {
	            session.getTransaction().rollback();
	            showAlert("Error", "Failed to associate products with factura.");
	        }
	    } catch (Exception e) {
	        session.getTransaction().rollback();
	        System.out.println(e);
	        showAlert("Error", "An error occurred: " + e.getMessage());
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

	// Método para obtener el ticket seleccionado
	public Ticket getTicketSeleccionado() {
		return ticketSeleccionado;
	}
}
