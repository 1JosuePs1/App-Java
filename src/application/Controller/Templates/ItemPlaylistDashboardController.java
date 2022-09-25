package application.Controller.Templates;

import application.Listeners.PlaylistDashboardListener;
import application.Listeners.PlaylistListadoListener;
import application.Model.Playlist;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 20-08-2022
 *
 * Controller destinado a la vista template de playlist para el dashboard
 */
public class ItemPlaylistDashboardController {
    @FXML
    private Label labelNombre;

    private Playlist playlist;
    private PlaylistDashboardListener listener;

    @FXML
    private void play(MouseEvent mouseEvent) throws IOException {
        listener.onClickPlay(playlist);
    }

    /**
     * Metodo utilizado para colocar la informacion de la instancia de playlist recibida por parametro
     * @param playlist instancia de playlist para obtener la informacion
     * @param listener instancia de listener para los eventos de click
     **/
    public void setData(Playlist playlist, PlaylistDashboardListener listener) {
        this.playlist = playlist;
        this.listener = listener;

        labelNombre.setText(playlist.getNombre());
    }

}
