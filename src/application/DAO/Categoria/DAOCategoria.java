package application.DAO.Categoria;

import application.Model.Categoria;
import java.sql.SQLException;
import java.util.List;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Clase abstracta para el DAO de Categoria
 */
public abstract class DAOCategoria {

    /**
     * Metodo que se encarga del registro de Categorias
     * @param categoria Instancia de categoria que se desea registrar
     * */
    public abstract void store(Categoria categoria) throws SQLException;

    /**
     * Metodo que se encarga de obtener todas las categorias registradas
     * @return getAll Lista que contiene todos las categorias
     * */
    public abstract List<String> getAll() throws SQLException;

}
