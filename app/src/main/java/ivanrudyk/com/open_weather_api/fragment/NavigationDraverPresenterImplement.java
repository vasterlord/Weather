package ivanrudyk.com.open_weather_api.fragment;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.helper.FirebaseHelper;
import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 10.08.2016.
 */
public class NavigationDraverPresenterImplement implements NavigatonDraverPresenter, NavigationDraverIterator.OnDraverFinishedListener {

    ArrayList<String> listLocation = new ArrayList();
    Users user = new Users();

    private NavigationDraverView draverView;
    private NavigationDraverIterator draverIterator;
    private FirebaseHelper helper = new FirebaseHelper();

    public NavigationDraverPresenterImplement(NavigationDraverView dreverView) {
        this.draverView = dreverView;
        this.draverIterator = new NavigationDraverIteratorImlement();
    }

    @Override
    public void addLocation(Users users, String newLocation) {
        draverIterator.addLocation(newLocation, this);
        this.user = users;
    }


    @Override
    public void onLocatoinAddError() {
        draverView.setLocationAddError("location will not be empty");
    }

    @Override
    public void onSuccess(String newLocation) {
        listLocation.clear();
        listLocation.addAll(user.getLocation());
        listLocation.add(newLocation);
        ImplementAddLocation implementAddLocation = new ImplementAddLocation();
        implementAddLocation.execute();
    }

    class ImplementAddLocation extends AsyncTask<String, Void, Void> {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("user");

        private void addDataLocation() {
                DatabaseReference refLocation = ref.child(user.getUserName().toString());
                DatabaseReference locationRef = refLocation.child("location");
                locationRef.setValue(listLocation);
        }

        @Override
        protected void onPreExecute() {
            addDataLocation();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            FirebaseHelper.arrayListLocation.clear();
            helper.retriveDataLocation(user.getUserName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            user.setLocation(FirebaseHelper.arrayListLocation);
            draverView.setUpFragment();
            draverView.setDialogClosed();
        }
    }
}
