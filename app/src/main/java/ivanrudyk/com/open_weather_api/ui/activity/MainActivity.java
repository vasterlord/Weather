package ivanrudyk.com.open_weather_api.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helpers.ArrayHelper;
import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.helpers.Helper;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.model.ModelLocation;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenter;
import ivanrudyk.com.open_weather_api.presenter.activity.MainPresenterImplement;
import ivanrudyk.com.open_weather_api.ui.fragment.NavigationDraverFragment;

class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private static final String KEY = "keyprf";
    public static final String TAG = MainActivity.class.getSimpleName();

    Toolbar toolbar;
    ImageView imOk;
    TextView etRegister, tv;
    EditText etLogin, etPassword;
    ProgressBar progressBar;
    LoginButton loginButtonFacebook;
    ImageButton ibLogin;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView descriptonField;
    ImageView iconView;
    Button btnCurrentPlace;
    Button btnCityChanged;
    CheckBox checkBoxShowPassword;
    ImageView mRefreshImageView;
    ProgressBar mProgressBar;

    Handler handler;

    private final int REGISTER_REQUEST = 34323;

    String uid = "";

    double[] coord = new double[2];

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    MainPresenter presenter;
    ModelUser users = new ModelUser();
    ModelLocation modelLocation = new ModelLocation();

    public MainActivity() {
        handler = new Handler();
    }

    RealmDbHelper dbHelper = new RealmDbHelper();
    Profile profile;
    int inputType;

    private Dialog d;

    FirebaseHelper firebaseHelper = new FirebaseHelper();

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.e(TAG, "SsssssignInnn");

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setVisibleLoginItem();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == REGISTER_REQUEST) {
                users.setUserName(data.getStringExtra("UserName"));
                users.setEmailAdress(data.getStringExtra("EmailADress"));
                users.setPhoto(RealmDbHelper.decodeBase64(data.getByteArrayExtra("PhotoUser")));
                ArrayList<String> loc = data.getStringArrayListExtra("UserLocation");
                modelLocation.setLocation(loc);
                users.setLocation(modelLocation);
                onCreareToolBar();
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RealmDbHelper dbHelper = new RealmDbHelper();
        ModelUser u = new ModelUser();
        u = dbHelper.retriveUserFromRealm(this);
        if (u.getUserName() != null && u.getUserName().length() > 0) {
            if (users.getUserName() != null && users.getUserName().length() > 0) {
                dbHelper.deleteUserFromRealm(this);
            }
        }
        dbHelper.saveUserToRealm(users, this);
        ArrayHelper arrayHelper = new ArrayHelper(this);
        arrayHelper.saveArray(KEY, (ArrayList<String>) users.getLocation().getLocation());
    }

    private void loginFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e("LOGIN", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOGIN", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    uid = user.getUid();
                    presenter.loginFacebook(profile, uid, getApplicationContext());
                    Log.e("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.e("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        detailsField = (TextView) findViewById(R.id.details_field);
        currentTemperatureField = (TextView) findViewById(R.id.current_temperature_field);
        descriptonField = (TextView) findViewById(R.id.decription_field);
        iconView = (ImageView) findViewById(R.id.icon_Image);
        btnCurrentPlace = (Button) findViewById(R.id.currentLoc);
        btnCityChanged = (Button) findViewById(R.id.cityLoc);
//        coord = mHelper.CoordTracker(getApplicationContext());
        Log.e("TESTING", "Lat = : " + coord[0]);
        Log.e("TESTING", "Lon = " + coord[1]);

        // city = "";

        btnCurrentPlace.setOnClickListener(this);
        btnCityChanged.setOnClickListener(this);
        mRefreshImageView.setOnClickListener(this);

        users = dbHelper.retriveUserFromRealm(this);

        presenter = new MainPresenterImplement(this);

        ibLogin = (ImageButton) findViewById(R.id.ibLogin);
        onCreareToolBar();
        ibLogin.setOnClickListener(this);
        mCallbackManager = new CallbackManager.Factory().create();

        profile = Profile.getCurrentProfile();
        InitializeDialog();
        LoginProgress loginProgress = new LoginProgress();
        loginProgress.execute();
    }

    private void InitializeDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.login_layout);
        loginButtonFacebook = (LoginButton) d.findViewById(R.id.login_button);
        etLogin = (EditText) d.findViewById(R.id.etLogin);
        etPassword = (EditText) d.findViewById(R.id.etPassword);
        checkBoxShowPassword = (CheckBox) d.findViewById(R.id.checkBoxShowPassword);
        inputType = etPassword.getInputType();
