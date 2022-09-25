package application.Controller.Playlist;

import application.DAO.Playlist.DAOPlaylistImpl;
import application.DAO.Video.DAOVideoImpl;
import application.Model.Playlist;
import application.Model.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import static application.Controller.Playlist.ListarController.currentPlaylist;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado a la actualizacion de playlists
 */
public class ActualizarController implements Initializable {

    /*
     * Atributos a utilizar del controller
     *
     * */
    DAOPlaylistImpl DAOPlaylist = new DAOPlaylistImpl();
    DAOVideoImpl DAOVideo = new DAOVideoImpl(); //Instancia del DAO video

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtNamePlaylist;
    @FXML
    private TextField txtTemaPlaylist;
    @FXML
    private Label playlistAlertaMensaje;

    @FXML
    private ListView<String> listAllVideos;
    @FXML
    private ListView<String>listPlaylist;

    private ObservableList<String> StringsAllVideos;
    private ObservableList<String> StringsPlaylist;

    private List<Video> tempAllVideos;
    private List<Video> allVideos;
    private List<Video> currentVideos;

    private List<Video> videosPlaylist = new ArrayList<>();

    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");


    /**
     * Constructor del controller, utilizado para inicializar la listas que contendran las instancias de videos
     *
     */
    public ActualizarController() {
        try {
            allVideos = DAOVideo.getAll();
            tempAllVideos = DAOVideo.getAll();
            currentVideos = DAOPlaylist.getAllVideoPlaylist(currentPlaylist.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPlaylistData();
        setAllVideos();
    }

    /**
     * Metodo utilizado para mostrar en el listview los videos actuales del playlists registrado
     *
     */
    public void getPlaylistData() {
        txtNamePlaylist.setText(currentPlaylist.getNombre());
        txtTemaPlaylist.setText(currentPlaylist.getTema());

        for (int i = 0; i<allVideos.size(); i++) {
            for (int j = 0; j < currentVideos.size(); j++) {
                if (allVideos.get(i).getId() == currentVideos.get(j).getId()) {
                    allVideos.remove(i);
                }
            }
        }
    }

    /**
     * Metodo utilizado para colocar en los listview los videos registrados
     *
     */
    public void setAllVideos () {
        StringsAllVideos = FXCollections.observableArrayList();
        StringsPlaylist = FXCollections.observableArrayList();

        StringsPlaylist.clear();
        listPlaylist.getItems().clear();

        for (int i = 0; i < allVideos.size(); i++) {
            StringsAllVideos.add(allVideos.get(i).getNombre());
        }
        for (int i = 0; i < currentVideos.size(); i++) {
            StringsPlaylist.add(currentVideos.get(i).getNombre());
        }

        listPlaylist.setItems(StringsPlaylist);
        listAllVideos.setItems(StringsAllVideos);
    }

    /**
     * Metodo utilizado para validar el formulario registro
     *
     */
    public void validationFormPlaylist(MouseEvent actionEvent) throws SQLException, IOException {
        if (txtNamePlaylist.getText().isBlank() ||
                txtTemaPlaylist.getText().isBlank())
        {
            System.out.println("Campos vacios");
            playlistAlertaMensaje.setText("Todos los campos son requeridos");
            playlistAlertaMensaje.setStyle(errorMessage);
        }else {
            updatePlaylist();
        }
    }


    /**
     * Metodo para la actualizacion de playlists con la informacion dada por el usuario
     *
     */
    public void updatePlaylist() throws IOException, SQLException {

        String nombre = txtNamePlaylist.getText();
        String tema  = txtTemaPlaylist.getText();

        currentPlaylist.setNombre(nombre);
        currentPlaylist.setTema(tema);

        saveVideos();
    }

    /**
     *  Metodo para obtnener en una lista de videos los videos seleccionados por el usuario para generar el playlist
     *
     */
    public void saveVideos() throws SQLException {

        System.out.println("size" + tempAllVideos.size());

        for (int i = 0; i<tempAllVideos.size(); i++) {
            for (int j = 0;  j<StringsPlaylist.size(); j++) {
                if ( tempAllVideos.get(i).getNombre().equals(StringsPlaylist.get(j))) {
                    videosPlaylist.add(tempAllVideos.get(i));
                    System.out.println(tempAllVideos.get(i).getNombre());
                }
            }
        }

        DAOPlaylist.update(currentPlaylist, true);

        storeVideoPlaylist(videosPlaylist, currentPlaylist);
    }

    /**
     * Metodo para el registro de los videos seleccionados por el usuario en base de datos
     *
     */
    public void storeVideoPlaylist (List<Video> videosPlaylist, Playlist currentPlaylist) throws SQLException {
        for (int i = 0; i<videosPlaylist.size(); i++) {
            DAOPlaylist.storeVideosPlaylist(videosPlaylist.get(i).getId(), currentPlaylist.getId());
        }

        playlistAlertaMensaje.setText("La Playlist se ha actualizado con exito");
        playlistAlertaMensaje.setStyle(validateMessage);
    }

    /**
     * Metodo utilizado para la seleccion interactiva de los videos registrados y los videos que el usuario desea agregar al playlist
     *
     */
    public void addTo(MouseEvent event) {
        Node aNode = (Node)event.getSource();
        int videoSelected;

        if (event.getClickCount() == 1) {

            if (aNode.equals(listAllVideos)) {
                videoSelected = listAllVideos.getSelectionModel().getSelectedIndex();

                if (listAllVideos.getSelectionModel().getSelectedItem() != null) {
                    StringsPlaylist.add(listAllVideos.getSelectionModel().getSelectedItem());
                }
                if (videoSelected != -1) {
                    StringsAllVideos.remove(videoSelected);
                }

                listPlaylist.setItems(StringsPlaylist);
            } else {
                videoSelected = listPlaylist.getSelectionModel().getSelectedIndex();

                if (listPlaylist.getSelectionModel().getSelectedItem() != null) {
                    StringsAllVideos.add(listPlaylist.getSelectionModel().getSelectedItem());
                }
                if (videoSelected != -1) {
                    StringsPlaylist.remove(videoSelected);
                }

                listAllVideos.setItems(StringsAllVideos);
            }

        }
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
