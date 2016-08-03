package ivanrudyk.com.open_weather_api.model_user;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Ivan on 04.07.2016.
 */
public class Users {
    private String login;
    private String password;
    private String userName;
    private Bitmap Photo;


    public Users() {
    }
    public Bitmap getPhoto() {
        return Photo;
    }

    public void setPhoto(Bitmap photo) {
        Photo = photo;
    }
    public Users(String userName) {
        this.userName=userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private List<String> location;



    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
