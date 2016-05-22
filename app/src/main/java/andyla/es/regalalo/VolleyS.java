package andyla.es.regalalo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by andyla on 16/05/2016.
 */
public class VolleyS {
    private static VolleyS mVolleys = null;
    //Cola que usara la aplicacion
    private  RequestQueue mRequestQueve;

    private VolleyS (Context context)
    {

        mRequestQueve = Volley.newRequestQueue(context);
    }

    public static VolleyS getInstance(Context context)
    {

        if (mVolleys == null){

            mVolleys = new VolleyS(context);

        }
        return mVolleys;
    }

    public RequestQueue getRequestQueve()
    {
        return mRequestQueve;
    }
}
