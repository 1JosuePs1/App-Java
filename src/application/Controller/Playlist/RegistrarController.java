package application.Controller.Playlist;

import application.DAO.Video.DAOVideoImpl;
import application.Model.Playlist;
import application.Model.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import application.DAO.Playlist.DAOPlaylistImpl;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado al registro de playlists
 */
public class RegistrarController implements Initializable {

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

    private List<Video> allVideos;
    private List<Video> videosPlaylist = new ArrayList<>();;

    private Playlist currentPlaylist;

    String errorMessage = String.format("-fx-text-fill: RED;");
    String validateMessage = String.format("-fx-text-fill: BLUE;");


    /**
     * Constructor del controller, utilizado para inicializar la lista que contendra las instancias de videos
     *
     */
    public RegistrarController() {
        try {
            allVideos = DAOVideo.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAllVideos();
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
            registerPlaylist();
        }
    }


    /**
     * Metodo para limpiar el formulario de registro despues de un registro exitoso
     *
     */
    public void cleanForm() {
        txtNamePlaylist.setText("");
        txtTemaPlaylist.setText("");

        playlistAlertaMensaje.setText("La Playlist se ha agregado con exito");
        playlistAlertaMensaje.setStyle(validateMessage);

        setAllVideos();
    }


    /**
     * Metodo para obtener la fecha y hora del momento de ejecucion
     *
     */
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }


    /**
     * Metodo utlizado para generar de manera aleatoria ids para registrar playlists
     *
     */
    public String getPlaylistID () {
        Random rnd = new Random();
        int id = rnd.nextInt(999999);

        return String.format("%06d", id);
    }


    /**
     * Metodo para el registro de playlists con la informacion dada por el usuario
     *
     */
    public void registerPlaylist() throws IOException, SQLException {
        int id = Integer.parseInt(getPlaylistID());
        String nombre = txtNamePlaylist.getText();
        String tema  = txtTemaPlaylist.getText();
        String fechaCreacion = getCurrentDate();

        currentPlaylist = new Playlist(id, nombre, tema, fechaCreacion);

        DAOPlaylist.store(currentPlaylist);
        saveVideos();
    }

    /**
     *  Metodo para obtnener en una lista de videos los videos seleccionados por el usuario para generar el playlist
     *
     */
    public void saveVideos() throws SQLException {
        for (int i = 0; i<allVideos.size(); i++) {
            for (int j = 0;  j<StringsPlaylist.size(); j++) {
                if ( allVideos.get(i).getNombre().equals(StringsPlaylist.get(j))) {
                    videosPlaylist.add(allVideos.get(i));
                    System.out.println(allVideos.get(i).getNombre());
                }
            }
        }

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

        cleanForm();
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
