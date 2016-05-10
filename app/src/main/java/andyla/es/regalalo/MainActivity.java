package andyla.es.regalalo;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        cargarPreferencias();
        //
        Explode explode = new Explode();
        explode.setDuration(3000);// Duración en milisegundos
        getWindow().setEnterTransition(explode);

        // Inicializar Animes
        List<Regalo> items = new ArrayList<>();

        items.add(new Regalo(R.drawable.angel, "Angel Beats", usuario.userId));
        items.add(new Regalo(R.drawable.death, "Death Note", usuario.userId));
        items.add(new Regalo(R.drawable.fate, "Fate Stay Night", usuario.userId));
        items.add(new Regalo(R.drawable.nhk, "Welcome to the NHK", usuario.userId));
        items.add(new Regalo(R.drawable.suzumiya, "Suzumiya Haruhi", usuario.userId));

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        //recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        //lManager = new LinearLayoutManager(this);
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Crear un nuevo adaptador
        adapter = new RegaloAdapter(items);
        recycler.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
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
    public void pasoMenuRegalo(View v)
    {
        Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
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

        return super.onOptionsItemSelected(item);
    }
}
