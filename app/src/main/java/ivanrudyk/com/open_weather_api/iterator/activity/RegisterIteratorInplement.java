package ivanrudyk.com.open_weather_api.iterator.activity;

import android.os.Handler;
import android.text.TextUtils;

import ivanrudyk.com.open_weather_api.model.ModelUser;

/**
 * Created by Ivan on 03.08.2016.
 */
public class RegisterIteratorInplement implements RegisterIterator {
    @Override
    public void register(final ModelUser user, final OnRegisterFinishedListener listener, final String[] paswordsAndCity) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                if (TextUtils.isEmpty(user.getUserName())){
                    listener.onUsernameError();
                    error = true;
                }
                if (TextUtils.isEmpty(user.getEmailAdress())){
                    listener.onLoginError();
                    error = true;
                }
                if (TextUtils.isEmpty(paswordsAndCity[2])){
                    listener.onCityError();
                    error = true;
                }

                if (TextUtils.isEmpty(paswordsAndCity[1])){
                    listener.onConfirmPasswordError("is empty");
                    error = true;
                }

                if (!paswordsAndCity[0].equals(paswordsAndCity[1])){
                    listener.onConfirmPasswordError("password is not equals");
                    error = true;
                }

                if (TextUtils.isEmpty(paswordsAndCity[0])){
                    listener.onPasswordError();
                    error = true;
                }
                if (!error){
                    listener.onSuccess();
                }
            }
        }, 2000);
    }

}
