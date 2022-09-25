package application.Controller.Categoria;

import application.Model.Categoria;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;

import application.DAO.Categoria.DAOCategoriaImpl;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado al registro categorias
 */
public class RegistrarController {

    /*
    * Atributos a utilizar del controller
    *
    * */
    DAOCategoriaImpl DAOCategoria = new DAOCategoriaImpl();

    @FXML
    private BorderPane root_categoria;
    @FXML
    private TextField txtCategoriaNombre;
    @FXML
    private TextArea txtCategoriaDescripcion;
    @FXML
    private Label mensajeDeAlertaCategoria;
    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");


    /*
     * Funcion para validar el formulario de registro de categoria
     * Si los campos estan llenos correctamente se registrara una categoria nueva
     *
     * */
    public void validationFormCategoria(ActionEvent actionEvent) throws SQLException, IOException {
        if (txtCategoriaNombre.getText().isBlank() ||
                txtCategoriaDescripcion.getText().isBlank())
        {
            System.out.println("Campos vacios");
            mensajeDeAlertaCategoria.setText("Todos los campos son requeridos");
            mensajeDeAlertaCategoria.setStyle(errorMessage);
        }else {
            registerCategoria();
        }
    }

    /*
     * Funcion utilizada para limpiar el formulario de registro de categoria
     *
     * */
    public void cleanForm() {
        txtCategoriaNombre.setText("");
        txtCategoriaDescripcion.setText("");

        mensajeDeAlertaCategoria.setText("La categoria se ha agregado con exito");
        mensajeDeAlertaCategoria.setStyle(validateMessage);
    }

    /*
     * Funcion utilizada para realizar el regsitro de categoria segun los datos optenidos del formulario
     *
     * */
    public void registerCategoria() throws IOException, SQLException {
        String nombre = txtCategoriaNombre.getText();
        String descripcion  = txtCategoriaDescripcion.getText();

        Categoria categoria = new Categoria( nombre, descripcion);
        DAOCategoria.store(categoria);

        cleanForm();
    }

    /*
     * Funcion utilizada para regresar a la vista al dashboard principal
     *
     * */
    public void goBack() throws IOException {
        Parent root_categoria = FXMLLoader.load(this.getClass().getResource("/application/View/Dashboard/Dashboard.fxml"));

        Scene subScene = new Scene(root_categoria);
        Stage primaryStage = (Stage) this.root_categoria.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }
}
