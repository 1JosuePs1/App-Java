package application.DAO.Categoria;

import application.DAO.Categoria.DAOCategoria;
import application.DAO.Conexion;
import application.Model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCategoriaImpl extends DAOCategoria {

    Conexion conexion = Conexion.getInstance();

    @Override
    public void store(Categoria categoria) throws SQLException {
        try {
            Connection conectar =  conexion.setConexion();
            PreparedStatement insert = conectar.prepareStatement("INSERT INTO categoria(nombre, descripcion) VALUES (?,?)");

            insert.setString(1, categoria.getNombre());
            insert.setString(2, categoria.getDescripcion());

            insert.executeUpdate();
            conexion.closeConexion();

            System.out.println("Agregada Exitosamente");

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public List<String> getAll() {

        List<String> listaCategoria = new ArrayList<String>();

        try {
            Connection conectar = conexion.setConexion();
            PreparedStatement getAll= conectar.prepareStatement("SELECT nombre FROM categoria ORDER BY nombre ASC ");

            ResultSet rs = getAll.executeQuery();

            while (rs.next()) {
                listaCategoria.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return listaCategoria;
    }


}
