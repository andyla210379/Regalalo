package andyla.es.regalalo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable(){
            public void run(){

                if(testConexion())
                {
                    if(userLoginOK())
                    {
                        // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(SplashScreen.this, "SIN CONEXION DE RED", Toast.LENGTH_SHORT).show();
                }
            }
        }, DURACION_SPLASH);
    }

    public boolean testConexion()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean userLoginOK()
    {
        String password;
        String nombre;
        int id;
        boolean loginOK;
        SharedPreferences misPreferencias = getSharedPreferences("preferenciasUsuario", Context.MODE_PRIVATE);
        id=misPreferencias.getInt("id",0);
        nombre= misPreferencias.getString("nombre","");
        password = misPreferencias.getString("password","");
        loginOK = misPreferencias.getBoolean("loginOK",false);
        return loginOK;
        //usuario=new User(id,nombre,password);
    }
}
