package application.Controller.Playlist;

import application.Controller.Templates.ItemPlaylistListadoController;
import application.Controller.Templates.ItemVideoListadoController;
import application.DAO.Categoria.DAOCategoriaImpl;
import application.DAO.Playlist.DAOPlaylist;
import application.DAO.Playlist.DAOPlaylistImpl;
import application.DAO.Video.DAOVideoImpl;
import application.Listeners.PlaylistListadoListener;
import application.Listeners.VideoListadoListeners;
import application.Model.Playlist;
import application.Model.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static application.Controller.Video.VideoPlayerController.lastScenne;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado al listado de playlists
 */
public class ListarController implements Initializable {

    DAOPlaylistImpl DAOPlaylist = new DAOPlaylistImpl(); //Instancia del DAO plylist

    private List<Playlist> listaPlaylist;
    public static Playlist currentPlaylist = null;

    //Atributos usados del controller
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> sltFilter;

    private PlaylistListadoListener listener;

    /**
     * Constructor del controller, utilizado para inicializar la lista que contendra las instancias de playlists registradas
     *
     */
    public ListarController() {
        try {
            listaPlaylist = DAOPlaylist.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo para abrir el playlist seleccionado por el usuario lanzando la vista de listar videos
     * @param playlist instancia de playlist que se desea abrir
     */
    public void play(Playlist playlist) throws IOException {
        currentPlaylist = playlist;
        lastScenne = "Playlist/Listar.fxml";

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Listar.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();
    }

    /**
     * Metodo para editar el playlist seleccionado por el usuario lanzando la vista de editar playlist
     * @param playlist instancia de playlist que se desea editar
     */
    public void edit(Playlist playlist) throws IOException {
        System.out.println("Edit "+ playlist.getNombre());
        currentPlaylist = playlist;

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Playlist/Actualizar.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();

    }

    /**
     * Metodo para borrar el playlist seleccionado por el usuario
     * @param playlist instancia de playlist que se desea eliminar
     */
    public void delete(Playlist playlist) throws SQLException {
        System.out.println("Delete ID Playlist: " + playlist.getId());

        for (int i = 0; i<listaPlaylist.size(); i++) {
            if (listaPlaylist.get(i).getId() == playlist.getId()) {
                listaPlaylist.remove(i);
                DAOPlaylist.delete(playlist);
            }
        }
    }

    /**
     * Metodo utilizado para listar instancias de playlist de manera dinamica utilizando la vista de templates referente al playlist
     *
     * @param listaPlaylist Lista de instancias de playlist utilizado para el listado
     * */
    public void showItems(List<Playlist> listaPlaylist) {
        System.out.println(listaPlaylist.size());
        //Define las columnas y filas iniciales para el listado
        int columnas = 0;
        int filas = 1;

        try {

            //Para cada video
            for (int i = 0; i < listaPlaylist.size(); i++) {

                //Obtenemos la vista de Item de video y generamos un AnchorPane para ser agregado al Grid
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/application/View/Templates/ItemPlaylistListado.fxml"));
                AnchorPane itemVideo = fxmlLoader.load();

                //Uso del controller del item de video para cargar la informacion dinamicamente de cada video en la lista
                ItemPlaylistListadoController itemController = fxmlLoader.getController();
                itemController.setData(listaPlaylist.get(i), listener);

                //Por cada 3 columnas reinicia a 0 para agregar items abajo
                if (columnas == 3) {
                    columnas = 0;
                    filas++;
                }

                //Agrega el AnchorPane con la informacion de la camiseta al grid, en la columna y fila correspondiente
                grid.add(itemVideo, columnas++, filas);

                //Define el tamaño del grid dinamicamente segun se agregan nuevos items
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                //Añade 10px de margin por cada video
                GridPane.setMargin(itemVideo, new Insets(10));
            }
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listener = new PlaylistListadoListener() {
            @Override
            public void onClickPlay(Playlist playlist) throws IOException {
                System.out.println("Abrir: " + playlist.getNombre());
                play(playlist);
            }

            @Override
            public void onClickEdit(Playlist playlist) throws IOException, SQLException {
                edit(playlist);
            }

            @Override
            public void onClickDelete(Playlist playlist) throws SQLException {
                delete(playlist);
                limpiarPantalla();
                showItems(listaPlaylist);
            }
        };
        showItems(listaPlaylist);
    }

    /**
    * Metodo utilizado para limpiar el grid donde se realiza el listado dinamico
    *
    * */
    public void limpiarPantalla () {
        grid.getChildren().clear();
    }

    /**
     * Metodo para regresar al home
     *
     */
    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Dashboard/Dashboard.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }

}
