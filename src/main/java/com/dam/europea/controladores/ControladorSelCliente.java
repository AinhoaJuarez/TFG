package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.dam.europea.entidades.Cliente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class ControladorSelCliente implements Initializable{

	    @FXML
	    private TableView<Cliente> tablaClientes;

	    private Cliente clienteSeleccionado;

	    @Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {
	        tablaClientes.setOnMouseClicked(event -> {
	            clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
	            
	            System.out.println("Cliente seleccionado: " + clienteSeleccionado);
	        });
	    }

	    public Cliente getClienteSeleccionado() {
	        return clienteSeleccionado;
	    }

	}

