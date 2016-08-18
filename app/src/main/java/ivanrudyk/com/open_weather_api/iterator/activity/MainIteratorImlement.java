package ivanrudyk.com.open_weather_api.iterator.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.model.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public class MainIteratorImlement implements MainIterator {
    private Users users = new Users();
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private ArrayList<Users> retrivUserArray = new ArrayList<>();
    private OnMainFinishedListener onMainFinishedListener;
    Bitmap b;


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

    @Override
    public void loginFasebook(Profile profile, OnMainFinishedListener onMainFinishedListener, Context context) {
        this.onMainFinishedListener = onMainFinishedListener;
        Picasso.with(context)
                .load(profile.getProfilePictureUri(256, 256))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                       users.setPhoto(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        users.setUserName(profile.getName());

        LoginFacebookTasck loginFacebookTasck = new LoginFacebookTasck();
        loginFacebookTasck.execute();
    }


    private boolean retrivActiveUser(String userName) {
        Boolean ret = false;
        for (int userNumber = 0; userNumber < retrivUserArray.size(); userNumber++) {
            if (userName.equals(retrivUserArray.get(userNumber).getUserName())) {
                users.setUserName(retrivUserArray.get(userNumber).getUserName());
                ret = true;
            }
        }
        return ret;
    }

    private void retriveUserFirebase() {
        firebaseHelper.downloadPhotoStorage(users.getUserName());
        firebaseHelper.retriveDataLocation(users.getUserName());
    }

    private void addUserFirebase() {
        ArrayList<String> arLoc = new ArrayList<>();
        arLoc.add("");
        Users u = new Users();
        u.setUserName(users.getUserName());
        u.setLogin("");
        u.setLocation(arLoc);
        u.setPassword("");
        firebaseHelper.addUser(u);
        if (users.getPhoto() != null) {
            firebaseHelper.loadPhotoStorage(users.getUserName(), users.getPhoto());
        }
    }

    private class LoginFacebookTasck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            firebaseHelper.retrivDataUser();
            FirebaseHelper.arrayListLocation.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firebaseHelper.sortDataUser();
            retrivUserArray = FirebaseHelper.arrayListUser;
            if (retrivActiveUser(users.getUserName())) {
                retriveUserFirebase();
            } else {
                addUserFirebase();
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            users.setLocation(FirebaseHelper.arrayListLocation);
            onMainFinishedListener.setUserFasebook(users);
        }
    }


}
