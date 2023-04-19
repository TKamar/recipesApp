package com.example.recipesapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Firebase.FireStorage;
import com.example.recipesapp.Fragments.GroupListFragment;
import com.example.recipesapp.Listeners.CallBack_ImageUpload;
import com.example.recipesapp.Objects.User;
import com.example.recipesapp.R;
import com.firebase.ui.auth.AuthUI;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity<FirebaseFirestore> extends AppCompatActivity {

    //CallBack
    CallBack_ImageUpload callBack_Image_upload = new CallBack_ImageUpload() {
        @Override
        public void imageUrlAvailable(String url, Activity activity) {
            //update the database
            DatabaseReference reference = dataManager.usersListReference().child(dataManager.getCurrentUser().getUid());
            reference.child(Keys.KEY_USER_IMG).setValue(url);
        }
    };

    private final DataManager dataManager = DataManager.getInstance();
    private FireStorage fireStorage = FireStorage.getInstance();
    private final User currentUser = dataManager.getCurrentUser();
//    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();

    private BottomNavigationView bottom_nav_menu;
    private FloatingActionButton main_FAB_fab;
    private DrawerLayout drawer_layout;
    private NavigationView main_nav_side_menu;
    private MaterialToolbar main_TOB_up;
    private View header;
    private FloatingActionButton profile_FAB_edit;
    private MaterialTextView profile_TXT_username;
    private ShapeableImageView profile_IMG_user;
    private CircularProgressIndicator profile_BAR_progress;
    private FragmentContainerView main_FRG_container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(main_TOB_up);

        findViews();
        //Init fireStorage
        fireStorage = FireStorage.getInstance();
        fireStorage.setCallBack_imageUpload(callBack_Image_upload);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_FRG_container, new GroupListFragment()).commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_FRG_container, new GroupListFragment()).commit();
        initButtons();

        //Check if user still logged-in, if not -> will transfer to login
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

//        initUserProfileSide();
        updateUI();
    }

    /**
     * Update Fragment and Drawer
     */
    private void updateUI() {

        //Update Fragment
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.main_FRG_container, GroupListFragment.class, null)
//                .commit();

        //Update side menu - profile
        bottom_nav_menu.getMenu().getItem(3).setEnabled(true);
        initUserProfileSide();
    }

    private void initUserProfileSide() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = dataManager.usersListReference().child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = currentUser;
                Glide.with(MainActivity.this)
                        .load(user.getImg())
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_IMG_user);
                profile_TXT_username.setText(user.getFirstName()+" "+user.getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initButtons() {

        //Up Bar
        main_TOB_up.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Back", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        //Side Bar Options
        main_nav_side_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_settings:
                        Toast.makeText(MainActivity.this, "Setting - NOT NEED FOR NOW", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item_log_out:
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @SuppressLint("RestrictedApi")
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                        Toast.makeText(MainActivity.this, "Bey Bey", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                        break;
                }
                return true;
            }
        });

        //Bottom Menu
        bottom_nav_menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_ITM_itm1:
                        //Open Side menu - Clicked Profile
                        drawer_layout.openDrawer(drawer_layout.getForegroundGravity());
//                        Toast.makeText(MainActivity.this, "open drawer Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_ITM_itm4:
                        //Click Groups
//                       fragment = new GroupListFragment();
                        Toast.makeText(MainActivity.this, "maybe on the future", Toast.LENGTH_SHORT).show();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.main_FRG_container, new GroupListFragment()).commit();
                        break;
                }

                return true;
            }
        });

        //Thw add fab - if on All groups page -> add Group
        //              if on specific Group -> add recipe
        main_FAB_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateGroupActivity.class));
            }
        });

        profile_FAB_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .crop(1f, 1f)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

    }

    private void findViews() {
        bottom_nav_menu = findViewById(R.id.main_BNV_bottom_nav);
        //Enabled the under fav item
        bottom_nav_menu.getMenu().getItem(2).setEnabled(false);

        main_FAB_fab = findViewById(R.id.main_FAB_fab);
        drawer_layout = findViewById(R.id.drawer_layout);
        main_nav_side_menu = findViewById(R.id.main_nav_side_menu);
        main_TOB_up = findViewById(R.id.main_TOB_up);

        main_FRG_container = findViewById(R.id.main_FRG_container);

        //side menu
        header = main_nav_side_menu.getHeaderView(0);
        profile_FAB_edit = header.findViewById(R.id.profile_FAB_edit);
        profile_TXT_username = header.findViewById(R.id.profile_TXT_username);
        profile_IMG_user= header.findViewById(R.id.profile_IMG_user);
        profile_BAR_progress= header.findViewById(R.id.profile_BAR_progress);
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
        profile_IMG_user.setImageURI(resultUri);
        fireStorage.uploadImgToStorage(resultUri, currentUser.getUid(), Keys.KEY_PROFILE_PICTURES, this);

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // Behaves like you would have pressed the home-button
            moveTaskToBack(true);
//            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}