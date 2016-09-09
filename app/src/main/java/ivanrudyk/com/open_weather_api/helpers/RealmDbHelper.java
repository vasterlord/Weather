package ivanrudyk.com.open_weather_api.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.exceptions.RealmMigrationNeededException;
import ivanrudyk.com.open_weather_api.database.RealmLocation;
import ivanrudyk.com.open_weather_api.database.RealmModelUser;
import ivanrudyk.com.open_weather_api.model.ModelLocation;
import ivanrudyk.com.open_weather_api.model.ModelUser;

/**
 * Created by Ivan on 11.08.2016.
 */
public class RealmDbHelper {

    private static final String KEY = "keyprf";
    public static final String PREF_FILE_NAME = "prefname";
    public void saveUserToRealm(ModelUser user, Context context) {
        Realm realm = null;
        Log.e("TAG", "----------ADDD");
        try {
            realm = Realm.getInstance(context);

        } catch (RealmMigrationNeededException r) {
            Realm.deleteRealmFile(context);
            realm = Realm.getInstance(context);
        }
        if(user.getUserName()!=null) {
            realm.beginTransaction();
            RealmModelUser person = realm.createObject(RealmModelUser.class);

            if (user.getPhoto()!=null) {
                person.setByteArray(encodeTobase64(user.getPhoto()));
            }
            person.setUserName(user.getUserName());
            if (user.getEmailAdress()!=null) {
                person.setLogin(user.getEmailAdress());
            }
            if(user.getLocation()!=null){
                for (int loc= 0 ; loc<FirebaseHelper.modelLocation.getLocation().size(); loc++){
                    RealmLocation realmLocation = new RealmLocation();
                    realmLocation.setLocation(FirebaseHelper.modelLocation.getLocation().get(loc));
                    person.getRealmLocationList().add(realmLocation);
                }
            }
            realm.commitTransaction();
        }
    }

    public ModelUser retriveUserFromRealm(Context context) {
        ArrayList<String> arrLoc = new ArrayList();
        ArrayHelper arrayHelper = new ArrayHelper(context);
        ModelUser users = new ModelUser();
        ModelLocation modelLocation = new ModelLocation();
        Realm realm = Realm.getInstance(context);
        RealmModelUser realmModelUser = realm.where(RealmModelUser.class).findFirst();
        if (realmModelUser != null) {
            users.setPhoto(decodeBase64(realmModelUser.getByteArray()));
            users.setEmailAdress(realmModelUser.getLogin());
            users.setUserName(realmModelUser.getUserName());
            if(realmModelUser.getRealmLocationList().size()>0) {
                for (int loc = 0; loc < realmModelUser.getRealmLocationList().size(); loc++) {
                    RealmLocation realmLocation = new RealmLocation();
                    realmLocation = realmModelUser.getRealmLocationList().get(loc);
                    arrLoc.add(realmLocation.getLocation());
                    modelLocation.setLocation(arrLoc);
                    users.setLocation(modelLocation);
                }
            }
//            users.setLocation(arrayHelper.getArray(KEY));
        }
        return users;
    }

    public void deleteUserFromRealm(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.where(RealmModelUser.class).findAll().clear();
//        RealmQuery query = realm.where(RealmModelUser.class);
//        RealmResults results = query.findAll();
//
//        results.removeLast();
       Log.e("TAG", "----------DELETE");
        realm.commitTransaction();
    }

    public static byte[] encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static Bitmap decodeBase64(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
