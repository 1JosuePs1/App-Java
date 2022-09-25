package application.DAO.Playlist;

import application.DAO.Conexion;
import application.DAO.Playlist.DAOPlaylist;
import application.Model.Playlist;
import application.Model.Video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOPlaylistImpl extends DAOPlaylist {

    Conexion conexion = Conexion.getInstance();

    @Override
    public void store(Playlist plylist) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement insert = conectar.prepareStatement("INSERT INTO playlist(id, nombre, tema, fecha_creacion) VALUES (?,?,?,?)");

            insert.setInt(1, plylist.getId());
            insert.setString(2, plylist.getNombre());
            insert.setString(3, plylist.getTema());
            insert.setString(4, plylist.getFechaCreacion());

            insert.executeUpdate();
            conexion.closeConexion();

            System.out.println("Playlist registrada exitosamente");

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void storeVideosPlaylist(int idVideo, int idPlaylist) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement insert = conectar.prepareStatement("INSERT INTO video_playlist VALUES (?,?)");

            insert.setInt(1, idVideo);
            insert.setInt(2, idPlaylist);

            insert.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public List<Playlist> getAll() throws SQLException {
        List<Playlist> listaPlaylist = new ArrayList<>();
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement getAll  = conectar.prepareStatement("SELECT * FROM playlist WHERE playlist.estado = 1 ORDER BY fecha_creacion ASC");

            ResultSet rs = getAll.executeQuery();

            while (rs.next()) {
                Playlist playlist = new Playlist();

                playlist.setId(rs.getInt("id"));
                playlist.setNombre(rs.getString("nombre"));
                playlist.setTema(rs.getString("tema"));
                playlist.setFechaCreacion(rs.getString("fecha_creacion"));
                playlist.setUltimaReproduccion(rs.getString("ultima_repr"));

                listaPlaylist.add(playlist);
            }
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return listaPlaylist;
    }

    @Override
    public List<Playlist> getRecents() throws SQLException {
        List<Playlist> listaPlaylistRecientes = new ArrayList<>();
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement getAll  = conectar.prepareStatement("SELECT * FROM playlist WHERE ultima_repr IS NOT NULL AND playlist.estado = 1 ORDER BY ultima_repr DESC");

            ResultSet rs = getAll.executeQuery();

            while (rs.next()) {
                Playlist playlist = new Playlist();

                playlist.setId(rs.getInt("id"));
                playlist.setNombre(rs.getString("nombre"));
                playlist.setTema(rs.getString("tema"));
                playlist.setFechaCreacion(rs.getString("fecha_creacion"));
                playlist.setUltimaReproduccion(rs.getString("ultima_repr"));

                listaPlaylistRecientes.add(playlist);
            }
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return listaPlaylistRecientes;
    }

    @Override
    public List<Video> getAllVideoPlaylist(int idPlaylist) throws SQLException {
        List<Video> listaVideos = new ArrayList<>();
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement getAll  = conectar.prepareStatement("SELECT * FROM video INNER JOIN video_playlist ON video.id = video_playlist.id_video WHERE estado = 1 AND video_playlist.id_playlist = ?");

            getAll.setInt(1, idPlaylist);

            ResultSet rs = getAll.executeQuery();

            while (rs.next()) {
                Video video = new Video();

                video.setId(rs.getInt("id"));
                video.setNombre(rs.getString("nombre"));
                video.setDescripcion(rs.getString("descripcion"));
                video.setCalificacion(rs.getInt("calificacion"));
                video.setRuta(rs.getString("ruta"));
                video.setFechaCreacion(rs.getString("fecha_creacion"));
                video.setUltimaReproduccion(rs.getString("ultima_repr"));
                video.setEstado(rs.getInt("estado"));
                video.setCategoria(rs.getString("nombre_categoria"));

                listaVideos.add(video);
            }
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return listaVideos;
    }

    @Override
    public void updateLastPlayed(Playlist playlist) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();

            PreparedStatement update  = conectar.prepareStatement("UPDATE playlist SET ultima_repr = ? WHERE id = ?");

            update.setTimestamp(1, java.sql.Timestamp.valueOf(playlist.getUltimaReproduccion()));
            update.setInt(2, playlist.getId());

            update.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void update(Playlist playlist, boolean editVideos) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();

            PreparedStatement update  = conectar.prepareStatement("UPDATE playlist SET nombre = ?, tema = ? WHERE id = ?");

            update.setString(1, playlist.getNombre());
            update.setString(2, playlist.getTema());
            update.setInt(3, playlist.getId());

            update.executeUpdate();

            if (editVideos) {
                PreparedStatement dropPlaylist  = conectar.prepareStatement("CALL DropPlaylist(?)");
                dropPlaylist.setInt(1, playlist.getId());
                System.out.println("drop");
                dropPlaylist.executeUpdate();
            }

            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void delete(Playlist playlist) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement delete  = conectar.prepareStatement("UPDATE playlist SET estado = 0 WHERE id = ?");

            delete.setInt(1, playlist.getId());

            delete.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}
