package application.Listeners;

import application.Model.Playlist;
import java.io.IOException;
import java.sql.SQLException;
/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Listener del Listado de Videos
 */


public interface PlaylistListadoListener {
    /**
     * Este Listener procesa o "escucha" los eventos de Listado de Videos al dar click on play.
     * @param playlist
     **/
    public void onClickPlay(Playlist playlist) throws IOException;

    /**
     * Este Listener procesa o "escucha" los eventos de Listado de Videos al dar click en edit.
     * @param playlist
     **/
    public void onClickEdit(Playlist playlist) throws IOException, SQLException;
    /**
     * Este Listener procesa o "escucha" los eventos de Listado de Videos al dar click en delete
     * @param playlist
     **/
    public void onClickDelete(Playlist playlist) throws SQLException;
}
