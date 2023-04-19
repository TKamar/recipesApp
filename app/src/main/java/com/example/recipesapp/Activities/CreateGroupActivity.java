package com.example.recipesapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Firebase.FireStorage;
import com.example.recipesapp.Listeners.CallBack_ImageUpload;
import com.example.recipesapp.Objects.Group;
import com.example.recipesapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    //Firebase
    private DataManager dataManager = DataManager.getInstance();
    private FireStorage fireStorage;

    //Views
    private ShapeableImageView create_group_IMG_img;
    private FloatingActionButton create_group_FAB_edit;
    private TextInputEditText create_group_EDT_name;
    private TextInputEditText create_group_EDT_description;
    private Button create_group_BTN_save;
    private AppCompatImageButton create_group_BTN_back;

    //Attributes
    private String urlImg;
    //CallBack
    CallBack_ImageUpload callBack_Image_upload = new CallBack_ImageUpload() {
        @Override
        public void imageUrlAvailable(String url, Activity activity) {
            urlImg = url;
        }
    };
    private Group tempGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        tempGroup = new Group("","",dataManager.getCurrentUser().getUid());
        dataManager.setCurrentIdGroup(tempGroup.getIdGroup());
        Log.d("creat Group:", dataManager.getCurrentIdGroup());
        findViews();
        //Init fireStorage
        fireStorage = FireStorage.getInstance();
        fireStorage.setCallBack_imageUpload(callBack_Image_upload);
        initButtons();

    }

    private void initButtons() {
        //Save to database the new Group
        create_group_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewGroup();
            }
        });

        //Insert Group Photo from gallery with image picker
        create_group_FAB_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePick();
            }
        });

        //Back button clicked -> finish the activity
        create_group_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findViews() {
        create_group_IMG_img = findViewById(R.id.create_group_IMG_img);
        create_group_FAB_edit = findViewById(R.id.create_group_FAB_edit);
        create_group_EDT_name = findViewById(R.id.create_group_EDT_name);
        create_group_EDT_description = findViewById(R.id.create_group_EDT_description);
        create_group_BTN_save = findViewById(R.id.verify_BTN_verify);
        create_group_BTN_back = findViewById(R.id.create_group_BTN_back);
    }

    //Image Recipe Picker From Gallery
    private void imagePick() {
        ImagePicker.with(CreateGroupActivity.this)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    /**
     * This function Handel the Image Picker Result
     * The image will be store in the Firebase Storage.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri resultUri = data.getData();
        //Change the view to the new image
        create_group_IMG_img.setImageURI(resultUri);
        fireStorage.uploadImgToStorage(resultUri, dataManager.getCurrentIdGroup(), Keys.KEY_GROUP_PICTURES, CreateGroupActivity.this);

    }

    //create  the new Group to database
    private void createNewGroup() {

        //Temp Group Creation
        tempGroup.setGroupCreator(dataManager.getCurrentUser().getUid());
        tempGroup.getUsersIds().put(dataManager.getCurrentUser().getUid(),true);
        //current Group save on data manager and creator (IDS)
        dataManager.setCurrentIdGroup(tempGroup.getIdGroup());
        dataManager.setCurrentGroupCreator(dataManager.getCurrentUser().getUid());

        //Handel Image Picker & upload image to the Storage  and save the url
        if (urlImg != null)
            tempGroup.setImgGroup(urlImg);
//        else {
//            Toast.makeText(this, "U need to upload IMG ", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // TODO: 29/06/2022 Add Validator 
        //Get Group Name
        if (create_group_EDT_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Group Name can't be empty ", Toast.LENGTH_SHORT).show();
            return;
        } else
            tempGroup.setName(create_group_EDT_name.getText().toString());

        //get the description (can be Empty)
        tempGroup.setDescription(create_group_EDT_description.getText().toString());

        //Save and move to the next Activity
        SaveNewGroupToDatabase(tempGroup);
    }

    private void SaveNewGroupToDatabase(Group tempGroup) {
        //Save tempGroup to Recipe List
        DatabaseReference ref = dataManager.groupsListReference();
        ref.child(tempGroup.getIdGroup()).setValue(tempGroup.toMap());
        //        ref.child(Keys.KEY_GROUP_ID).setValue(tempGroup.getIdGroup());
//        ref.child(Keys.KEY_GROUP_NAME).setValue(tempGroup.getName());
//        ref.child(Keys.KEY_GROUP_DESCRIPTION).setValue(tempGroup.getDescription());
//        ref.child(Keys.KEY_GROUP_IMG).setValue(tempGroup.getImgGroup());
//        ref.child(Keys.KEY_GROUP_CREATOR).setValue(tempGroup.getGroupCreator());
//        ref.child(Keys.KEY_GROUP_RECIPES_LIST).setValue(tempGroup.getRecipesIds());
//        ref.child(Keys.KEY_GROUP_USERS_LIST).setValue(tempGroup.getUsersIds());

        /**
         * After The Save - need to add this Group to the User (creator) Group List
         * Add the Group to Current User
         */
        if(dataManager.getCurrentUser().getGroupsIds() == null){
            dataManager.getCurrentUser().setGroupsIds(new HashMap<>()) ;
        }
        dataManager.getCurrentUser().getGroupsIds().put(tempGroup.getIdGroup(),tempGroup.getName());
        DatabaseReference refUser = dataManager.getRealTimeDB()
                .getReference(Keys.KEY_USERS)
                .child(dataManager.getCurrentUser().getUid())
                .child(Keys.KEY_USER_GROUPS_IDS);

        refUser.child(tempGroup.getIdGroup())
                .setValue(tempGroup.getName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(CreateGroupActivity.this, "new Group added to User creator DB",Toast.LENGTH_SHORT).show();
                /**
                 * if the group was added successfully
                 * this layout will close and get back ro Main layout
                 * and see the new group there
                 */
                finish();
            }
        });
    }

    // TODO: 29/06/2022 Change next activity if needed
    private void nextActivity() {
        startActivity(new Intent(CreateGroupActivity.this, MainActivity.class));
        finish();
    }
}