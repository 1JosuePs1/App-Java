package application.DAO.Video;

import application.Model.Video;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Clase abstracta para el DAO de Video
 */
public abstract class DAOVideo {

    /**
     * Metodo que se encarga del registro de videos
     * @param video Instancia de video que se desea registrar
     * */
    public abstract void store(Video video) throws SQLException;

    /**
     * Metodo que se encarga de obtener todos las videos registrados
     * @return  Lista que contiene todos los videoss
     * */
    public abstract List<Video> getAll() throws SQLException;

    /**
     * Metodo que se encarga de obtener todas las playlists recientes
     * @return  Lista que contiene todos las playlist recientes
     * */
    public abstract List<Video> getRecents() throws SQLException;

    /**
     * Metodo que se encarga de actualizar la ultima reproduccion de un video
     * @param video instancia de video que desea actializar
     * */
    public abstract void updateLastPlayed(Video video) throws SQLException;

    /**
     * Metodo que se encarga de actualizar informacion de un video
     * @param video instancia de video que desea actializar
     * */
    public abstract void update(Video video) throws SQLException;

    /**
     * Metodo que se encarga de eliminar de manera logica un video
     * @param video instancia de video que desea eliminar
     * */
    public abstract void delete(Video video) throws SQLException;

    /**
     * Metodo que se encarga de realizar actualizacion de calificacion de un video
     * @param video instancia de video que likear
     * */
    public abstract void like(Video video) throws SQLException;
}




