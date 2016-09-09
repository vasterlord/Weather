package ivanrudyk.com.open_weather_api.ui.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ivanrudyk.com.open_weather_api.R;
import ivanrudyk.com.open_weather_api.adapter.FavoritesLocationAdapter;
import ivanrudyk.com.open_weather_api.helpers.FirebaseHelper;
import ivanrudyk.com.open_weather_api.helpers.PhotoHelper;
import ivanrudyk.com.open_weather_api.model.ModelUser;
import ivanrudyk.com.open_weather_api.presenter.fragment.NavigationDraverPresenterImplement;
import ivanrudyk.com.open_weather_api.presenter.fragment.NavigatonDraverPresenter;
import ivanrudyk.com.open_weather_api.ui.activity.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 */

public class NavigationDraverFragment extends Fragment implements NavigationDraverView {

    public static final String PREF_FILE_NAME = "preffilename";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public ModelUser users = new ModelUser();
    TextView tvNavUserName, tvNavLogin;
    ImageView ivPhotoUser;
    ListView lvLocation;
    ImageView bAdd;
    Button bAddLocation;
    EditText etAddLocation;
    private ProgressBar progressBar;
    LinearLayout linearLayoutAddLoc, linearLayoutSettings;
    PhotoHelper photoHelper = new PhotoHelper();
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearndDrawer;
    private boolean mFromSavedInstanseState;
    private View containerView;
    String uid;

    private Dialog d;

    NavigatonDraverPresenter draverPresenter;


    public NavigationDraverFragment() {
    }

    public static void saveToPreferenses(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferenses(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mUserLearndDrawer = Boolean.valueOf((readFromPreferenses(getActivity(), KEY_USER_LEARNED_DRAWER, "false")));
//        if (savedInstanceState != null) {
//            mFromSavedInstanseState = true;
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_navigation_draver, container, false);
        initializeFragment(v);
        return v;
    }

    private void initializeFragment(View v) {
        ivPhotoUser = (ImageView) v.findViewById(R.id.ivPhotoUser);
        tvNavUserName = (TextView) v.findViewById(R.id.tvDrUserName);
        tvNavLogin = (TextView) v.findViewById(R.id.tvDrLogin);
        lvLocation = (ListView) v.findViewById(R.id.listViewLocation);
        bAdd = (ImageView) v.findViewById(R.id.ivAddLocation);
        linearLayoutAddLoc = (LinearLayout) v.findViewById(R.id.linLayoutAddLoc);
        linearLayoutSettings = (LinearLayout) v.findViewById(R.id.linearLayoutSettings);
        draverPresenter = new NavigationDraverPresenterImplement(this);
        linearLayoutAddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (users.getUserName() != null) {
                    d = new Dialog(getActivity());
                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    d.setContentView(R.layout.add_location_layout);
                    etAddLocation = (EditText) d.findViewById(R.id.etAddLocation);
                    bAddLocation = (Button) d.findViewById(R.id.bAddLocation);
                    progressBar = (ProgressBar) d.findViewById(R.id.progressBarAddLocation);

                    bAddLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            draverPresenter.addLocation(users, uid, etAddLocation.getText().toString());
                        }
                    });
                    d.show();
                }
            }
        });
        linearLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void arrayAdapter() {
        final FavoritesLocationAdapter locationAdapter = new FavoritesLocationAdapter(this.getContext(), users.getLocation().getLocation());
        lvLocation.setClickable(true);
        lvLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getActivity(), "" + locationAdapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
        if (users.getLocation() != null && users.getLocation().getLocation().size() > 0) {
            String temp = users.getLocation().getLocation().get(0);
                lvLocation.setAdapter(locationAdapter);

        }



    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolBar, ModelUser users, String uid) {
        this.users = users;
        this.uid = uid;
        tvNavUserName.setText(this.users.getUserName());
        tvNavLogin.setText(this.users.getEmailAdress());
        ivPhotoUser.setImageBitmap(PhotoHelper.getCircleMaskedBitmapUsingClip(this.users.getPhoto(), 60));
        arrayAdapter();


        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearndDrawer) {
                    mUserLearndDrawer = true;
                    //saveToPreferenses(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearndDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearndDrawer && !mFromSavedInstanseState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void setUpFragment() {
        FavoritesLocationAdapter locationAdapter = new FavoritesLocationAdapter(this.getContext(), FirebaseHelper.modelLocation.getLocation());
        lvLocation.setAdapter(locationAdapter);
    }

    @Override
    public void setLocationAddError(String s) {
        etAddLocation.setError(s);
    }

    @Override
    public void setDialogClosed() {
        dialogClosed();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setUser(ModelUser user) {
        this.users = user;
    }

    private void dialogClosed() {
        d.cancel();
    }
}
