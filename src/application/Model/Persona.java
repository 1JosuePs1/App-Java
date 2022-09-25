package application.Model;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Modelo de Persona
 */

public class Persona {

    /*Atributos de Persona*/
    private String id;
    private String nombre;
    private String apellidos;
    private String foto;

    /*Constructores de Persona*/
    public Persona() {}

    /** @param id,
     * @param nombre
     * @param apellidos,
     * @param foto */
    public Persona(String id, String nombre, String apellidos, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.foto = foto;
    }

    /*Getters y Setters de Persona*/

    /** @param Id  */
    public void setId(String Id) { this.id = id; }

    /** @return id */
    public String getId() {
        return id;
    }
    /** @return nombre */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre  */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** @return apellidos */
    public String getApellidos() {
        return apellidos;
    }

    /** @param apellidos  */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    /** @return foto */
    public String getFoto() {
        return foto;
    }
    /** @param foto  */
    public void setFoto(String foto) {
        this.foto = foto;
    }
}
