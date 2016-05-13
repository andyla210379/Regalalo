package andyla.es.regalalo;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por AndresGarcia
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private User usuario;
    public List<Regalo> items = new ArrayList<>();
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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        cargarPreferencias();
        //
        /*Explode explode = new Explode();
        explode.setDuration(3000);// Duración en milisegundos
        getWindow().setEnterTransition(explode);*/
        //

        // Inicializar tarjetas
        //
        // Añado los items
        items.add(new Regalo(R.drawable.angel, "Angel Beats", "afsdasdf"));
        items.add(new Regalo(R.drawable.death, "Death Note", "fasdfadf"));
        items.add(new Regalo(R.drawable.fate, "Fate Stay Night", "adfasfdfa"));
        items.add(new Regalo(R.drawable.nhk, "Welcome to the NHK", "fasfsafdsa"));
        items.add(new Regalo(R.drawable.suzumiya, "Suzumiya Haruhi", "fasdfdsfa"));

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);

        //
        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    public static final String TAG ="" ;

                    @Override public void onItemClick(View view, int position) {
                        onClickTarjeta(position);
                        //Log.d(TAG, "onClick"+position+"/");
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

        // Crear un nuevo adaptador
        adapter = new RegaloAdapter(items);
        recycler.setAdapter(adapter);

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

        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    }

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
    // Metodo para abrir la camara


    public void sacarFoto()
    {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //Creamos una carpeta en la memeria del terminal
        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "fotos");
        imagesFolder.mkdirs();
        //añadimos el nombre de la imagen
        File image = new File(imagesFolder, "foto.jpg");
        Uri uriSavedImage = Uri.fromFile(image);
        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        //Lanzamos la aplicacion de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, 1);

    }

    //
    // Recojo los resultados del activiti
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras =data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i("Resultado del Activity","Foto hecha");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            llamarIntentMenuRegaloNuevo(byteArray);
        }
    }


    //
    // Metodo para redimensionar una imagen
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }



    public Bitmap getFoto()
    {
        Bitmap imagen = null;
        URL imageUrl = null;
        HttpURLConnection conn = null;

        try {

            imageUrl = new URL("http://pagina.com/foto.jpg");
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2

            imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);

            // img.setImageBitmap(imagen);

        } catch (IOException e) {

            e.printStackTrace();

        }
        return imagen;
    }
    //
    // Cuando hago click en una tarjeta
    public void onClickTarjeta(int position)
    {
        String titulo= this.items.get(position).getNombre();
        int imagen= items.get(position).getImagen();
        String detalle = items.get(position).getDetalle();
        //
        // Creo un intent para pasar los datos y cambiar de vista
        Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
        detalleRegalo.putExtra("titulo",titulo);
        detalleRegalo.putExtra("imagen",imagen);
        detalleRegalo.putExtra("detalle",detalle);
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
    //
    // Metodo que controla el actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_search){

            Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
            startActivity(detalleRegalo);
        }

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            //...

    }

        return super.onOptionsItemSelected(item);
    }





}


