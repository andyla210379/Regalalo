package andyla.es.regalalo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Creado AndresGarcia
 */
public class Regalo {
    public byte[] aImagen;
    public Bitmap imagen;
    public String nombre;
    public String detalle;
    public String provincia;

    public Regalo(String imagen, String nombre, String detalle,String provincia) {

        setData(imagen);
        this.nombre = nombre;
        this.detalle = detalle;
        this.provincia = provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public Bitmap getImagen() {
        return this.imagen;
    }

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

    public String getProvincia(){return this.provincia;}
}
