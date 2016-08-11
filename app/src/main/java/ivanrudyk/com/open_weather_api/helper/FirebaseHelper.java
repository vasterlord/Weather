package ivanrudyk.com.open_weather_api.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public class FirebaseHelper {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("user");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://justweather-92b19.appspot.com/");

    public static ArrayList<String> arrayListUserData = new ArrayList();
    public static ArrayList<Users> arrayListUser = new ArrayList();
    public static ArrayList<String> arrayListLocation = new ArrayList();

    public static Bitmap photoDownload;

    public void loadPhotoStorage(String userName, Bitmap photo) {
        StorageReference userRef = storageRef.child(userName + "/");
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

    public void addUser(Users user) {
        DatabaseReference nameRef = ref.child(user.getUserName());
        nameRef.setValue(user);
        DatabaseReference nameRefLocation = nameRef.child("location");
        nameRefLocation.setValue(user.getLocation());
    }

    public void retrivDataUser() {
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchData(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arrayListUserData.add(ds.getValue().toString());
        }
    }

    public void sortDataUser() {
        int userNumber = 0;

        while (userNumber < arrayListUserData.size()) {
            Users users = new Users();
            users.setLogin(arrayListUserData.get(userNumber + 1).toString());
            users.setPassword(arrayListUserData.get(userNumber + 2).toString());
            users.setUserName(arrayListUserData.get(userNumber + 3).toString());
            arrayListUser.add(users);
            userNumber += 4;
        }
    }

    private void fetchDataLocatoin(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            arrayListLocation.add(ds.getValue().toString());
        }
    }

    public void retriveDataLocation(String userName) {
        DatabaseReference refLocation = ref.child(userName);
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

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void downloadPhotoStorage(String userName) {
        StorageReference userRef = storageRef.child(userName + "/");
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

}
