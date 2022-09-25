package application.Controller.Video;

import application.DAO.Categoria.DAOCategoriaImpl;
import application.DAO.Video.DAOVideoImpl;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static application.Controller.Video.ListarController.currentVideo;

/**
 * @author Berny Fabricio Salgado Ulloa
 * @version 1.0
 * @since 23-07-2022
 *
 * Controller destinado a la actualizacion de videos
 */
public class ActualizarController implements Initializable {

    DAOVideoImpl DAOVideo = new DAOVideoImpl();
    DAOCategoriaImpl DAOCategoria = new DAOCategoriaImpl();
    ObservableList<String> listaCategoria = FXCollections.observableArrayList(DAOCategoria.getAll());

    @FXML
    private Pane root;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private ChoiceBox sltCategoria;
    @FXML
    private Label message;
    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sltCategoria.setItems(listaCategoria);
        sltCategoria.setValue(currentVideo.getCategoria());
        txtTitulo.setText(currentVideo.getNombre());
        txtDescripcion.setText(currentVideo.getDescripcion());
    }

    /**
     *Funcion utilizada para validar el formulario de registro, de todo estar correcto llama a la funcion para registrar video
     * */
    public void validationForm(ActionEvent actionEvent) throws SQLException, IOException, NoSuchFileException {
        if (txtTitulo.getText().isBlank() ||
            txtDescripcion.getText().isBlank() ||
            sltCategoria.getValue() == "-------------"
        ) {
            System.out.println("Campos vacíos");
            message.setText("Por favor rellenar todos los campos");
            message.setStyle(errorMessage);
        } else {
            updateVideo();
        }
    }

    /**
     * Metodo utilizado para realizar la actualizacion del video con la informacion del formulario
     * */
    public void updateVideo() throws IOException, SQLException {
        String nombre = txtTitulo.getText();
        String descripcion = txtDescripcion.getText();
        String nombreCategoria = (String) sltCategoria.getValue();

        currentVideo.setNombre(nombre);
        currentVideo.setDescripcion(descripcion);
        currentVideo.setCategoria(nombreCategoria);

        DAOVideo.update(currentVideo);
        message.setText("Video Actualizado con exito");
        message.setStyle(validateMessage);
    }

    /**
     * Metodo utilizado para volver a los videos listados
     * */
    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Listar.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }

}
