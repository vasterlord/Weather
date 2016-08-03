package ivanrudyk.com.open_weather_api.helper;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ivanrudyk.com.open_weather_api.model_user.Users;

/**
 * Created by Ivan on 03.08.2016.
 */
public class FirebaseHelper {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("user");
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public  void loadPhotoStorage(String userName, Bitmap photo) {

        StorageReference storageRef = storage.getReferenceFromUrl("gs://justweather-92b19.appspot.com/");
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

    public void addUser(Users user){
        DatabaseReference nameRef = ref.child(user.getUserName());
        nameRef.setValue(user);
        DatabaseReference nameRefLocation = nameRef.child("location");
        nameRefLocation.setValue(user.getLocation());
    }

}
