package ivanrudyk.com.open_weather_api.iterator.fragment;

/**
 * Created by Ivan on 10.08.2016.
 */
public interface NavigationDraverIterator {

    void addLocation(String newLocation, OnDraverFinishedListener onDraverFinishedListener);

    interface OnDraverFinishedListener {

        void onLocatoinAddError();

        void onSuccess(String newLocation);
    }
}
