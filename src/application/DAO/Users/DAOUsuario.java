package application.DAO.Users;

import application.Model.Usuario;
import java.sql.SQLException;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Clase abstracta para el DAO de Usuarios
 */
public abstract class DAOUsuario {

    /**
     * Metodo que se encarga del registro de usuarios
     * @param usuario Instancia de usuario que se desea registrar
     * */
    public abstract boolean store(Usuario usuario) throws SQLException;

    /**
     * Metodo que se encarga validar el inicio de sesion con los datos recibidos
     * @param username String del nombre de usuario
     * @param password String del password de usuario
     * @return  Usuario, retorna el usuario encontrado, si hace login correcto de lo contrario devuelve un usuario null
     * */
    public abstract Usuario auth(String username, String password) throws SQLException;

}