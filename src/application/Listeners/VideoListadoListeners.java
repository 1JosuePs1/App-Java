package application.Listeners;

import application.Model.Video;

import java.io.IOException;
import java.sql.SQLException;
/**
 * @author Berny Fabricio Salgado Ulloa
 * @version 1.0
 * @since 01-08-2022
 *
 * Interfaz para los eventos de click de los items de videos listados
 *
 */

public interface VideoListadoListeners {
    /**Este Listener procesa o "escucha" el evento del Video listado al dar click on play
     * @param video
     * */
    public void onClickPlay(Video video) throws IOException;
    /**Este Listener procesa o "escucha" el evento del Video listado al dar click on edit
     * @param video
     * */
    public void onClickEdit(Video video) throws IOException;
    /**Este Listener procesa o "escucha" el evento del Video listado al dar click on delete
     * @param video
     * */
    public void onClickDelete(Video video) throws SQLException;
}
