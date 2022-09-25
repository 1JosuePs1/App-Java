package application.DAO.Playlist;

import application.Model.Playlist;
import application.Model.Video;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Clase abstracta para el DAO de Playlist
 */
public abstract class DAOPlaylist {

    /**
     * Metodo que se encarga del registro de playlists
     * @param playlist Instancia de playlist que se desea registrar
     * */
    public abstract void store(Playlist playlist) throws SQLException;

    /**
     * Metodo que se encarga del registro de videos - playlists
     * @param idVideo ID del video que se desea relacionar con el playlist
     * @param idPlaylist ID del playlist que se desea relacion al video
     * */
    public abstract void storeVideosPlaylist(int idVideo, int idPlaylist) throws SQLException;

    /**
     * Metodo que se encarga de obtener todas las playlists registradas
     * @return  Lista que contiene todos las playlist
     * */
    public abstract List<Playlist> getAll() throws SQLException;

    /**
     * Metodo que se encarga de obtener todas las playlists recientes
     * @return  Lista que contiene todos las playlist recientes
     * */
    public abstract List<Playlist> getRecents() throws SQLException;

    /**
     * Metodo que se encarga de obtener todos los videos relacionados al playlists
     * @param idPlaylist ID del playlist que desea obtener
     * @return  Lista que contiene todos los videos del playlists
     * */
    public abstract List<Video> getAllVideoPlaylist(int idPlaylist) throws SQLException;

    /**
     * Metodo que se encarga de actualizar la ultima reproduccion de una playlists
     * @param playlist ID del playlist que desea actualizar
     * */
    public abstract void updateLastPlayed(Playlist playlist) throws SQLException;
    public abstract void update(Playlist playlist, boolean editVideos) throws SQLException;

    /**
     * Metodo que se encarga de eliminar logicamente  una playlists
     * @param playlist instancia que desea eliminar
     * */
    public abstract void delete(Playlist playlist) throws SQLException;

}
