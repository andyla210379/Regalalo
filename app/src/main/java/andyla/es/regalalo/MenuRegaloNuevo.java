package andyla.es.regalalo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class MenuRegaloNuevo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imagenRegaloNuevo;
    EditText tituloRegalNuevo;
    EditText descripcionRegaloNuevo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_regalo_nuevo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        toolbar.setTitle("Haz un Regalo");
        //
        // Creo el intent
        Intent intent = getIntent();
        //
        // Recojo los datos
        Bundle extras = intent.getExtras();
        //
        // Creo un array de bytes con los datos recogidos
        byte [] imagen = extras.getByteArray("imagen");
        //
        // Decodifico el array y lo guardo como imagen
        Bitmap bmp = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
        //
        // Enlazo los controles
        imagenRegaloNuevo = (ImageView) findViewById(R.id.imagenRegaloNuevo);
        tituloRegalNuevo = (EditText)findViewById(R.id.tituloRegaloNuevo);
        descripcionRegaloNuevo = (EditText)findViewById(R.id.descripcionRegaloNuevo);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //
        // Creo un array par cargar los datos
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.provincias,android.R.layout.simple_spinner_item);
        //
        // Especifica que layout usa el adaptador de elecciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Se aplica
        spinner.setAdapter(adapter);

        //
        // Cargo la imagen en el control
        imagenRegaloNuevo.setImageBitmap(bmp);
        //
        // muestro el tamaño
        Integer megasImagen=((imagen.length/1024)/1024);
        Log.i("Tamaño de imagen:",megasImagen.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
