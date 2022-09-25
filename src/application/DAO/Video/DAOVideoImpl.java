package application.DAO.Video;

import application.DAO.Conexion;
import application.DAO.Video.DAOVideo;
import application.Model.Video;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOVideoImpl extends DAOVideo {

    Conexion conexion = Conexion.getInstance();

    @Override
    public void store(Video video) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement insert  = conectar.prepareStatement("INSERT INTO Video(nombre, descripcion, duracion, ruta, fecha_creacion, nombre_categoria) VALUES (?,?,?,?,?,?)");

            insert.setString(1, video.getNombre());
            insert.setString(2, video.getDescripcion());
            insert.setDouble(3, video.getDuracion());
            insert.setString(4, video.getRuta());
            insert.setTimestamp(5, java.sql.Timestamp.valueOf(video.getFechaCreacion()));
            insert.setString(6,video.getCategoria());

            insert.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public List<Video> getAll() throws SQLException {
        List<Video> listaVideos = new ArrayList<>();
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement getAll  = conectar.prepareStatement("SELECT * FROM video WHERE estado = 1 ORDER BY fecha_creacion ASC");

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
    public List<Video> getRecents() throws SQLException {
        List<Video> videosRecientes = new ArrayList<>();
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement getRecent  = conectar.prepareStatement("SELECT * FROM video WHERE ultima_repr IS NOT NULL AND estado = 1  ORDER BY ultima_repr DESC");

            ResultSet rs = getRecent.executeQuery();

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

                videosRecientes.add(video);
            }
            conexion.closeConexion();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return videosRecientes;
    }

    @Override
    public void updateLastPlayed(Video video) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();

            PreparedStatement update  = conectar.prepareStatement("UPDATE video SET ultima_repr = ? WHERE id = ?"); // PreparedStatement es un objeto que contiene una sentencia precompilada de SQL

            update.setTimestamp(1, java.sql.Timestamp.valueOf(video.getUltimaReproduccion())); // El 1 como "parameterIndex" equivale al primer signo de pregunta del Pepared Statement y luego define su valor después de la coma
            update.setInt(2, video.getId());

            update.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void update(Video video) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();

            PreparedStatement updateVideo  = conectar.prepareStatement("UPDATE video SET nombre = ? , descripcion = ?, nombre_categoria = ? WHERE id = ?");
            updateVideo.setString(1, video.getNombre());
            updateVideo.setString(2, video.getDescripcion());
            updateVideo.setString(3, video.getCategoria());
            updateVideo.setInt(4, video.getId());

            updateVideo.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void delete(Video video) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement delete  = conectar.prepareStatement("UPDATE video SET estado = 0 WHERE id = ?");

            delete.setInt(1, video.getId());

            delete.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void like(Video video) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement like  = conectar.prepareStatement("UPDATE video SET calificacion = ? WHERE id = ?");

            like.setInt(1, video.getCalificacion());
            like.setInt(2, video.getId());

            like.executeUpdate();
            conexion.closeConexion();

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
}

