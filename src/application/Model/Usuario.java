package application.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Modelo de Usuario
 */

public class Usuario extends Persona{

/*Atributos de Usuario*/
    private String username;
    private String password;

    /*Constructores de Usuario*/

    /**
     * @param id
     * @param nombre
     * @param apellidos
     * @param foto
     * @param username
     * @param password
     */
    public Usuario(String id, String nombre, String apellidos, String foto, String username, String password) {
        super(id, nombre, apellidos, foto);
        this.username = username;
        this.password = password;
    }

    public Usuario() {
        super();
    }

    /*Getters y Setters de Usuario*/

    /**
     * @param username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**@return password */
    public String getPassword() {
        return password;
    }

    /**@param password*/
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param password
     * @return validated.matches()
     */
    public static boolean validatePassword(String password) {
        //Expresion regular que contiene los requerimientos del password
        String regex = "^"
                +"(?=.*[0-9])"       //representa que debe contener al menos 1 numero.
                + "(?=.*[a-z])"      //representa que debe contener al menos 1 letra minuscula
                + "(?=.*[A-Z])"      //representa que debe contener al menos 1 letra mayuscula
                + "(?=.*[@#$!¡%^&+=])" //representa que debe contener al menos 1 caracter especial
                + "(?=\\S+$)"        //representa que no deben haber espacios en blanco
                + ".{6,8}$";        //representa que debe contenedor de 6 a 8 digitos
                                     //1Testing123#
        //Uso de la clase Patter para compilar el reger usado para el password
        Pattern requirements = Pattern.compile(regex);

        //Uso del metodo matcher para encontrar coinsidencias entre el password y los requerimientos del password
        Matcher validated = requirements.matcher(password);

        return validated.matches();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
