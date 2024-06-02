package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TicketDialogController implements Initializable {
	@FXML
	private Label lblImporteFinal;

	@FXML
	private TextField txt_Descuento;

	@FXML
	private Label lblImporteTotal;

	private double totalTicket;
	private double discount;
	private boolean accepted;
	@FXML
	Button btnCancelar;
	@FXML
	Button btnAceptar;

	public void setTotalTicket(double totalTicket) {
		this.totalTicket = totalTicket;
		lblImporteTotal.setText(String.format("%.2f", totalTicket));
		lblImporteFinal.setText(String.format("%.2f", totalTicket ));
		
	}

	public boolean isAccepted() {
		return accepted;
	}

	public double getDiscount() {
		return discount;
	}

	public double getFinalAmount() {
		return totalTicket - discount;
	}

	@FXML
	private void handleCancel() {
		accepted = false;
		((Stage) lblImporteFinal.getScene().getWindow()).close();
	}

	@FXML
	private void handleAccept() {
		accepted = true;
		((Stage) lblImporteFinal.getScene().getWindow()).close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txt_Descuento.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				discount = Double.parseDouble(newValue);
			} catch (NumberFormatException e) {
				discount = 0.0;
			}
			lblImporteFinal.setText(String.format("%.2f", totalTicket - (totalTicket *discount/100)));
			
		});
		btnCancelar.setOnAction(e -> handleCancel());
		btnAceptar.setOnAction(e -> handleAccept());
	}
}
