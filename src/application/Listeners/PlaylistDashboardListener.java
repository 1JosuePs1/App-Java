package application.Listeners;

import application.Model.Playlist;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Listener de Videos en el Dashboard
 **/




public interface PlaylistDashboardListener {

    /**Este Listener procesa o "escucha" el evento de Playlist al Playlist del objeto Playlist
     * @param playlist
     * */
    public void onClickPlay(Playlist playlist) throws IOException;
}
