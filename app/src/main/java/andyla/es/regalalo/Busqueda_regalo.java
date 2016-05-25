package andyla.es.regalalo;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Busqueda_regalo extends AppCompatActivity {

    private EditText txtQuebuscas;
    private Spinner spnDondeLoBuscas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_regalo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtQuebuscas = (EditText) findViewById(R.id.txtQueBusqueda);
        spnDondeLoBuscas = (Spinner) findViewById(R.id.spinnerDondeBusqueda);
        //
        // Creo un array par cargar los datos en el spinner
        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(this,
                R.array.provincias,android.R.layout.simple_spinner_item);
        //
        // Especifica que layout usa el adaptador de elecciones
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //
        // Se aplica al spinner
        spnDondeLoBuscas.setAdapter(spnAdapter);
        //
        // Boton flotante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titulo = txtQuebuscas.getText().toString();
                String localidad = spnDondeLoBuscas.getSelectedItem().toString();

                if(titulo.isEmpty() || spnDondeLoBuscas.getSelectedItemPosition()==0)
                {
                    Snackbar.make(view, "Titulo y Provincia requeridos", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
                else
                {
                    pasoParametrosBusqueda(titulo,localidad);
                    finish();
                }
            }
        });
    }

    //
    // Carga en las preferencias los parametros de la busqueda
    public void pasoParametrosBusqueda(String titulo,String localidad)
    {
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putString("localidad",localidad);
        editor.putString("titulo",titulo);
        editor.putBoolean("hayBusqueda",true);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
