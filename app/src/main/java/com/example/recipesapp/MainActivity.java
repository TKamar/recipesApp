package com.example.recipesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initNavView();

    }

    private void findViews() {
        navigationView = findViewById(R.id.bottom_navigation);
    }

    private void initNavView() {
        navigationView.setSelectedItemId(R.id.page_1);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.page_1:
                        return true;
                    case R.id.page_2:
                        finish();
                        startActivity(new Intent(MainActivity.this, newRecipe.class));
                }
                return true;
            }
        });
    }




}