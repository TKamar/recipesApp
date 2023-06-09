package com.example.recipesapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;
import com.example.recipesapp.App;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    VideoView splash_VID_video;
//    AppCompatButton splash_BTN_loginSplash;
    MaterialButton splash_BTN_loginSplash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash);
        LayoutInflater inflter = (LayoutInflater.from(this));


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DataManager.getInstance().currentUserChangeListener();
        }
        //Check if the user is already existing
//        DataManager dataManager = DataManager.getInstance();
//        dataManager.setCallback_isUserExist(Callback_isUserExist);


        splash_BTN_loginSplash = findViewById(R.id.splash_BTN_loginSplash);
        splash_VID_video = findViewById(R.id.splash_VID_video);
        String path = "";
        String packName = this.getResources().getString(R.string.package_sufix);
        switch (packName){
            case ".family":
                path = "android.resource://com.example.recipesapp.family/" + R.raw.splash_video_family;
                break;
            case ".italian":
                path = "android.resource://com.example.recipesapp.italian/" + R.raw.splash_video_italian;
                break;
            default:
                path = "android.resource://com.example.recipesapp/" + R.raw.splash_video;
                break;
        }
//        Log.d("path_video", "path: " + path);
//        Log.d("path_video", "packName: " + packName);
//        Log.d("path_video", "path: " + path);
//        Log.d("path_video", "R.string.package_sufix: " + R.string.package_sufix);
//        Log.d("path_video", "R.raw.splash_video: " + R.raw.splash_video);
//        String path = "https://assets9.lottiefiles.com/packages/lf20_0lzv8w7z.json";
        Uri uri = Uri.parse(path);
        splash_VID_video.setVideoURI(uri);
        splash_VID_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();

            }
        });

        //for the video will be playing again and again
        splash_VID_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

        //moving to the login/register page
        splash_BTN_loginSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this , LoginActivity.class));
                finish(); //the splash page id dead cant go back to it
            }
        });

    }

//    Callback_isUserExist callback_isUserExist = new Callback_isUserExist() {
//        @Override
//        public void isExist() {
//            moveActivity(MainActivity.class);
//        }
//
//        @Override
//        public void createUser() {
//            moveActivity(SignUpActivity.class);
//        }
//    } ;

}