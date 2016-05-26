package andyla.es.regalalo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MenuUsuario extends AppCompatActivity {

    private EditText txtLoginUserConfig;
    private EditText txtPasswordUserConfig;
    private EditText getTxtPasswordUserConfigConfirm;
    private RatingBar rtnEstrellasUsuarioConfig;
    private User usuario;
    private String URL = "https://proyectoagl-andyla.c9users.io/updateUser.php";
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

                if(txtLoginUserConfig.getText().equals("") || txtPasswordUserConfig.getText().equals(""))
                {
                    Snackbar.make(view, "Los campos no pueden estar vacios", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else if (!txtPasswordUserConfig.getText().toString().trim().equals(getTxtPasswordUserConfigConfirm.getText().toString().trim()))
                {
                    Snackbar.make(view, "Las contraseñas no coinciden", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    usuario.username = txtLoginUserConfig.getText().toString().trim();
                    usuario.password = txtPasswordUserConfig.getText().toString().trim();
                    cambiarDatosUsuario(usuario);
                    guardarPreferencias(usuario);
                    finish();
                }


            }
        });
    }

    public void cambiarDatosUsuario(User u)
    {
        RequestQueue queue = Volley.newRequestQueue(this);  // this = context

        //url = "http://httpbin.org/put";
        StringRequest putRequest = new StringRequest(Request.Method.PUT, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                       // Log.d("Error.Response", response);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nombre",usuario.username.toString() );
                params.put("password", usuario.password.toString());
                params.put("id",usuario.userId.toString());

                return params;
            }

        };

        queue.add(putRequest);
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

    public void guardarPreferencias(User u)
    {
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = misPreferencias.edit();
        editor.putInt("id",u.userId);
        editor.putString("nombre",u.username);
        editor.putString("password",u.password);
        editor.putBoolean("loginOK",u.loginOK);
        editor.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
