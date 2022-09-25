package application.Model;

/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Modelo de Categoría
 */


public class Categoria {

    /* Atributos de Categoría*/
    private int id;
    private String nombre;
    private String descripcion;

    /* Constructor de Categoría*/
    /**@param descripcion
     * @param nombre  */
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }


    /* Getters y Setters de Categoría*/
    public Categoria() { }


    /** @return id */
    public int getId() {
        return id;
    }

    /** @param id */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nombre  */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre*/
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** @return descripcion  */
    public String getDescripcion() {
        return descripcion;
    }

    /**@param descripcion */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
