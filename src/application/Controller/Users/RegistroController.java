package application.Controller.Users;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;

import application.Model.Usuario;
import application.DAO.Users.DAOUsuarioImpl;
import javafx.stage.Stage;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 *
 * Controller destinado a la vista de registro de usuario
 */
public class RegistroController {

    DAOUsuarioImpl DAOUsuario = new DAOUsuarioImpl();

    @FXML
    private BorderPane root_register;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ImageView imgProfile;
    @FXML
    private Label registrationMessage;
    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");

    private File picture = null;
    private final FileChooser fc = new FileChooser(); //Construye el fileChooser para la seleccion de archivos


    /**
     * Metodo utilizado para mostrar el preview de la foto de perfil seleccionada por el usuario
     * */
    public void setPicture(ActionEvent actionEvent) throws IOException {
        fc.setTitle("Seleccionar Imagen de Perfil"); //Renombra el titulo de la ventana emergente

        //Limpia las etiquetas de archivos permitidos y añade el filtro para aceptar solo imagenes
        fc.getExtensionFilters().clear();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Imagen", "*.png", "*.jpg"));

        picture = fc.showOpenDialog(null); //Crea un archivo temporal del archivo seleccionado en la ventana emergente

        if (picture != null) {
            System.out.println(picture.getAbsolutePath()); //Imprime el path del archivo
            imgProfile.setImage(new Image(picture.toURI().toString())); //Muestra el preview de la imagen seleccionada
        } else {
            System.out.println("Imagen no seleccionada");
        }
    }

    /**
     * Metodo utilizado para guardar la foto de perfil del usuario
     * recibe como parametros el file que contiene la imagen y el username de la persona para realizar una copia del archivo imagen original
     * @param picture de tipo file, archivo que contiene la imagen de perfil
     * @param username de tipo string, nombre del usuario para guardar asi la imagen
     * @return String de la ruta donde se guardo la imagen
     * */
    public String savePicture (File picture, String username) throws IOException {

        String mediaDirectory = Paths.get("") //Obtiene el path origen del proyecto
                .toAbsolutePath()
                .toString();

        if (picture != null) {
            String extension = picture.getName().substring(picture.getName().indexOf(".")); //Extrae la extesion del archivo
            mediaDirectory += "\\src\\resources\\media\\pics\\"+username+extension; //Crea la ruta para guardar el archivo

            System.out.println(mediaDirectory);

            Path from = Paths.get(picture.toURI()); //Obtiene el path de la imagen recibida
            Path to = Paths.get(mediaDirectory); //Obtiene el path de la ruta final
            CopyOption[] options = new CopyOption[]{
                    StandardCopyOption.REPLACE_EXISTING, //Reemplaza el archivo si ya existe
                    StandardCopyOption.COPY_ATTRIBUTES   //Obtiene los atributos originales para el archivo copy
            };
            Files.copy(from, to, options); //Realiza la copia del archivo

            return mediaDirectory;
        }

        return mediaDirectory + "\\src\\resources\\media\\pics\\default-user.png";
    }

    /**
     * Metodo utilizado para validar la informacion del formulario de registro
     * */
    public void validationForm(ActionEvent actionEvent) throws SQLException, IOException {
        if (txtId.getText().isBlank() ||
            txtName.getText().isBlank() ||
            txtLastName.getText().isBlank() ||
            txtUsername.getText().isBlank() ||
            txtPassword.getText().isBlank())
        {
            System.out.println("Campos vacios");
            registrationMessage.setText("Todos los campos son requeridos");
            registrationMessage.setStyle(errorMessage);
        } else if (  !Usuario.validatePassword(txtPassword.getText())  ) {
            registrationMessage.setText("La contraseña deberá tener un mínimo de 6 caracteres, y un máximo de 8,\n con al menos una letra mayúscula, una letra minúscula y un carácter especial (@#$!¡%^&+=).");
            registrationMessage.setStyle(errorMessage);
        } else {
            registerUser();
        }
    }

    /**
     * Metodo utilizado para limpiar el formulario de registro despues de un registro
     * */
    public void cleanForm() {
        txtId.setText("");
        txtName.setText("");
        txtLastName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");

        registrationMessage.setText("El usuario ha sido registrado con exito");
        registrationMessage.setStyle(validateMessage);
    }

    /**
     * Metodo utilizado para realizar el registro del usuario con la informacion del formulario
     * */
    public void registerUser() throws IOException, SQLException {

        String id = txtId.getText();
        String name = txtName.getText();
        String lastname = txtLastName.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String photo = savePicture(picture, username);

        Usuario user = new Usuario(id, name, lastname, photo, username, password);

        if (DAOUsuario.store(user)) {
            cleanForm();
        } else {
            registrationMessage.setText("Ocurrieron errores en el registro");
            registrationMessage.setStyle(validateMessage);
        }
    }

    /**
     * Metodo utilizado para regresar al login
     * */
    public void login() throws IOException {
        Parent root_register = FXMLLoader.load(this.getClass().getResource("/application/View/Users/Login.fxml"));

        Scene subScene = new Scene(root_register);
        Stage primaryStage = (Stage) this.root_register.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }

}
