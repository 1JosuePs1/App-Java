package application.Controller.Video;

import application.Controller.Templates.ItemVideoListadoController;
import application.DAO.Categoria.DAOCategoriaImpl;
import application.DAO.Playlist.DAOPlaylistImpl;
import application.DAO.Video.DAOVideoImpl;
import application.Listeners.VideoListadoListeners;
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

import static application.Controller.Playlist.ListarController.currentPlaylist;
import static application.Controller.Socket.ServerController.sendData;
import static application.Controller.Socket.ServerController.serverOn;
import static application.Controller.Video.VideoPlayerController.lastScenne;


/**
 * @author Berny Fabricio Salgado Ulloa
 * @version 1.0
 * @since 23-07-2022
 *
 * Controller destinado al listado de videos
 */
public class ListarController implements Initializable {

    DAOVideoImpl DAOVideo = new DAOVideoImpl(); //Instancia del DAO video
    DAOPlaylistImpl DAOPlaylist = new DAOPlaylistImpl(); //Instancia del DAO playlist

    DAOCategoriaImpl DAOCategoria = new DAOCategoriaImpl(); //Instancia del DAO categorias
    ObservableList<String> listaCategorias = FXCollections.observableArrayList(DAOCategoria.getAll());

    private List<Video> listaVideos;
    public static Video currentVideo;

    //Atributos usados del controller
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> sltFilter;

    private VideoListadoListeners listener;

    public ListarController() {
        try {
            if (currentPlaylist != null) {
                listaVideos = DAOPlaylist.getAllVideoPlaylist(currentPlaylist.getId());
                return;
            }
            listaVideos = DAOVideo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo de play del video listado, asigna al currentVideo el video recibido por parametro y lanza el videoplayer
     * @param video Instancia de video, referencia al video seleccionado que desea abrir el usuario
     */
    public void play(Video video) throws IOException {
        System.out.println("Playing "+ video.getNombre());
        currentVideo = video;
        lastScenne = "Video/Listar.fxml";

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/VideoPlayer.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();
    }

    /**
     * Metodo de editar del video listado, asigna al currentVideo el video recibido por parametro
     * @param video Instancia de video, referencia al video seleccionado que desea editar el usuario
     */
    public void edit(Video video) throws IOException {
        System.out.println("Edit "+ video.getNombre());
        currentVideo = video;

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Actualizar.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();

    }

    /**
     * Metodo de play del video listado, asigna al currentVideo el video recibido por parametro
     * @param video Instancia de video, referencia al video seleccionado que desea borrar el usuario
     */
    public void delete(Video video) throws SQLException {
        System.out.println("Delete ID VIDEO: " + video.getId());

        for (int i = 0; i<listaVideos.size(); i++) {
            if (listaVideos.get(i).getId() == video.getId()) {
                listaVideos.remove(i);
                DAOVideo.delete(video);
            }
        }
    }

    /**
     * Metodo utilizado para listar instancias de videos de manera dinamica utilizando la vista de templates referente al video
     *
     * @param listaVideos Lista de instancia de videos utilizado para el listado
     * */
    public void showItems(List<Video> listaVideos) {
        System.out.println(listaVideos.size());
        //Define las columnas y filas iniciales para el listado
        int columnas = 0;
        int filas = 1;

        try {

            //Para cada video
            for (int i = 0; i < listaVideos.size(); i++) {

                //Obtenemos la vista de Item de video y generamos un AnchorPane para ser agregado al Grid
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/application/View/Templates/ItemVideoListado.fxml"));
                AnchorPane itemVideo = fxmlLoader.load();

                //Uso del controller del item de video para cargar la informacion dinamicamente de cada video en la lista
                ItemVideoListadoController itemController = fxmlLoader.getController();
                itemController.setData(listaVideos.get(i), listener);

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
        /**
         * Se crea una instancia de la interfaz de listener del videos, haciendo sobreescritura de los metodos abstractos
         * @param video Instancia de video, referencia al video seleccionado que desea borrar el usuario
         */
        listener = new VideoListadoListeners() {
            @Override
            public void onClickPlay(Video video) throws IOException {
                play(video);
            }

            @Override
            public void onClickEdit(Video video) throws IOException {
                edit(video);
            }

            @Override
            public void onClickDelete(Video video) throws SQLException {
                delete(video);
                limpiarPantalla();
                showItems(listaVideos);
            }
        };

        showItems(listaVideos);
        sltFilter.setItems(listaCategorias);
    }

    /**
     * Metodo utilizado para limpiar el grid donde se realiza el listado dinamico
     *
     * */
    public void limpiarPantalla () {
        grid.getChildren().clear();
    }


    /**
     * Metodo utilizado para limpiar busqueda y filtro del usuario
     *
     * */
    public void cleanFilters() {
        sltFilter.setValue("");
        txtSearch.setText("");
        limpiarPantalla();
        showItems(listaVideos);
    }

    /**
     * Metodo utilizado para realizar busquedas por nombre en los videos listados
     * @param nombre String del nombre del video que se quiere buscar
     * @return Retorna la lista de videos con las posibles instancias encontradas
     * */
    public List<Video> searchVideos(String nombre) {
        List<Video> listaFiltrada = new ArrayList<>();;
        nombre = nombre.toLowerCase();

        for (int i = 0; i<listaVideos.size(); i++) {
            if (listaVideos.get(i).getNombre().toLowerCase().contains(nombre)) {
                listaFiltrada.add(listaVideos.get(i));
            }
        }
        return listaFiltrada;
    }

    /**
     * Metodo utilizado para filtrar la lista de videos segun el filtro recibido por parametro
     * @param filtro String del filtro
     * @return Retorna la lista de videos filtrada
     * */
    public List<Video> getFilteredList (String filtro) {
        List<Video> listaFiltrada = new ArrayList<>();;
        for (int i = 0; i<listaVideos.size(); i++) {
            if (listaVideos.get(i).getCategoria().equals(filtro)) {
                listaFiltrada.add(listaVideos.get(i));
            }
        }
        return listaFiltrada;
    }


    public void search() {
        String val = txtSearch.getText();
        limpiarPantalla();
        showItems(searchVideos(val));
    }

    public void filterList(ActionEvent actionEvent) {
        String filtro = sltFilter.getValue();
        limpiarPantalla();
        showItems(getFilteredList(filtro));
    }

    public void goBack() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Dashboard/Dashboard.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }

}
