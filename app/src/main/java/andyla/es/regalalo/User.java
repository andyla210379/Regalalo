package andyla.es.regalalo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andyla on 10/05/2016.
 */
public class User {
    public Integer userId;
    public String username;
    public String password;
    public boolean loginOK;

    public User(int userId, String username, String password){
        this.userId=userId;
        this.username=username;
        this.password=password;
        this.loginOK=false;
    }

}