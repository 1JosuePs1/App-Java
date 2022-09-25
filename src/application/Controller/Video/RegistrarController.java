package application.Controller.Video;


import java.io.File;

import application.DAO.Categoria.DAOCategoriaImpl;
import application.DAO.Video.DAOVideoImpl;
import application.Model.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * @author Berny Fabricio Salgado Ulloa
 * @version 1.0
 * @since 23-07-2022
 *
 * Controller destinado al registro de videos
 */
public class RegistrarController implements Initializable {

    DAOVideoImpl DAOVideo = new DAOVideoImpl();
    DAOCategoriaImpl DAOCategoria = new DAOCategoriaImpl();

    ObservableList<String> listaCategoria = FXCollections.observableArrayList(DAOCategoria.getAll());

    @FXML
    private Pane root;
    @FXML
    private Label txtVideoAgregado;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ChoiceBox sltCategoria;
    @FXML
    private Label txtError;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sltCategoria.setValue("-------------");
        sltCategoria.setItems(listaCategoria);
    }

    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");
    private File video = null;

    private final FileChooser fc = new FileChooser(); // Construye el FileChooser para seleccionar archivos desde la pc.


    /**
     *Metodo utilizado para seleccionar el video a registar por el usuario haciendo uso del filechooser
     *
     * */
    public void setBtnAdjuntar(ActionEvent actionEvent) throws IOException {
        fc.setTitle("Seleccionar Video"); // Renombra el título de la ventana emergente.

        fc.getExtensionFilters().clear(); // Hace un reset a las etiquetas de archivos permitidos
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Video", "*.mp4", "*.avi")); // Escoge el tipo de archivos permitidos para video.

        video = fc.showOpenDialog(null); // Esto crea un archivo temporal del archivo seleccionado en la ventana emergente

        if (video != null) {
            System.out.println(video.getAbsolutePath()); //Imprime el path del archivo
            txtVideoAgregado.setText("Video seleccionado");
            txtVideoAgregado.setStyle(validateMessage);

        } else {
            System.out.println("Video no seleccionado");
        }
    }


    /**
     * Metodo utilizado para guardar la foto de perfil del usuario
     * recibe como parametros el file que contiene la imagen y el username de la persona para realizar una copia del archivo imagen original
     * @param video de tipo file, archivo que contiene el video
     * @param nombre de tipo string, nombre del video para guardar asi la imagen
     * @return String de la ruta donde se guardo la imagen
     * */
    public String saveVideo(File video, String nombre) throws IOException{

        String mediaDirectory = Paths.get("")
                .toAbsolutePath()
                .toString();


        String extension = video.getName().substring(video.getName().indexOf(".")); //Extrae la extesion del archivo
        mediaDirectory += "\\src\\resources\\media\\videos\\" + nombre + extension; //Crea la ruta para guardar el archivo

        System.out.println(mediaDirectory);

        Path from = Paths.get(video.toURI()); //Obtiene el path de la imagen recibida
        Path to = Paths.get(mediaDirectory); //Obtiene el path de la ruta final
        CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING, //Reemplaza el archivo si ya existe
                StandardCopyOption.COPY_ATTRIBUTES   //Obtiene los atributos originales para el archivo copy
        };
        Files.copy(from, to, options); //Realiza la copia del archivo

        return mediaDirectory;


    }

    /**
     *Funcion utilizada para validar el formulario de registro, de todo estar correcto llama a la funcion para registrar video
     * */
    public void validationForm(ActionEvent actionEvent) throws SQLException, IOException, NoSuchFileException {

        if (txtTitulo.getText().isBlank() ||
            txtDescripcion.getText().isBlank() ||
            video == null ||
            sltCategoria.getValue() == "-------------"
        ) {
            System.out.println(video);
            System.out.println("Campos vacíos");
            txtError.setText("Por favor rellenar todos los campos");
            txtError.setStyle(errorMessage);
        } else {
            registerVideo();
        }
    }

    /**
     * Metodo utilizado para limpiar el formulario de registro despues de un registro
     * */
    public void limpiarFormulario() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtError.setText("Video registrado exitosamente");
        txtError.setStyle(validateMessage);
    }

    /**
     * Metodo utilizado para obtener fecha y hora actual al momento de ejecutarla
     * */
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Metodo utilizado para realizar el registro del video con la informacion del formulario
     * */
    public void registerVideo() throws IOException, SQLException {
        String nombre = txtTitulo.getText();
        String descripcion = txtDescripcion.getText();
        double duracion = 2.55;
        String ruta = saveVideo(video, nombre);
        String fechaCreacion = getCurrentDate();
        String nombreCategoria = (String) sltCategoria.getValue();

        Video video = new Video(nombre, descripcion, duracion, ruta, fechaCreacion, nombreCategoria);

        DAOVideo.store(video);
        limpiarFormulario();
    }

    /**
     * Metodo utilizado para regresar al home
     * */
    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Dashboard/Dashboard.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }
}



