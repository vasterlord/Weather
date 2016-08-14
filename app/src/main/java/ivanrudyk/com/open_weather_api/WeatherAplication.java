package ivanrudyk.com.open_weather_api;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ivan on 14.08.2016.
 */
public class WeatherAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        showHash();
    }

    public void showHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ivanrudyk.com.open_weather_api",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}