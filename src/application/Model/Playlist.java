package application.Model;

import java.util.List;
/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Modelo de Playlist
 */
public class Playlist {

/*Atributos de Playlist*/

    private int id;
    private String nombre;
    private String tema;
    private String fechaCreacion;
    private String ultimaReproduccion;
    private int cantidad;

    /*Constructores de Playlist*/
    /**@param id
     * @param nombre
     * @param fechaCreacion
     * @param tema
     * */
    public Playlist(int id, String nombre, String tema, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.tema = tema;
        this.fechaCreacion = fechaCreacion;
    }

    public Playlist() {}


    /*Getters y Setters de Playlist*/

    /** @return id */
    public int getId() {
        return id;
    }
    /** @param id  */
    public void setId(int id) {
        this.id = id;
    }
    /** @return nombre */
    public String getNombre() {
        return nombre;
    }
    /** @param nombre  */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** @return tema */
    public String getTema() {
        return tema;
    }
    /** @param tema  */
    public void setTema(String tema) {
        this.tema = tema;
    }
    /** @return fechaCreacion */
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    /** @param fechaCreacion  */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /** @return ultimaReproduccion */
    public String getUltimaReproduccion() {
        return ultimaReproduccion;
    }
    /** @param ultimaReproduccion  */
    public void setUltimaReproduccion(String ultimaReproduccion) {
        this.ultimaReproduccion = ultimaReproduccion;
    }
    /** @return cantidad */
    public int getCantidad() {
        return cantidad;
    }
    /** @param cantidad  */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
