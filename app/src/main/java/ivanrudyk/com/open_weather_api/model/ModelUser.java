package ivanrudyk.com.open_weather_api.model;

import android.graphics.Bitmap;

/**
 * Created by Ivan on 21.08.2016.
 */
public class ModelUser {
    private String userName;
    private String emailAdress;
    private ModelLocation location = new ModelLocation();
    private Bitmap photo;

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getEmailAdress() {

        return emailAdress;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public ModelLocation getLocation() {
        return location;
    }

    public void setLocation(ModelLocation location) {
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
