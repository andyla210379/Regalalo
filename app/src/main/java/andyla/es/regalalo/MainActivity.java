package andyla.es.regalalo;
//
import android.app.ActivityOptions;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//
/**
 * Creado por AndresGarcia
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RegaloAdapter adapter;
    private RecyclerView.LayoutManager lManager;
    private User usuario;
    private String url_busqueda="getAllRegalo.php";
    private String URL_ALL_REGALO="getAllRegalo.php";
    private String URL_BUSQUEDA_REGALO="getByBusqueda.php";
    private RequestQueue requestQueue;
    private FloatingActionButton fabCancelarBusqueda;
    public List<Regalo> items = new ArrayList<>();
    JsonObjectRequest jsArrayRequest;
    //
    static  final int REQUEST_IMAGE_CAPTURE = 1;
    //
    DrawerLayout drawerLayout;
    NavigationView navView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.regalo3);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.regalo3);

        //
        // Cargo las Sharedpreferences
        cargarPreferencias();
        //
        // Añado los items
        getRegalos(url_busqueda);
        //
        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);


        //
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    public static final String TAG ="" ;

                    @Override public void onItemClick(View view, int position) {
                        onClickTarjeta(position);
                    }
                })
        );
        //
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        //lManager = new GridLayoutManager(this,2);
        // lManager = new LinearLayoutManager(this);
        lManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(lManager);
        //
        // Crear un nuevo adaptador
        adapter = new RegaloAdapter(items);
        recycler.setAdapter(adapter);
        //
        // Llama al metdodo para iniciar la camara
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                // Saco la foto
                llamarIntentFoto();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

         fabCancelarBusqueda = (FloatingActionButton) findViewById(R.id.fabCancelarBusqueda);

        fabCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                // Reinicio el layout

                borrarParametrosBusqueda();
                getRegalos(URL_ALL_REGALO);
                fabCancelarBusqueda.hide();

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });


        //
        // Crear layouts
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navView = (NavigationView)findViewById(R.id.navview);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_seccion_1:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_2:
                                fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_3:
                                fragment = new Fragment3();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });


    }

    //
    // Abre el menu de hacer la foto
    private void llamarIntentFoto()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    private void llamarIntentMenuRegaloNuevo(byte [] imagen)
    {
        Intent menuRegaloNuevo = new Intent(getApplicationContext(),MenuRegaloNuevo.class);
        menuRegaloNuevo.putExtra("imagen",imagen);
        startActivity(menuRegaloNuevo,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    //
    // Recojo los resultados del activiti
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("Resultado del Activity", "Foto hecha");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            llamarIntentMenuRegaloNuevo(byteArray);
            try
            {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //
    // Cuando hago click en una tarjeta
    public void onClickTarjeta(int position)
    {
        String titulo= this.items.get(position).getNombre();
        byte [] aImagen= items.get(position).aImagen;
        String detalle = items.get(position).getDetalle();
        //
        // Creo un intent para pasar los datos y cambiar de vista
        Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
        detalleRegalo.putExtra("titulo",titulo);
        //detalleRegalo.putExtra("imagen",imagen);
        detalleRegalo.putExtra("detalle",detalle);
        detalleRegalo.putExtra("imagen",aImagen);
        //
        // Inicio la vista
        startActivity(detalleRegalo,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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

    //
    // Cambio de activity
    public void pasoMenuRegalo(String titulo,int imagen,String detalle)
    {
        Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
        detalleRegalo.putExtra("detalle",detalle);
        startActivity(detalleRegalo,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    //
    // Metodo inicio del menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Limpia las preferencias al deslogarse
     */
    public void limpiarPreferencias()
    {
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putInt("id",0);
        editor.putString("nombre","");
        editor.putString("password","");
        editor.putBoolean("loginOK",false);
        editor.commit();
    }
    //
    // Metodo que controla el actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logOut) {
            limpiarPreferencias();
            finish();
            return true;
        }
        else if(id == R.id.action_search){


            Intent busquedaRegalo = new Intent(getApplicationContext(),Busqueda_regalo.class);
            startActivity(busquedaRegalo);
            //AlertDialog busqueda = crearDialogoBusqueda();
            //busqueda.show();
            return true;
        }

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            //...
    }

        return super.onOptionsItemSelected(item);
    }

    /***Gernera un json con los regalos de la base de datos
     *
     */
    private void getRegalos(String php)
    {
        requestQueue = Volley.newRequestQueue(this);
        Log.i("getRegalos: ","Ejecutado");
        // Variables locales
        JSONArray jsonArray = null;
        // Nueva petición JSONObject
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://proyectoagl-andyla.c9users.io/"+php,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //items = parseJson(response);
                        Log.i("onResponse: ","Ejecutado");
                        parseJson(response);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("", "Error Respuesta en JSON: " + error.getMessage());
                        Log.i("onResponseError: ","Sin Conexion");

                    }
                }
        );
        // Añadir petición a la cola
        Log.i("Peticion añadida a la cola","Si");
        requestQueue.add(jsArrayRequest);
    }

    /***
     * Parse el objeto json recibido
     * @param jsonObject
     */
    public void parseJson(JSONObject jsonObject){
        // Variables locales
        //List<Regalo> posts = new ArrayList();
        if(!items.isEmpty())items.clear();
        JSONArray jsonArray= null;
        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("items");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    Regalo post = new Regalo(
                            objeto.getString("imagen"),
                            objeto.getString("titulo"),
                            objeto.getString("descripcion"),
                            objeto.getString("localidad"));

                    items.add(post);

                } catch (JSONException e) {
                    Log.e("", "Error de parsing: "+ e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void borrarParametrosBusqueda()
    {
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putString("localidad","");
        editor.putString("titulo","");
        editor.putBoolean("hayBusqueda",false);
        editor.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //
        // Cargo los resultados de la busqueda de SharedPreferences
        String localidad = "";
        String titulo    = "";
        boolean hayBusqueda = false;
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        titulo =misPreferencias.getString("titulo","");
        localidad = misPreferencias.getString("localidad","");
        hayBusqueda = misPreferencias.getBoolean("hayBusqueda",false);
        if(hayBusqueda)
        {
            getRegalos(URL_BUSQUEDA_REGALO+"?"+"titulo="+titulo+"&localidad="+localidad);
            Integer numeroItems= items.size();
            Log.i("Numero de items",numeroItems.toString());
            Log.i("Cadena de conexion",URL_BUSQUEDA_REGALO+"?"+"titulo="+titulo+"&localidad="+localidad);
            fabCancelarBusqueda.show();
            //borrarParametrosBusqueda();
        }
        else
        {
            //getRegalos(URL_ALL_REGALO);
            //fabCancelarBusqueda.setVisibility(-1);
        }

        //url_busqueda="getByBusqueda.php";

        //adapter.notifyDataSetChanged();

        //finish();
        //startActivity(MainActivity.this.getIntent());
    }


}


