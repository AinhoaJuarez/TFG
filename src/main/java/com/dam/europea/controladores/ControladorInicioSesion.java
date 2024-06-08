package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.Usuario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// Este es el controlador para la pantalla de inicio de sesión
public class ControladorInicioSesion implements Initializable {
	@FXML
	private ImageView imgView; // Vista para mostrar una imagen
	private SessionFactory sf; // Fábrica de sesiones de Hibernate
	@FXML
	private TextField txtUser;
	@FXML
	private TextField txtPass;
	@FXML
	private Button btnEntrar;
	private Session session;

	// Inicializamos el controlador cargando una imagen
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InputStream inputStream = getClass().getResourceAsStream("/iconoGrande.jpeg");
		Image imagen = new Image(inputStream);
		imgView.setImage(imagen);
		btnEntrar.setOnAction(arg0 -> {
			try {
				switchToMenu(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// Cambiamos a la escena del menú principal
	public void switchToMenu(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		session = sf.openSession();
		String username = txtUser.getText();
		String password = txtPass.getText();

		// Check if the user exists and the password is correct
		Usuario user = checkUserCredentials(username, password);

		if (user != null) {
			byte[] passwordCheck = txtPass.getText().getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(passwordCheck);
			byte[] passwordCheckHash = md.digest();
			String passCheckHashBase64 = Base64.getEncoder().encodeToString(passwordCheckHash);
			if(user.getPass().equals(passCheckHashBase64)) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/erae.fxml"));
				Controller controller = new Controller(sf);
				loader.setController(controller);
				Parent root = loader.load();
				Scene scene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			}else {
				showAlert("Inicio de Sesión Fallido", "La contraseña no es correcta.");
			}
			
		} else {
			// Show an alert if the user does not exist or the password is incorrect
			showAlert("Inicio de Sesión Fallido", "El usuario no existe.");
		}
		session.close();
	}

	private Usuario checkUserCredentials(String username, String password) {
		Query<Usuario> query = session.createQuery("FROM Usuario WHERE userName = :userName", Usuario.class);
		query.setParameter("userName", username);
		Usuario user = query.uniqueResult();

		if (user != null ) {
			return user;
		} else {
			return null;
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Constructor que recibe una fábrica de sesiones de Hibernate
	public ControladorInicioSesion(SessionFactory sessionFactory) {
		this.sf = sessionFactory;
	}
}
