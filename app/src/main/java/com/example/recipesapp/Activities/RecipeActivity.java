package com.example.recipesapp.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Objects.Ingredient;
import com.example.recipesapp.Objects.NutritionFacts;
import com.example.recipesapp.Objects.Recipe;
import com.example.recipesapp.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class RecipeActivity extends AppCompatActivity {

    private DataManager dataManager = DataManager.getInstance();
    private Recipe recipe;
    private DatabaseReference recipeRef;

    //views
    private ShapeableImageView recipe_view_IMG_dish;
    private MaterialTextView recipe_view_LBL_name;
    private MaterialTextView recipe_view_LBL_sub_title;
    private MaterialTextView ingredients_txt;
    private MaterialTextView how_to_txt;
    private AppCompatImageView res_IMG_nut1;
    private AppCompatImageView res_IMG_nut2;
    private AppCompatImageView res_IMG_nut3;
    private AppCompatImageView res_IMG_nut4;
    private AppCompatImageView res_IMG_nut5;
    private AppCompatImageButton back_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        findViews();
        initButton();

        recipeRef = dataManager.recipessListReference();
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(dataManager.getCurrentIdRecipe())){
                    recipe = snapshot.child(dataManager.getCurrentIdRecipe()).getValue(Recipe.class);
                    loadData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadData() {
        recipe_view_LBL_name.setText(recipe.getName());
        String des  = recipe.getDescription();

        //image
        Glide.with(this).load(recipe.getImg())
                .apply(RequestOptions.circleCropTransform())
                .into(recipe_view_IMG_dish);

        if(des != null && !des.isEmpty())
            recipe_view_LBL_sub_title.setText(des);

        //ingridients
        StringBuffer buffer = new StringBuffer();
        Collection<Ingredient> values = recipe.getIngredients().values();
        // Creating an ArrayList of values
        ArrayList<Ingredient> listOfValues= new ArrayList<>(values);       
        for (int i =0; i< listOfValues.size();i++){
            Ingredient temp = listOfValues.get(i);
            buffer.append(temp.getNameIngredient() +"-\nAmount: ") ;
            buffer.append(temp.getAmount() + ", Unit Of Measure: ") ;
            buffer.append(temp.getUnitOfMeasure().toString().toLowerCase(Locale.ROOT)+"\n" );
        }
        ingredients_txt.setText(buffer.toString().replace("\\n","\n"));

        Collection<NutritionFacts> nut = recipe.getNutritionFacts().values();
        // Creating an ArrayList of values
        ArrayList<NutritionFacts> nutList= new ArrayList<>(nut);
        for (int i =0; i< nutList.size();i++){
            String ing = nutList.get(i).getNameNutrition().toString();
            switch (ing) {
                case "ORGANIC":
                    res_IMG_nut1.setImageResource(R.drawable.ic_organic_food);
                    res_IMG_nut1.setVisibility(View.VISIBLE);
                    break;
                case "CARBOHYDRATES":
                    res_IMG_nut2.setImageResource(R.drawable.ic_carbohydrates);
                    res_IMG_nut2.setVisibility(View.VISIBLE);
                    break;
                case "DAIRY":
                    res_IMG_nut3.setImageResource(R.drawable.ic_dairy);
                    res_IMG_nut3.setVisibility(View.VISIBLE);
                    break;
                case "NO_EGG":
                    res_IMG_nut4.setImageResource(R.drawable.ic_no_eggs);
                    res_IMG_nut4.setVisibility(View.VISIBLE);
                    break;
                case "NO_PEANUT":
                    res_IMG_nut5.setImageResource(R.drawable.ic_no_peanut);
                    res_IMG_nut5.setVisibility(View.VISIBLE);
                    break;
            }
        }

        how_to_txt.setText(recipe.getSteps().replace("\\n","\n"));



    }


    private void initButton() {
        back_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
                finish();
            }
        });
    }

    private void findViews() {
        recipe_view_IMG_dish = findViewById(R.id.recipe_view_IMG_dish);
        recipe_view_LBL_name = findViewById(R.id.recipe_view_LBL_name);
        recipe_view_LBL_sub_title = findViewById(R.id.recipe_view_LBL_sub_title);
        ingredients_txt = findViewById(R.id.ingredients_txt);
        how_to_txt = findViewById(R.id.how_to_txt);
        res_IMG_nut1 = findViewById(R.id.res_IMG_nut1);
        res_IMG_nut2 = findViewById(R.id.res_IMG_nut2);
        res_IMG_nut3 = findViewById(R.id.res_IMG_nut3);
        res_IMG_nut4 = findViewById(R.id.res_IMG_nut4);
        res_IMG_nut5 = findViewById(R.id.res_IMG_nut5);
        back_BTN = findViewById(R.id.back_BTN);
    }
}