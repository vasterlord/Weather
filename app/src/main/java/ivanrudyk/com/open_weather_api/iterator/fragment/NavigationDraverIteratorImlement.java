package ivanrudyk.com.open_weather_api.iterator.fragment;

import android.os.Handler;
import android.text.TextUtils;

import ivanrudyk.com.open_weather_api.iterator.fragment.NavigationDraverIterator;

/**
 * Created by Ivan on 10.08.2016.
 */
public class NavigationDraverIteratorImlement implements NavigationDraverIterator {

    @Override
    public void addLocation(final String newLocation, final OnDraverFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(newLocation)){
                    listener.onLocatoinAddError();
                    error = true;
                }
                if (!error){
                    listener.onSuccess(newLocation);
                }
            }
        }, 1000);
    }
}
