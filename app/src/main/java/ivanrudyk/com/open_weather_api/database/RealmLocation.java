package ivanrudyk.com.open_weather_api.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Ivan on 17.08.2016.
 */
public class RealmLocation  extends RealmObject{
    @PrimaryKey
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
