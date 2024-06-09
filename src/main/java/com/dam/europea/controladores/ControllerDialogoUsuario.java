package com.dam.europea.controladores;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Usuario;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerDialogoUsuario implements Initializable {

	private SessionFactory sf;
	private String IDUsuario;
	@FXML
	private TextField txtIDUsuario;
	@FXML
	private TextField txtNombreUsuario;
	@FXML
	private TextField txtContrasena;
	private Session session;
	@FXML
	private Button btnAceptar;
	@FXML
	private Button btnCancelar;
	@FXML
	private ComboBox<String> comboBoxRol;
	// Clases para rellenar
	private Usuario u;
	private ControllerGI_Users ct2;

	public ControllerDialogoUsuario(SessionFactory sf, String iDUsuario, ControllerGI_Users ct2) {
		this.sf = sf;
		this.IDUsuario = iDUsuario;
		this.ct2 = ct2;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxRol.getItems().addAll("Administrador", "Supervisor", "Staff");
		session = sf.openSession();
		if (IDUsuario != null) {
			u = session.find(Usuario.class, IDUsuario);
			if (u != null) {
				txtIDUsuario.setText(u.getIdUsuario());
				txtNombreUsuario.setText(u.getUserName());
				txtContrasena.setText(u.getPass());

				comboBoxRol.setValue(u.getRol());
				;

			}
		}

		btnAceptar.setOnAction(event -> {
			if (areFieldsValid()) {
				if (IDUsuario == null) {
					try {
						crearUsuario();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					try {
						modUsuario(u);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				closeWindow();
			} else {
				showWarning();
			}
		});

		btnCancelar.setOnAction(event -> closeWindow());
	}

	private boolean areFieldsValid() {
		return !txtIDUsuario.getText().isEmpty() && !txtNombreUsuario.getText().isEmpty()
				&& !txtContrasena.getText().isEmpty();
	}

	private void showWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Campos vacíos");
		alert.setHeaderText(null);
		alert.setContentText("Por favor, complete todos los campos.");
		alert.showAndWait();
	}

	public void crearUsuario() throws NoSuchAlgorithmException {
		session = sf.openSession();
		session.beginTransaction();
		Usuario u = new Usuario();
		u.setIdUsuario(txtIDUsuario.getText());
		u.setUserName(txtNombreUsuario.getText());
		byte[] password = txtContrasena.getText().getBytes();
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password);
		byte[] passHash = md.digest();
		String mensajeHashBase64 = Base64.getEncoder().encodeToString(passHash);
		u.setPass(mensajeHashBase64);
		u.setRol(comboBoxRol.getValue());
		session.persist(u);
		session.getTransaction().commit();
		session.close();
	}

	public void modUsuario(Usuario u) throws NoSuchAlgorithmException {
		session = sf.openSession();
		session.beginTransaction();
		u.setUserName(txtNombreUsuario.getText());
		if (txtContrasena.getText().length() < 30) {
			byte[] password = txtContrasena.getText().getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password);
			byte[] passHash = md.digest();
			String mensajeHashBase64 = Base64.getEncoder().encodeToString(passHash);
			u.setPass(mensajeHashBase64);
		}
		u.setRol(comboBoxRol.getValue());

		session.merge(u);
		session.getTransaction().commit();
		session.close();
	}

	private void closeWindow() {
		ct2.cargarTabla();
		if (session != null && session.isOpen()) {
			session.close();
		}
		Stage stage = (Stage) btnAceptar.getScene().getWindow();
		stage.close();

	}

}
