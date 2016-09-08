package ivanrudyk.com.open_weather_api.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ivanrudyk.com.open_weather_api.model.ModelLocation;
import ivanrudyk.com.open_weather_api.model.ModelUser;

/**
 * Created by Ivan on 03.08.2016.
 */
public class FirebaseHelper {


    public static ModelUser modelUser = new ModelUser();
    public static ModelLocation modelLocation = new ModelLocation();
    public static Bitmap photoDownload;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://justweather-92b19.appspot.com");
    private DatabaseReference refLocation;

    private ArrayList<String> arrayListLocation = new ArrayList<>();


    public void retriveDataLocation(String userName, final String uid) {
        DatabaseReference refLocation = database.child("Users").child(uid).child(userName).child("location");
        refLocation.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchDataLocatoin(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchDataLocatoin(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchDataLocatoin(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                fetchDataLocatoin(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchDataLocatoin(DataSnapshot dataSnapshot) {
        arrayListLocation.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arrayListLocation.add(ds.getValue().toString());
        }
        modelLocation.setLocation(arrayListLocation);
    }

    public void addUser(ModelUser modelUser, String userId) {
        DatabaseReference nameUser = database.child("Users").child(userId).child(modelUser.getUserName());
        nameUser.setValue(modelUser);
    }

    public void retrivDataUser(final String uid) {
        DatabaseReference nameChild = database.child("Users").child(uid);

        nameChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                modelUser = dataSnapshot.getValue(ModelUser.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                modelUser = dataSnapshot.getValue(ModelUser.class);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                modelUser = dataSnapshot.getValue(ModelUser.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                modelUser = dataSnapshot.getValue(ModelUser.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadPhotoStorage(String uid, Bitmap photo) {
        StorageReference userRef = storageRef.child("UsersPhoto").child(uid + "/");
        StorageReference userImagesRef = userRef.child("photo.jpg");

        Bitmap bitmap = photo;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    public void downloadPhotoStorage(String userName) {
        StorageReference userRef = storageRef.child("UsersPhoto/").child(userName + "/");
        StorageReference userImagesRef = userRef.child("photo.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        userImagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                photoDownload = photo;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void addDataLocation(String userName, final String uid, String newLocation) {

        retriveDataLocation(userName, uid);
        refLocation = database.child("Users").child(uid).child(userName).child("location");
        ImplementAddLocation implementAddLocation = new ImplementAddLocation();
        implementAddLocation.execute(newLocation);
    }

    class ImplementAddLocation extends AsyncTask<String, Void, Void> {

        ArrayList<String> listLocation = new ArrayList();
        ModelLocation mlocation = new ModelLocation();

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
            listLocation.clear();
            listLocation.addAll(modelLocation.getLocation());
            listLocation.add(strings[0]);
            mlocation.setLocation(listLocation);
            refLocation.setValue(mlocation);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
