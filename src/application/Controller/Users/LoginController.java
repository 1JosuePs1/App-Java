package application.Controller.Users;

import application.DAO.Users.DAOUsuarioImpl;

import application.Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 *
 * Controller destinado a la vista de login
 */
public class LoginController {

    DAOUsuarioImpl DAOUsuario = new DAOUsuarioImpl();
    static public Usuario currentUser = new Usuario();

    @FXML
    private BorderPane root;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label loginMessage;


    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");


    /**
     * Metodo utilizado para validar la informacion del formulario de login
     * */
    public void validateData(ActionEvent actionEvent) throws SQLException, IOException {
        if (txtUsername.getText().isBlank() || txtPassword.getText().isBlank()) {
            System.out.println("Campos vacios");
            loginMessage.setText("Ingresa tu nombre de usuario y contraseña para continuar");
            loginMessage.setStyle(errorMessage);
        } else {
            userLogin();
        }
    }

    /**
     * Metodo utilizado para realizar el proceso de login con el password y user dado por el usuario
     * */
    public void userLogin() throws SQLException, IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        currentUser = DAOUsuario.auth(username, password);

        if ( currentUser.getUsername() != null || currentUser == null ) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Dashboard/Dashboard.fxml"));

            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.root.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

        } else {
            System.out.println("Login Invalido");
            loginMessage.setText("Datos incorrectos, intenta de nuevo");
            loginMessage.setStyle(errorMessage);
        }
    }

    /**
     * Metodo utilizado para lanzar la vista de registro de usuario
     * */
    public void signin() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Users/Registro.fxml"));

        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }
}
