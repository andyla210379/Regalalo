package andyla.es.regalalo;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MenuRegaloNuevo extends BaseVolleyFragment implements AdapterView.OnItemSelectedListener  {

    private ImageView imagenRegaloNuevo;
    private EditText tituloRegalNuevo;
    private EditText descripcionRegaloNuevo;
    private TextView contador;
    private Spinner spinner;
    private VolleyS volley;
    private User usuario;
    protected RequestQueue fRequestQueue;
    private byte [] imagen;
    private String imagenBase;
    private String UPLOAD_URL="https://proyectoagl-andyla.c9users.io/uploadFoto.php";
    private String KEY_IMAGE = "image";
    private String KEY_IDUSUARIO ="idUsuario";
    private String KEY_NAME = "name";
    private String KEY_PROVINCIA="provincia";
    private String KEY_DESCRIPCION="descripcion";
    private String provinciaSeleccionada="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volley = VolleyS.getInstance(getApplicationContext());
        fRequestQueue = volley.getRequestQueve();
        setContentView(R.layout.activity_menu_regalo_nuevo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        //Cargo los datos de SharedPreferences
        cargarPreferencias();
        //
        // Creo el intent
        Intent intent = getIntent();
        //
        // Recojo los datos de la imagen
        Bundle extras = intent.getExtras();
        //
        // Creo un array de bytes con los datos recogidos
        imagen = extras.getByteArray("imagen");
        //
        // Decodifico el array y lo guardo como imagen
        Bitmap bmp = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
        //
        // Enlazo los controles
        imagenRegaloNuevo = (ImageView) findViewById(R.id.imagenRegaloNuevo);
        tituloRegalNuevo = (EditText)findViewById(R.id.tituloRegaloNuevo);
        descripcionRegaloNuevo = (EditText)findViewById(R.id.descripcionRegaloNuevo);
        spinner = (Spinner) findViewById(R.id.spinner);
        contador= (TextView) findViewById(R.id.texto_contador);
        contador.setText("0");

        //
        // Controla el contador de espacios disponible
        tituloRegalNuevo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tamanoString = String.valueOf(s.length());
                contador.setText(tamanoString);
            }
        });
        //
        // Creo un array par cargar los datos en el spinner
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
        Float megasImagen=((float)(imagen.length/1024)/1024);
        Log.i("Tamaño de imagen:",megasImagen.toString());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView=null;

                String provincia = spinner.getSelectedItem().toString().trim();
                String descripcion= descripcionRegaloNuevo.getText().toString().trim();
                String  titulo= tituloRegalNuevo.getText().toString().trim();

                if(TextUtils.isEmpty(provincia) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(titulo) || provincia.equals("Selecciona tu provincia"))
                {
                    showToast("Rellene todos los campos");
                }
                else
                {
                    uploadFoto(imagen);
                    //llamarIntentMainActivity();
                }

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();*/
            }
        });
    }

    private void showToast(String s)
    {
        Toast.makeText(MenuRegaloNuevo.this, s, Toast.LENGTH_SHORT).show();
    }

    //
    // Cargo los datos de SharedPreferences
    public void cargarPreferencias()
    {
        String password;
        String nombre;
        int id;
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        id=misPreferencias.getInt("id",0);
        nombre= misPreferencias.getString("nombre","");
        password = misPreferencias.getString("password","");
        usuario=new User(id,nombre,password);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void uploadFoto(byte [] foto)
    {

        imagenBase = Base64.encodeToString(foto,Base64.DEFAULT);
        Log.i("Imagen",imagenBase.toString());
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(MenuRegaloNuevo.this, "Regalo subido" , Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(MenuRegaloNuevo.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(bitmap);---------------------->Cambio por la foto

                //Recojo los datos del regalo
                String name = tituloRegalNuevo.getText().toString().trim();
                String provincia = spinner.getSelectedItem().toString();
                String descripcion = descripcionRegaloNuevo.getText().toString().trim();
                String idUsuario = usuario.userId.toString();

                //Creo los parametros
                Map<String,String> params = new Hashtable<String, String>();

                //Añado los parametros
                params.put(KEY_IMAGE, imagenBase);
                params.put(KEY_NAME, name);
                params.put(KEY_PROVINCIA,provincia);
                params.put(KEY_DESCRIPCION,descripcion);
                params.put(KEY_IDUSUARIO,idUsuario);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void llamarIntentMainActivity()
    {
        //finish();
        Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);

        //menuRegaloNuevo.putExtra("imagen",imagen);
        MenuRegaloNuevo.this.startActivity(mainActivity);
        //finish();
    }


}
