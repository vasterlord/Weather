package ivanrudyk.com.open_weather_api.presenter.fragment;

import android.os.AsyncTask;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.iterator.fragment.NavigationDraverIterator;
import ivanrudyk.com.open_weather_api.iterator.fragment.NavigationDraverIteratorImlement;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.ui.fragment.NavigationDraverView;

/**
 * Created by Ivan on 10.08.2016.
 */
public class NavigationDraverPresenterImplement implements NavigatonDraverPresenter, NavigationDraverIterator.OnDraverFinishedListener {

    ArrayList<String> listLocation = new ArrayList();
    ModelUser user = new ModelUser();
    String uid;

    private NavigationDraverView draverView;
    private NavigationDraverIterator draverIterator;
    private FirebaseHelper helper = new FirebaseHelper();

    public NavigationDraverPresenterImplement(NavigationDraverView dreverView) {
        this.draverView = dreverView;
        this.draverIterator = new NavigationDraverIteratorImlement();
    }

    @Override
    public void addLocation(ModelUser users, String uid, String newLocation) {
        draverView.showProgress();
        draverIterator.addLocation(newLocation, this);
        this.user = users;
        this.uid = uid;
    }


    @Override
    public void onLocatoinAddError() {
        draverView.setLocationAddError("location will not be empty");
        draverView.hideProgress();
    }

    @Override
    public void onSuccess(String newLocation) {
        helper.addDataLocation(user.getUserName(), uid, newLocation);
        ImplementAddLocation implementAddLocation = new ImplementAddLocation();
        implementAddLocation.execute();
    }

    class ImplementAddLocation extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            helper.retriveDataLocation(user.getUserName(), uid);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            draverView.hideProgress();

            draverView.setUpFragment();
            draverView.setDialogClosed();
            draverView.setUpFragment();
        }
    }
}
