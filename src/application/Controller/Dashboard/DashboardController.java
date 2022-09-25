package application.Controller.Dashboard;

import application.Controller.Templates.ItemPlaylistDashboardController;
import application.Controller.Templates.ItemVideoDashboardController;
import application.DAO.Playlist.DAOPlaylistImpl;
import application.DAO.Video.DAOVideoImpl;
import application.Listeners.PlaylistDashboardListener;
import application.Listeners.VideoDashboardListeners;
import application.Model.Playlist;
import application.Model.Video;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import static application.Controller.Playlist.ListarController.currentPlaylist;
import static application.Controller.Users.LoginController.currentUser;
import static application.Controller.Video.ListarController.currentVideo;
import static application.Controller.Video.VideoPlayerController.lastScenne;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado a la vista del dashboard principal
 */
public class DashboardController implements Initializable {

    /*
     * Atributos a utilizar del controller
     *
     * */
    DAOVideoImpl DAOVideo = new DAOVideoImpl(); //Instancia del DAO video
    DAOPlaylistImpl DAOPlaylist = new DAOPlaylistImpl(); //Instancia del DAO video

    @FXML
    private GridPane gridLastPlayedVideos;
    @FXML
    private GridPane gridLastPlayedPlaylist;
    @FXML
    private Text wellcomeMessage;
    @FXML
    private AnchorPane root;

    private List<Video> listaVideosRecientes;
    private List<Playlist> listaPlaylistRecientes;

    private VideoDashboardListeners listenerVideo;
    private PlaylistDashboardListener listenerPlaylist;

    /**
     * Metodo de play del video listado, asigna al currentVideo el video recibido por parametro y lanza el videoplayer
     * @param video Instancia de video, referencia al video seleccionado que desea abrir el usuario
     */
    public void playVideo(Video video) throws IOException {
        System.out.println("Playing "+ video.getNombre());
        currentVideo = video;
        lastScenne = "Dashboard/Dashboard.fxml";

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/VideoPlayer.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();
    }

    /**
     * Metodo para abrir un playlist, asigna a la variable de currentplaylist el playlist seleccionado por el usuario
     * @param playlist Instancia de playlist, referencia al playlist seleccionado que desea abrir el usuario
     */
    public void setPlaylist(Playlist playlist) throws IOException {
        currentPlaylist = playlist;

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Listar.fxml"));
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(new Scene(root,989,780 ));
        primaryStage.centerOnScreen();
    }

    /**
     * Constructor del controller, utilizado para inicializar las listas que contendran las instancias de videos y playlists
     *
     */
    public DashboardController() {
        try {
            listaVideosRecientes = DAOVideo.getRecents();
            listaPlaylistRecientes = DAOPlaylist.getRecents();

            /**
             * Se crea una instancia de la interfaz de listener del videos, haciendo sobreescritura de los metodos abstractos
             * @param video Instancia de video, referencia al video seleccionado que desea play el usuario
             */
            listenerVideo = new VideoDashboardListeners() {
                @Override
                public void onClickPlay(Video video) throws IOException {
                    playVideo(video);
                }
            };
            listenerPlaylist = new PlaylistDashboardListener() {
                @Override
                public void onClickPlay(Playlist playlist) throws IOException {
                    setPlaylist(playlist);
                }
            };


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wellcomeMessage.setText( "Bienvenid@! " + currentUser.getNombre());
        showItemsVideos(listaVideosRecientes, gridLastPlayedVideos);
        showItemsPlaylist(listaPlaylistRecientes, gridLastPlayedPlaylist);
    }

    /**
     * Metodo utilizado como menu del home, para obtener la opcion que desea el usario y asi mostrar la vista correspondiente
     *
     * @param event De tipo MouseEvent, hace referencia a la interaccion de click del usuario
     * */
    public void navigate(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            Parent root = null;
            switch (icon.getId()) {
                case "btnRegisterCategory" -> root = FXMLLoader.load(this.getClass().getResource("/application/View/Categoria/Registrar.fxml"));
                case "btnRegisterVideo" -> root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Registrar.fxml"));
                case "btnRegisterPlaylist" -> root = FXMLLoader.load(this.getClass().getResource("/application/View/Playlist/Registro.fxml"));
            }
            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();
            }
        }
    }

    /**
     * Metodo utilizado para listar instancias de videos de manera dinamica utilizando la vista de templates referente al video
     *
     * @param listaVideos Lista de instancia de videos utilizado para el listado
     * @param grid Gridpane donde se agregara el listado dinamico
     * */
    public void showItemsVideos(List<Video> listaVideos, GridPane grid) {
        System.out.println(listaVideos.size());
        //Define las columnas y filas iniciales para el listado
        int columnas = 0;
        int filas = 0;

        try {
            //Para cada video
            for (int i = 0; i < listaVideos.size(); i++) {

                //Obtenemos la vista de Item de video y generamos un AnchorPane para ser agregado al Grid
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/application/View/Templates/ItemVideoDashboard.fxml"));
                AnchorPane itemVideo = fxmlLoader.load();

                //Uso del controller del item de video para cargar la informacion dinamicamente de cada video en la lista
                ItemVideoDashboardController itemController = fxmlLoader.getController();
                itemController.setData(listaVideos.get(i), listenerVideo);

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
                grid.setMargin(itemVideo, new Insets(10));
            }
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }

    /**
     * Metodo utilizado para listar instancias de playlist de manera dinamica utilizando la vista de templates referente al playlist
     *
     * @param lista Lista de instancias de playlist utilizado para el listado
     * @param grid Gridpane donde se agregara el listado dinamico
     * */
    public void showItemsPlaylist(List<Playlist> lista, GridPane grid) {
        System.out.println(lista.size());
        //Define las columnas y filas iniciales para el listado
        int columnas = 0;
        int filas = 0;

        try {
            //Para cada video
            for (int i = 0; i < lista.size(); i++) {

                //Obtenemos la vista de Item de video y generamos un AnchorPane para ser agregado al Grid
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/application/View/Templates/ItemPLaylistDashboard.fxml"));
                AnchorPane itemVideo = fxmlLoader.load();

                //Uso del controller del item de video para cargar la informacion dinamicamente de cada video en la lista
                ItemPlaylistDashboardController itemController = fxmlLoader.getController();
                itemController.setData(lista.get(i), listenerPlaylist);

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
                grid.setMargin(itemVideo, new Insets(10));
            }
        } catch (IOException e) {
            System.out.println("Error" + e);
        }
    }

    /**
     * Metodo utilizado para lanzar la vista de listar videos
     *
     * */
    public void listarVideos() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Video/Listar.fxml"));

        currentPlaylist = null;

        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }

    /**
     * Metodo utilizado para lanzar la vista de listar playlists
     *
     * */
    public void listarPlaylist() throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/Playlist/Listar.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }


    /**
     * Metodo utilizado para lanzar la vista de servidor
     *
     * */
    public void initServer() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/View/Socket/Server.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

}