//        etPassword.setInputType(InputType.TEXT);
    }


    private void onCreareToolBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        onCreateNavigationDraver();
    }

    private void onCreateNavigationDraver() {
        NavigationDraverFragment draverFragment = (NavigationDraverFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_draver);
        draverFragment.setUp(R.id.fragment_navigation_draver, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, users, uid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogLogin() {
        loginButtonFacebook.setReadPermissions("email", "public_profile");
        loginButtonFacebook.registerCallback(mCallbackManager, mCallback);
        progressBar = (ProgressBar) d.findViewById(R.id.progressBarLogin);
        imOk = (ImageView) d.findViewById(R.id.iv_ok_login);
        etRegister = (TextView) d.findViewById(R.id.etRegister);
        etRegister.setSelectAllOnFocus(true);

        checkBoxShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxShowPassword.isChecked()) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(inputType);
                }
            }
        });
        etRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.cancel();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REGISTER_REQUEST);
            }
        });

        imOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Helper.isNetworkAvailable(getApplicationContext())) {
                    d.cancel();
                    Toast.makeText(getApplicationContext(),
                            "No internet access", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    presenter.retriveUserFirebase(etLogin.getText().toString(), etPassword.getText().toString(), getApplicationContext());
                }
            }
        });
        d.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibLogin:
                if (this.users.getUserName() != null) {
                    showDialogQuit();
                } else {
                    showDialogLogin();
                }

                break;
            case R.id.currentLoc:
                break;
            case R.id.cityLoc:
                break;
            default:
                break;
        }
    }

    private void showDialogQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        final TextView input = new TextView(this);
        input.setText("You really want to continue?");
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String temp = users.getEmailAdress();
                FirebaseAuth.getInstance().signOut();
                if (users.getEmailAdress() == null || temp.length() == 0 || temp.equals("")) {
                    loginButtonFacebook.performClick();
                }
                ModelUser userQuit = new ModelUser();
                ModelLocation locationQuit = new ModelLocation();
                userQuit.setLocation(locationQuit);
                users = userQuit;
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                dbHelper.deleteUserFromRealm(getApplication());
                onCreareToolBar();
                setVisibleLoginItem();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void dialogClosed() {
        d.cancel();
    }

    @Override
    public void setLoginError(String value) {
        etLogin.setError(value);
    }

    @Override
    public void setPasswordError() {
        etPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void toastShow(String value) {

    }

    @Override
    public void setUser(ModelUser activeUser) {
        this.users = FirebaseHelper.modelUser;
        this.users.setLocation(FirebaseHelper.modelLocation);
        etLogin.setText("");
        etPassword.setText("");
        mProgressBar.setVisibility(View.INVISIBLE);
        ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
        onCreareToolBar();
    }

    @Override
    public void setViseibleLogin() {
        setVisibleLoginItem();
    }

    @Override
    public void setDialogClosed() {
        dialogClosed();
    }

    @Override
    public void loginUserFirebase(String userLogin, String userPassword) {
        loginFirebase(userLogin, userPassword);
        LoginProgress loginProgress = new LoginProgress();
        loginProgress.execute();
    }

    private void setVisibleLoginItem() {
        if (users.getUserName() == null) {
            ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_open_white_24dp));
        } else {
            ibLogin.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_outline_white_24dp));
        }
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private class LoginProgress extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mAuth.getCurrentUser() != null) {
                firebaseHelper.retrivDataUser(mAuth.getCurrentUser().getUid());
                firebaseHelper.downloadPhotoStorage(mAuth.getCurrentUser().getUid());
                uid = mAuth.getCurrentUser().getUid();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.INVISIBLE);
            super.onPostExecute(aVoid);
            users = FirebaseHelper.modelUser;
            users.setPhoto(FirebaseHelper.photoDownload);
            ModelUser u = new ModelUser();
            u = dbHelper.retriveUserFromRealm(getApplicationContext());
            if (u.getUserName() != null && u.getUserName().length() > 0) {
                if (users.getUserName() != null && users.getUserName().length() > 0) {
                    dbHelper.deleteUserFromRealm(getApplicationContext());
                }
            }
            dbHelper.saveUserToRealm(users, getApplicationContext());
            onCreareToolBar();
            d.cancel();
        }
    }


}