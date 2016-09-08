package ivanrudyk.com.open_weather_api.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 17.08.2016.
 */
public class ModelLocation {
    private ArrayList<String> location = new ArrayList<>();

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }
}
