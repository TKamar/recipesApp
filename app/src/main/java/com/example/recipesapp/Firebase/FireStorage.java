package com.example.recipesapp.Firebase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.recipesapp.Listeners.CallBack_ImageUpload;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireStorage {

    private static FireStorage single_instance; //singleton
    public StorageReference storageReferenceImg;
    private Context context;
    private FirebaseStorage myStorage;
    private CallBack_ImageUpload callBack_Image_upload;

    private FireStorage(Context context) {
        myStorage = FirebaseStorage.getInstance();
        this.context = context;
    }

    public static FireStorage getInstance() {
        return single_instance;
    }

    public static FireStorage initHelper(Context context) {
        if (single_instance == null) {
            single_instance = new FireStorage(context);
        }
        return single_instance;
    }

    public void setCallBack_imageUpload(CallBack_ImageUpload callBack_Image_upload) {
        this.callBack_Image_upload = callBack_Image_upload;
    }

    public void uploadImgToStorage(Uri uri, String id, String key, Activity activity) {

       //key - in witch list i wand to save in witch child id
        storageReferenceImg = myStorage.getReference().child(key).child(id);

        if (uri != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog= new ProgressDialog(activity);
            progressDialog.setTitle("Uploading image");
            progressDialog.show();

            storageReferenceImg.putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Image uploaded successfully -> Dismiss dialog
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Image Uploaded!", Toast.LENGTH_SHORT).show();

                                storageReferenceImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        callBack_Image_upload.imageUrlAvailable(uri.toString(), activity);
                                    }
                                });
                            }
                        }
                    })
                    // On failure - didn't upload the image
                     .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(activity,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading- percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage(
                                            "Uploaded "+ (int) progress + "%");
                                }
                            });
        }
    }


    public StorageReference getReference() {
        return storageReferenceImg;
    }
}
