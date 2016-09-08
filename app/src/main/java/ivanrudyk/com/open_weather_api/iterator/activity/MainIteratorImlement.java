package ivanrudyk.com.open_weather_api.iterator.activity;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by Ivan on 03.08.2016.
 */
public class MainIteratorImlement implements MainIterator {
    @Override
    public void login(final String userLogin, final String userPassword, final OnMainFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(userLogin)) {
                    listener.onLoginError();
                    error = true;
                }
                if (TextUtils.isEmpty(userPassword)) {
                    listener.onPasswordError();
                    error = true;
                }
                if (!error) {
                    listener.onSuccess(userLogin, userPassword);
                }
            }
        }, 2000);
    }


}
