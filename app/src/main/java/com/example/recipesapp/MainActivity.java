package com.example.recipesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.page_1:
                        return true;
                    case R.id.page_2:
                        return true;
                }
                return true;
            }
        });

        navigationView.setOnNavigationItemReselectedListener { item ->
                when(item.itemId) {
            R.id.item1 -> {
                // Respond to navigation item 1 reselection
            }
            R.id.item2 -> {
                // Respond to navigation item 2 reselection
            }
        }
        }
    }

    private void findViews() {
        navigationView = findViewById(R.id.bottom_navigation);
    }




}