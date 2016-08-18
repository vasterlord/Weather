package ivanrudyk.com.open_weather_api.iterator.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;

import ivanrudyk.com.open_weather_api.model.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public class RegisterIteratorInplement implements RegisterIterator {
    @Override
    public void register(final Users user, final OnRegisterFinishedListener listener,  final String confPass, final String city, final Bitmap photoLoad) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(user.getUserName())){
                    listener.onUsernameError();
                    error = true;
                }
                if (TextUtils.isEmpty(user.getLogin())){
                    listener.onLoginError();
                    error = true;
                }
                if (TextUtils.isEmpty(city)){
                    listener.onCityError();
                    error = true;
                }

                if (TextUtils.isEmpty(confPass)){
                    listener.onConfirmPasswordError("is empty");
                    error = true;
                }

                if (!user.getPassword().equals(confPass)){
                    listener.onConfirmPasswordError("password is not equals");
                    error = true;
                }

                if (TextUtils.isEmpty(user.getPassword())){
                    listener.onPasswordError();
                    error = true;
                }
                if (!error){
                    listener.onSuccess(user, photoLoad);
                }
            }
        }, 2000);
    }

}
