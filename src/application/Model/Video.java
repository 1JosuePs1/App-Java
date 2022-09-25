package application.Model;
/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Modelo de Video
 */
public class Video {

/*Atributos de Video*/
    private int id;
    private String nombre;
    private String descripcion;
    private int calificacion;
    private double duracion;
    private String ruta;
    private String fechaCreacion;
    private String ultimaReproduccion;
    private int estado;
    private String nombreCategoria;

    /*Constructores de Video*/

    /**
     * @param nombre
     * @param descripcion
     * @param duracion
     * @param ruta
     * @param fechaCreacion
     * @param nombreCategoria
     */
    public Video(String nombre, String descripcion, double duracion, String ruta, String fechaCreacion, String nombreCategoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.ruta = ruta;
        this.fechaCreacion = fechaCreacion;
        this.nombreCategoria = nombreCategoria;
    }

    public Video() {}

    /*Getters y Setters de Video*/

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**@param id*/
    public void setId(int id) {
        this.id = id;
    }

    /**@return nombre */
    public String getNombre() {
        return nombre;
    }

    /**@param nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return descripcion */
    public String getDescripcion() {
        return descripcion;
    }

    /**@param descripcion */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** @return calificacion*/
    public int getCalificacion() {
        return calificacion;
    }

    /**@param calificacion*/
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    /** @return duracion */
    public double getDuracion() {
        return duracion;
    }

    /** @param duracion */
    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    /**@return */
    public String getRuta() {
        return ruta;
    }

    /** @param ruta*/
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**@return fechaCreacion */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /** @param fechaCreacion */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**@return ultimaReproduccion*/
    public String getUltimaReproduccion() {
        return ultimaReproduccion;
    }

    /**@param ultimaReproduccion */
    public void setUltimaReproduccion(String ultimaReproduccion) {
        this.ultimaReproduccion = ultimaReproduccion;
    }

    /**@return estado*/
    public int getEstado() {
        return estado;
    }

    /**@param estado*/
    public void setEstado(int estado) {
        this.estado = estado;
    }

    /**@return nombreCategoria*/
    public String getCategoria() {
        return nombreCategoria;
    }

    /**@param nombreCategoria */
    public void setCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
