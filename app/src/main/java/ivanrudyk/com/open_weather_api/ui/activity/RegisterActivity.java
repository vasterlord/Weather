package ivanrudyk.com.open_weather_api.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helpers.PhotoHelper;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.model.ModelLocation;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.presenter.activity.RegisterPresenter;
import ivanrudyk.com.open_weather_api.presenter.activity.RegisterPresenterImplement;

public class RegisterActivity extends AppCompatActivity implements RegisterView, View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    EditText etLoginRegister, etPasswordRegister, etUserName, etCity, etConfirmPassword;
    ImageView ivCamera, ivGalary, ivOkRegister, ivCancelRegister, ivRegisterPhotoUser;
    ProgressBar progressBarRegister;

    CameraPhoto cameraPhoto;
    private final int CAMERA_REQUEST = 13323;
    private Bitmap photoLoad;
    GalleryPhoto galleryPhoto;
    private final int GALLERY_REQEST = 34623;

    RegisterPresenter presenter;
    ModelUser userAdd = new ModelUser();
    ModelLocation modelLocation = new ModelLocation();
    ArrayList<String> locationStart = new ArrayList();
    String [] passwordsAndCity;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
        setContentView(R.layout.activity_reg_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        initializeComponentView();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.e("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.e("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        presenter = new RegisterPresenterImplement(this);
        ivOkRegister.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
        ivCancelRegister.setOnClickListener(this);
        ivGalary.setOnClickListener(this);
    }

    private void initializeComponentView() {
        etLoginRegister = (EditText) findViewById(R.id.et_register_login);
        etPasswordRegister = (EditText) findViewById(R.id.et_register_password);
        etUserName = (EditText) findViewById(R.id.et_register_user_name);
        etCity = (EditText) findViewById(R.id.et_city_register);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivGalary = (ImageView) findViewById(R.id.iv_galery);
        ivOkRegister = (ImageView) findViewById(R.id.iv_ok_register);
        ivCancelRegister = (ImageView) findViewById(R.id.iv_cancel_register);
        ivRegisterPhotoUser = (ImageView) findViewById(R.id.ivRegisterPhotoUser);
        progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerOk(String login, String password) {
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ok_register:
                locationStart.clear();
                locationStart.add(etCity.getText().toString());
                userAdd.setEmailAdress(etLoginRegister.getText().toString());
                userAdd.setUserName(etUserName.getText().toString());
                modelLocation.setLocation(locationStart);
                userAdd.setLocation(modelLocation);
                String password = etPasswordRegister.getText().toString();
                String city = etCity.getText().toString();
                String confPass = etConfirmPassword.getText().toString();
                passwordsAndCity = new String[]{password, confPass, city};
                presenter.createAcount(userAdd, passwordsAndCity);
                progressBarRegister.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_camera:
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_galery:
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQEST);
                break;
            case R.id.iv_cancel_register:
                NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(50, 80).getBitmap();
                    photoLoad = bitmap;
                } catch (FileNotFoundException e) {

                }

            } else if (requestCode == GALLERY_REQEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(50, 80).getBitmap();
                    photoLoad = bitmap;
                } catch (FileNotFoundException e) {

                }

            }
        }
        ivRegisterPhotoUser.setImageBitmap(PhotoHelper.getCircleMaskedBitmapUsingClip(photoLoad, 100));
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBarRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setUsernameError() {
        etUserName.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        etPasswordRegister.setError(getString(R.string.password_error));
    }

    @Override
    public void setLoginError() {
        etLoginRegister.setError(getString(R.string.login_error));
    }

    @Override
    public void setCityError() {
        etCity.setError(getString(R.string.city_error));
    }

    @Override
    public void setConfirmPasswordError(String s) {
        etConfirmPassword.setError(s);
    }

    @Override
    public void navigateToMain() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void closeWiew() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void createUserAcount() {
        registerOk(etLoginRegister.getText().toString(), etPasswordRegister.getText().toString());
        RegisterProgress registerProgress = new RegisterProgress();
        registerProgress.execute();
    }

    @Override
    public void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("UserName", userAdd.getUserName());
        intent.putExtra("EmailADress", userAdd.getEmailAdress());
        intent.putExtra("PhotoUser", RealmDbHelper.encodeTobase64(photoLoad));
        intent.putExtra("UserLocation", userAdd.getLocation().getLocation());
        setResult(RESULT_OK, intent);
        finish();
    }


    class RegisterProgress extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            presenter.addUser(userAdd, passwordsAndCity, photoLoad, mAuth.getCurrentUser().getUid());
        }
    }
}
