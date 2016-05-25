package andyla.es.regalalo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class MenuUsuario extends AppCompatActivity {

    private EditText txtLoginUserConfig;
    private EditText txtPasswordUserConfig;
    private EditText getTxtPasswordUserConfigConfirm;
    private RatingBar rtnEstrellasUsuarioConfig;
    private User usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        // Creo un usuario con los datos
        cargarPreferencias();
        txtLoginUserConfig = (EditText) findViewById(R.id.txtLoginUserConfig);
        txtLoginUserConfig.setText(usuario.username );
        txtPasswordUserConfig = (EditText) findViewById(R.id.txtPasswordUserConfig);
        txtPasswordUserConfig.setText(usuario.password);
        getTxtPasswordUserConfigConfirm = (EditText) findViewById(R.id.txtPasswordUserConfigConfirm);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
