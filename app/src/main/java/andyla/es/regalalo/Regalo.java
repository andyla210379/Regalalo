package andyla.es.regalalo;

/**
 * Creado AndresGarcia
 */
public class Regalo {
    public int imagen;
    public String nombre;
    public String detalle;

    public Regalo(int imagen, String nombre, String detalle) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.detalle = detalle;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public int getImagen() {
        return imagen;
    }
}
