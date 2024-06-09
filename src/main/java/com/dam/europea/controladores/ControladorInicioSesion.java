package com.dam.europea.controladores;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.DatosEmpresa;
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
	@FXML
	private Button btnGuiaPaginaWeb;
	private Session session;

	// Inicializamos el controlador cargando una imagen
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InputStream inputStream = getClass().getResourceAsStream("/iconoGrande.jpeg");
		Image imagen = new Image(inputStream);
		imgView.setImage(imagen);
		btnEntrar.setOnAction(arg0 -> {
			try {
				checkAndCreateUsuario0();
				switchToMenu(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		InputStream archivoPregunta= getClass().getResourceAsStream("/pregunta.png");
		Image imagenPregunta= new Image(archivoPregunta, 35, 35, true, true);
		btnGuiaPaginaWeb.setGraphic(new ImageView(imagenPregunta));
		btnGuiaPaginaWeb.setOnAction(arg0 -> {
			try {
				abrirGuiaPaginaWeb(arg0);
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
		System.out.println(username + password);

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
	
	public void abrirGuiaPaginaWeb(ActionEvent event) throws IOException, URISyntaxException {
        // Recoge el directorio a la web y lo asigna
        String htmlDirectorio = "/paginaWeb/index.html";
        URI uri = ControladorInicioSesion.class.getResource(htmlDirectorio).toURI();

        // Utiliza el navegador por defecto en el equipo
        Desktop.getDesktop().browse(uri);
    }
	

	private Usuario checkUserCredentials(String username, String password) {
		Query<Usuario> query = session.createQuery("FROM Usuario WHERE userName = :userName", Usuario.class);
		query.setParameter("userName", username);
		Usuario user = query.uniqueResult();
		
		if (user != null ) {
			System.out.println(user.toString());
			return user;
		} else {
			return null;
		}
	}
	
	private void checkAndCreateUsuario0() throws NoSuchAlgorithmException {
        Session session = sf.openSession();
        session.beginTransaction();
       
        // Check if usuario with idUsuario 0 exists
        Query<Usuario> query = session.createQuery("FROM Usuario WHERE idUsuario = :idUsuario", Usuario.class);
        query.setParameter("idUsuario", "0");
        Usuario existingUsuario = query.uniqueResult();

        if (existingUsuario == null) {
            // Create new Usuario with idUsuario 0
            Usuario u = new Usuario();
            u.setIdUsuario("0");
            u.setUserName("admin");
            byte[] password = "0".getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password);
            byte[] passHash = md.digest();
            String mensajeHashBase64 = Base64.getEncoder().encodeToString(passHash);
            u.setPass(mensajeHashBase64);
            u.setRol("Administrador");
            session.persist(u);
            session.getTransaction().commit();
            
        } else {
        }

        session.close();
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
