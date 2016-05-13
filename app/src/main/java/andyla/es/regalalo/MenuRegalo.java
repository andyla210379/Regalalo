package andyla.es.regalalo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuRegalo extends AppCompatActivity {

    ImageView imagenRegalo;
    TextView textoRegalo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_regalo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //
        setSupportActionBar(toolbar);
        //
        CollapsingToolbarLayout collapser = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        //
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int imagen = extras.getInt("imagen");
        String titulo = extras.getString("titulo");
        String detalle = extras.getString("detalle");
        //
        collapser.setTitle(titulo); // Cambiar título
        //
        // Enlazo los controles
        textoRegalo = (TextView) findViewById(R.id.textoRegalo);
        imagenRegalo = (ImageView) findViewById(R.id.imagenRegalo);
        //
        //
        textoRegalo.setText(detalle);
        imagenRegalo.setImageResource(imagen);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
