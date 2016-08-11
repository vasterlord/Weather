package ivanrudyk.com.open_weather_api.activity.main;

import android.os.AsyncTask;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helper.FirebaseHelper;
import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public class MainPresenterImplement implements MainPresenter, MainIterator.OnMainFinishedListener {
    private MainView mainView;
    private MainIterator iterator;



    Users activeUser = new Users();
    public static Users userActive = new Users("vy");


    FirebaseHelper firebaseHelper = new FirebaseHelper();

    ArrayList<Users> retrivUserArray = new ArrayList<>();
    ArrayList<String> retrivData = new ArrayList<>();

    public MainPresenterImplement(MainView mainView) {
        this.mainView = mainView;
        this.iterator = new MainIteratorImlement();
    }


    @Override
    public void retriveUserFirebase(String userLogin, String userPassword) {
        mainView.showProgress();
        iterator.login(userLogin, userPassword, this);
    }

    @Override
    public void onLoginError() {
        if (mainView != null) {
            mainView.setLoginError("login is empty");
            mainView.hideProgress();
        }
    }

    @Override
    public void onPasswordError() {
        if (mainView != null) {
            mainView.setPasswordError();
            mainView.hideProgress();
        }
    }

    @Override
    public void onSuccess(String userLogin, String userPassword) {
        RetriveProgress progress = new RetriveProgress();
        progress.execute(userLogin, userPassword);
    }

    class RetriveProgress extends AsyncTask<String, Void, Void> {

        public void retrivActiveUser(String login, String password) {
            for (int userNumber = 0; userNumber < retrivUserArray.size(); userNumber++) {
                if (login.equals(retrivUserArray.get(userNumber).getLogin()) &&
                        password.equals(retrivUserArray.get(userNumber).getPassword())) {
                    activeUser.setUserName(retrivUserArray.get(userNumber).getUserName());
                    activeUser.setLogin(retrivUserArray.get(userNumber).getLogin());
                }
            }
        }

        @Override
        protected void onPreExecute() {
            firebaseHelper.retrivDataUser();
            FirebaseHelper.arrayListLocation.clear();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firebaseHelper.sortDataUser();
            retrivUserArray = FirebaseHelper.arrayListUser;
            retrivActiveUser(strings[0], strings[1]);
            if (activeUser.getUserName()!=null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                firebaseHelper.downloadPhotoStorage(activeUser.getUserName());
                firebaseHelper.retriveDataLocation(activeUser.getUserName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                mainView.toastShow("Incorect login or password");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            activeUser.setPhoto(FirebaseHelper.photoDownload);
            activeUser.setLocation(FirebaseHelper.arrayListLocation);
            mainView.hideProgress();
            mainView.setUser(activeUser);
            mainView.setViseibleLogin();
            mainView.setDialogClosed();
        }
    }

}
