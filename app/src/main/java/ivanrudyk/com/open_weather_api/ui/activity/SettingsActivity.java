package ivanrudyk.com.open_weather_api.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.helpers.RealmDbHelper;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIterator;
import ivanrudyk.com.open_weather_api.iterator.activity.MainIteratorImlement;
import ivanrudyk.com.open_weather_api.model.ModelUser;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    RealmDbHelper dbHelper = new RealmDbHelper();
    MainIterator mainIterator;
    ModelUser users = new ModelUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mainIterator = new MainIteratorImlement();
        Intent intent = getIntent();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }
}
