package com.example.recipesapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Firebase.FireStorage;
import com.example.recipesapp.Listeners.CallBack_ImageUpload;
import com.example.recipesapp.Objects.User;
import com.example.recipesapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    // Firebase
    private final DataManager dataManager = DataManager.getInstance();
    private FireStorage fireStorage ;
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();

    // Attributes
    private Button sign_up_BTN_sign;
    private EditText sign_upf_EDT_first_name;
    private EditText sign_upf_EDT_last_name;

    // Profile Picture
    private FloatingActionButton signup_FAB_profile_edit;
    private ShapeableImageView sign_up_IMG_profile;
    private User tempUser;
    private String uriImg;
    private String urlImg;

    // TODO: 28/06/2022 Add Validator
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fireStorage = FireStorage.getInstance();
        fireStorage.setCallBack_imageUpload(callBack_Image_upload);

        findViews();
        initButtons();
    }

    private void initButtons() {

        // The Sign In Button -> SAVE
        sign_up_BTN_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: 28/06/2022 Add validators
                if (!sign_upf_EDT_first_name.getText().toString().isEmpty() && !sign_upf_EDT_last_name.getText().toString().isEmpty()) {
                    String userID = dataManager.getFirebaseAuth().getCurrentUser().getUid();
                    String userFirstName = sign_upf_EDT_first_name.getText().toString();
                    String userLastName = sign_upf_EDT_last_name.getText().toString();
                    String userPhoneNumber = dataManager.getFirebaseAuth().getCurrentUser().getPhoneNumber();
                    //Create a temp user from the data (after getting the photo insert to data base)
                    tempUser = new User(userID, userFirstName, userLastName, userPhoneNumber);

                    if (urlImg != null)
                        tempUser.setImg(urlImg);

                    //Save the new temp User to database
                    dataManager.setCurrentUser(tempUser);
                    dataManager.currentUserChangeListener();
                    saveUserToDatabase(tempUser);

                } else { //the fields are Empty
                    Toast.makeText(SignUpActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        // Pick Image from Gallery or Take Picture
        signup_FAB_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(SignUpActivity.this)
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .crop(1f, 1f)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }



    private void saveUserToDatabase(User tempUser) {
        //Save to User List
        DatabaseReference ref = dataManager.getRealTimeDB().getReference(Keys.KEY_USERS).child(tempUser.getUid());
        ref.child(Keys.KEY_USER_ID).setValue(tempUser.getUid());
        ref.child(Keys.KEY_USER_FIRST_NAME).setValue(tempUser.getFirstName());
        ref.child(Keys.KEY_USER_LAST_NAME).setValue(tempUser.getLastName());
        ref.child(Keys.KEY_USER_PHONE).setValue(tempUser.getPhoneNumber());
        ref.child(Keys.KEY_USER_IMG).setValue(tempUser.getImg());
        ref.child(Keys.KEY_USER_GROUPS_IDS).setValue(tempUser.getGroupsIds());
//        Group group = new Group("s","s","s");
//        HashMap<String,String> h = new HashMap<>();
//        h.put(group.getIdGroup().toString(),group.getName());
//        tempUser.setGroupsIds(h);

        //save to get Uid By phone number List
        //phone_to_uid --> PhoneNumber : Uid
        DatabaseReference refPhone = dataManager.getRealTimeDB().getReference(Keys.KEY_PHONE_TO_UID).child(tempUser.getPhoneNumber());
        refPhone.setValue(tempUser.getUid());

        dataManager.setCurrentUser(tempUser);
        //Move to the Next Activity in the application
        nextActivity();

    }

    private void nextActivity() {
        startActivity(new Intent(SignUpActivity.this , MainActivity.class));
        finish();
    }

    private void findViews() {
        sign_up_BTN_sign = findViewById(R.id.verify_BTN_verify);
        sign_upf_EDT_first_name = findViewById(R.id.sign_upf_EDT_first_name);
        sign_upf_EDT_last_name = findViewById(R.id.sign_upf_EDT_last_name);
        signup_FAB_profile_edit = findViewById(R.id.signup_FAB_profile_edit);
        sign_up_IMG_profile = findViewById(R.id.sign_up_IMG_profile);
    }


    // TODO: 28/06/2022 Check if i can do it otherwise

    /**
     * This function Handel the Date Picker Result
     * The image will be store in the Firebase Storage.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri resultUri = data.getData();
        sign_up_IMG_profile.setImageURI(resultUri);
        fireStorage.uploadImgToStorage(resultUri,dataManager.getFirebaseAuth().getUid(), Keys.KEY_PROFILE_PICTURES,this);
    }

    CallBack_ImageUpload callBack_Image_upload =new CallBack_ImageUpload() {
        @Override
        public void imageUrlAvailable(String url, Activity activity) {
            urlImg=url;
        }
    };
}