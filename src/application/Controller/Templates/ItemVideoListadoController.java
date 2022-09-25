package application.Controller.Templates;

import application.Listeners.VideoListadoListeners;
import application.Model.Video;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;


/**
 * @author Berny Fabricio Salgado Ulloa
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado al manejo de la data de los items listados de video
 *
 */
public class ItemVideoListadoController {
    @FXML
    private Label nameVideo;
    private Video video;

    private VideoListadoListeners listener;

    @FXML
    private void playVideo(MouseEvent mouseEvent) throws IOException {
        listener.onClickPlay(video);
    }
    @FXML
    private void editVideo(MouseEvent mouseEvent) throws IOException {
        listener.onClickEdit(video);
    }
    @FXML
    private void deleteVideo(MouseEvent mouseEvent) throws SQLException {
        listener.onClickDelete(video);
    }

    /**
     * Metodo utilizado para asignar la informacion de la instancia de video recibida para la vista de item
     *
     * @param video De tipo video, hace referencia a la instancia de video que se desea listar
     * */
    public void setData(Video video, VideoListadoListeners listener) {
        this.video = video;
        this.listener = listener;

        nameVideo.setText(video.getNombre());
    }
}
