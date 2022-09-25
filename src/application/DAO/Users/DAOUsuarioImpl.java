package application.DAO.Users;

import application.DAO.Conexion;
import application.DAO.Users.DAOUsuario;
import application.Model.Usuario;
import java.sql.*;

public class DAOUsuarioImpl extends DAOUsuario {

    Conexion conexion = Conexion.getInstance();

    @Override
    public boolean store(Usuario usuario) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement insert = conectar.prepareStatement("INSERT INTO usuario(id, nombre, apellidos, foto, username, password) VALUES (?,?,?,?,?,?)");

            insert.setString(1, usuario.getId());
            insert.setString(2, usuario.getNombre());
            insert.setString(3, usuario.getApellidos());
            insert.setString(4, usuario.getFoto());
            insert.setString(5, usuario.getUsername());
            insert.setString(6, usuario.getPassword());

            insert.executeUpdate();
            conexion.closeConexion();

            System.out.println("Registro Exitoso");
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Usuario auth(String username, String password) throws SQLException {
        Usuario currentUser = null;

        try {
            currentUser = new Usuario();

            Connection conectar = conexion.setConexion();
            PreparedStatement search = conectar.prepareStatement("SELECT * FROM usuario WHERE username = ? AND password = ?");

            search.setString(1, username);
            search.setString(2, password);

            ResultSet rs = search.executeQuery();
            if (rs.next()) {

                currentUser.setId(rs.getString("id"));
                currentUser.setNombre(rs.getString("nombre"));
                currentUser.setApellidos(rs.getString("apellidos"));
                currentUser.setFoto(rs.getString("foto"));
                currentUser.setUsername(rs.getString("username"));
                currentUser.setPassword(rs.getString("password"));

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return currentUser;

    }
}
