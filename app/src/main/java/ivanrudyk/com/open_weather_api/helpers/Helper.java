package ivanrudyk.com.open_weather_api.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import ivanrudyk.com.open_weather_api.adapter.HourAdapter;

/**
 * Created by Yulian on 14.08.2016.
 */
public class Helper {

    public Helper() {
    }
    GPSTracker gps;
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
    public double[] CoordTracker(Context context) {
        gps = new GPSTracker(context);
        double [] coord = new double[2];
        if(gps.canGetLocation()) {
            coord[0] = gps.getLatitude();
            coord[1] = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }
        return coord;
    }
    public void toggleRefresh(ProgressBar mProgressBar , ImageView mRefreshImageView) {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }
    public void checkAdapterIsEmpty (HourAdapter mAdapter, RecyclerView mRecyclerView) {
        if (mAdapter == null) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
    }
}
