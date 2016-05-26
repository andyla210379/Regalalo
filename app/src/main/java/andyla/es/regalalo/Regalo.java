package andyla.es.regalalo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Creado AndresGarcia
 * Clase regalo que retorna un objeto regalo con sus atributos
 */
public class Regalo {
    public byte[] aImagen;// Array de bytes para almacenar la imagen en base64
    public Bitmap imagen; // Imagen en Bmp
    public String nombre; // Titulo del regalo
    public String detalle;// Detalle del regalo o descripcion
    public String provincia;// Provincia a la que pertenece.

    /**Constructor
     *
     * @param imagen
     * @param nombre
     * @param detalle
     * @param provincia
     */
    public Regalo(String imagen, String nombre, String detalle,String provincia) {

        setData(imagen);
        this.nombre = nombre;
        this.detalle = detalle;
        this.provincia = provincia;
    }

    /**
     * Geters
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public Bitmap getImagen() {
        return this.imagen;
    }

    public String getProvincia(){return this.provincia;}

    /**
     * Metodo que cambia la imagen del regalo en funcon de un String en base 64
     * que se convierte a un array de bytes
     * @param data
     */
    public void setData(String data) {
        //this.aImagen = data;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            aImagen=byteData;
            this.imagen = BitmapFactory.decodeByteArray( byteData, 0, byteData.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

