package andyla.es.regalalo;

import android.app.ActivityOptions;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {



    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Explode explode = new Explode();
        explode.setDuration(3000);// Duración en milisegundos
        getWindow().setEnterTransition(explode);

        // Inicializar Animes
        List<Regalo> items = new ArrayList<>();

        items.add(new Regalo(R.drawable.angel, "Angel Beats", 230));
        items.add(new Regalo(R.drawable.death, "Death Note", 456));
        items.add(new Regalo(R.drawable.fate, "Fate Stay Night", 342));
        items.add(new Regalo(R.drawable.nhk, "Welcome to the NHK", 645));
        items.add(new Regalo(R.drawable.suzumiya, "Suzumiya Haruhi", 459));

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


    public void pasoMenuRegalo(View v)
    {
        Intent detalleRegalo = new Intent(getApplicationContext(),MenuRegalo.class);
        startActivity(detalleRegalo,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
