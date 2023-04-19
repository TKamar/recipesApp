package com.example.recipesapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Constants.NutritionFactsNames;
import com.example.recipesapp.Constants.UnitOfMeasure;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Firebase.FireStorage;
import com.example.recipesapp.Listeners.CallBack_ImageUpload;
import com.example.recipesapp.Objects.Ingredient;
import com.example.recipesapp.Objects.NutritionFacts;
import com.example.recipesapp.Objects.Recipe;
import com.example.recipesapp.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class CreateRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase
    private final DataManager dataManager = DataManager.getInstance();
    private FireStorage fireStorage;

    //Attributes
    private final HashMap<String,Ingredient> ingredients = new HashMap<>(); //List of the ingredients
    private final HashMap<String,NutritionFacts> facts = new HashMap<>(); //List of the NutritionFacts
    private String urlImg;
    private Recipe tempRecipe;

    //CallBack
    CallBack_ImageUpload callBack_Image_upload = new CallBack_ImageUpload() {
        @Override
        public void imageUrlAvailable(String url, Activity activity) {
            urlImg = url;

        }
    };

    //Views
    private LinearLayoutCompat create_recipe_LAY_ingredients_list;
    private MaterialButton create_recipe_BTN_add;
    private MaterialButton create_recipe_BTN_save;
    private TextInputEditText create_recipe_EDT_recipe;
    private TextInputEditText create_recipe_EDT_name;
    private TextInputEditText create_recipe_EDT_sub_title;
    private ImageButton button1, button2, button3, button4, button5;
    private ImageButton create_recipe_BTN_edit;
    private AppCompatImageButton create_recipe_BTN_back;
    private ImageView nutrition_IMG_nut1;
    private ImageView nutrition_IMG_nut2;
    private ImageView nutrition_IMG_nut3;
    private ImageView nutrition_IMG_nut4;
    private ImageView nutrition_IMG_nut5;
    private ShapeableImageView create_recipe_IMG_dish;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        findViews();

        tempRecipe = new Recipe("");

        //Init fireStorage
        fireStorage = FireStorage.getInstance();
        fireStorage.setCallBack_imageUpload(callBack_Image_upload);

        initNutritionFacts();

        // Init On Click Buttons
        create_recipe_BTN_add.setOnClickListener(this);
        create_recipe_BTN_save.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        create_recipe_BTN_edit.setOnClickListener(this);
        create_recipe_BTN_back.setOnClickListener(this);

        //Add the option to scroll in a text view
        create_recipe_EDT_recipe.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (create_recipe_EDT_recipe.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    //Add Nutrition Facts to the array
    private void initNutritionFacts() {
        facts.put(NutritionFactsNames.ORGANIC.toString(),new NutritionFacts(NutritionFactsNames.ORGANIC, String.valueOf(R.drawable.ic_organic_food)));
        facts.put(NutritionFactsNames.CARBOHYDRATES.toString(),new NutritionFacts(NutritionFactsNames.CARBOHYDRATES, String.valueOf(R.drawable.ic_carbohydrates)));
        facts.put(NutritionFactsNames.NO_PEANUT.toString(),new NutritionFacts(NutritionFactsNames.NO_PEANUT, String.valueOf(R.drawable.ic_no_peanut)));
        facts.put(NutritionFactsNames.DAIRY.toString(),new NutritionFacts(NutritionFactsNames.DAIRY, String.valueOf(R.drawable.ic_dairy)));
        facts.put(NutritionFactsNames.NO_EGG.toString(),new NutritionFacts(NutritionFactsNames.NO_EGG, String.valueOf(R.drawable.ic_no_eggs)));
    }

    private void findViews() {
        create_recipe_BTN_add = findViewById(R.id.create_recipe_BTN_add);
        create_recipe_BTN_save = findViewById(R.id.create_recipe_BTN_save);
        create_recipe_LAY_ingredients_list = findViewById(R.id.create_recipe_LAY_ingredients_list);
        create_recipe_EDT_recipe = findViewById(R.id.create_recipe_EDT_recipe);
        create_recipe_EDT_name = findViewById(R.id.create_recipe_EDT_name);
        create_recipe_EDT_sub_title = findViewById(R.id.create_recipe_EDT_sub_title);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        nutrition_IMG_nut1 = findViewById(R.id.nutrition_IMG_nut1);
        nutrition_IMG_nut2 = findViewById(R.id.nutrition_IMG_nut2);
        nutrition_IMG_nut3 = findViewById(R.id.nutrition_IMG_nut3);
        nutrition_IMG_nut4 = findViewById(R.id.nutrition_IMG_nut4);
        nutrition_IMG_nut5 = findViewById(R.id.nutrition_IMG_nut5);
        create_recipe_IMG_dish = findViewById(R.id.create_recipe_IMG_dish);
        create_recipe_BTN_edit = findViewById(R.id.create_recipe_BTN_edit);
        create_recipe_BTN_back = findViewById(R.id.create_recipe_BTN_back);
    }

    /**
        On click  of all the Buttons in this form
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_recipe_BTN_add:
                addView();
                break;
            case R.id.create_recipe_BTN_save:
                saveNewRecipe();
                break;
            case R.id.button1:
                nutrition_IMG_nut1.setVisibility(View.GONE);
                button1.setVisibility(View.GONE);
                facts.remove(NutritionFactsNames.ORGANIC.toString());
                break;
            case R.id.button2:
                nutrition_IMG_nut2.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
                facts.remove(NutritionFactsNames.CARBOHYDRATES.toString());
                break;
            case R.id.button3:
                nutrition_IMG_nut3.setVisibility(View.GONE);
                button3.setVisibility(View.GONE);
                facts.remove(NutritionFactsNames.NO_PEANUT.toString());
                break;
            case R.id.button4:
                nutrition_IMG_nut4.setVisibility(View.GONE);
                button4.setVisibility(View.GONE);
                facts.remove(NutritionFactsNames.DAIRY.toString());
                break;
            case R.id.button5:
                nutrition_IMG_nut5.setVisibility(View.GONE);
                button5.setVisibility(View.GONE);
                facts.remove(NutritionFactsNames.NO_EGG.toString());
                break;
            case R.id.create_recipe_BTN_edit:
                imagePick();
                break;
            case R.id.create_recipe_BTN_back:
                finish();
                break;

        }
    }

    //Image Recipe Picker From Gallery
    private void imagePick() {
        ImagePicker.with(CreateRecipeActivity.this)
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
        create_recipe_IMG_dish.setImageURI(resultUri);
        fireStorage.uploadImgToStorage(resultUri, tempRecipe.getIdRecipe(), Keys.KEY_RECIPE_PICTURES, this);
    }

    /**
        Save the recipe into the firebase
     */
    private void saveNewRecipe() {

        if (create_recipe_EDT_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Recipe Name can't be empty ", Toast.LENGTH_SHORT).show();
            return;
        }else
            tempRecipe.setName(create_recipe_EDT_name.getText().toString());
//            tempRecipe = new Recipe(create_recipe_EDT_name.getText().toString());

        //Handel Image Picker & upload image to the Storage  and save the url
        if (urlImg != null)
            tempRecipe.setImg(urlImg);

        //Check the Ingredient data
        if (!checkIngredient()) {
            Toast.makeText(this, "all Details are require / incorrectly insert", Toast.LENGTH_SHORT).show();
            return;
        }
        //Add the ingredients to the temp recipe
        else
            tempRecipe.setIngredients(ingredients);

// TODO: 29/06/2022 Add validator
        //Check the recipe data
        //Check the recipe steps methods
        if (create_recipe_EDT_recipe.getText().toString().isEmpty()) {
            Toast.makeText(this, "Recipe steps methods be empty ", Toast.LENGTH_SHORT).show();
            return;
        } else
            tempRecipe.setSteps(create_recipe_EDT_recipe.getText().toString());

        //get the description (can be Empty)
        tempRecipe.setDescription(create_recipe_EDT_sub_title.getText().toString());

        //Check NutritionFacts
        tempRecipe.setNutritionFacts(facts);

        //current recipe save on data manager
        dataManager.setCurrentIdRecipe(tempRecipe.getIdRecipe());

        //Save and move to the next Activity
        SaveNewRecipeToDatabase(tempRecipe);
    }

    /** Save New Recipe To Database -Realtime */
    private void SaveNewRecipeToDatabase(Recipe tempRecipe) {
        //Save tempRecipe to Recipe List
        DatabaseReference ref = dataManager.recipesListReference();
        ref.child(tempRecipe.getIdRecipe()).setValue(tempRecipe.toMap(), new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                dataManager.groupsListReference().child(dataManager.getCurrentIdGroup())
                        .child(Keys.KEY_GROUP_RECIPES_LIST).child(tempRecipe.getIdRecipe()).setValue(tempRecipe.getName(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                dataManager.setCurrentIdRecipe(tempRecipe.getIdRecipe());
                                finish();
                            }
                        });



//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                dataManager.setCurrentIdRecipe(tempRecipe.getIdRecipe());
//                                finish();
//                            }
//                        });
            }
        });
//        ref.child(Keys.KEY_RECIPE_ID).setValue(tempRecipe.getIdRecipe());
//        ref.child(Keys.KEY_RECIPE_NAME).setValue(tempRecipe.getName());
//        ref.child(Keys.KEY_RECIPE_DESCRIPTION).setValue(tempRecipe.getDescription());
//        ref.child(Keys.KEY_RECIPE_IMG).setValue(tempRecipe.getImg());
//        ref.child(Keys.KEY_RECIPE_STEPS).setValue(tempRecipe.getSteps());
//        ref.child(Keys.KEY_RECIPE_INGREDIENTS_LIST).setValue(tempRecipe.getIngredients());
//        ref.child(Keys.KEY_RECIPE_FACTS_LIST).setValue(tempRecipe.getNutritionFacts());

        //Add the recipe Id to the current Group recipes ids list
// TODO: 30/06/2022 get back
        // TODO: 02/07/2022 do finish just after add listener successes


//        if (dataManager.getCurrentIdRecipe().isEmpty())

    }

    // TODO: 29/06/2022 Change next activity if needed
    private void nextActivity() {
        startActivity(new Intent(CreateRecipeActivity.this, MainActivity.class));
        finish();
    }

    private boolean checkIngredient() {
        //move over the ingredients into the linear layout and add them to the list ingredients
        for (int i = 0; i < create_recipe_LAY_ingredients_list.getChildCount(); i++) {

            //get the ingredient Card on the position i
            View ingredientCard = create_recipe_LAY_ingredients_list.getChildAt(i);
            //Find Views in the Ingredient Card in i index
            TextInputEditText ingredientName = ingredientCard.findViewById(R.id.ingredients_card_EDT_name);
            RadioGroup radioGroup = ingredientCard.findViewById(R.id.ingredients_card_BTN_radioGroup);
            MaterialTextView amount = ingredientCard.findViewById(R.id.ingredients_card_LBL_amount);

            //temp ingredient
            Ingredient temp = new Ingredient();

            // TODO: 29/06/2022 Add validator class
            //check validation of Name
            if (!ingredientName.getText().toString().isEmpty())
                temp.setNameIngredient(ingredientName.getText().toString());
            else
                return false;

            //check validation of amount
            if (Double.parseDouble(amount.getText().toString()) != 0.0)
                temp.setAmount(Double.parseDouble(amount.getText().toString()));
            else
                return false;

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_button_2:
                    temp.setUnitOfMeasure(UnitOfMeasure.TEASPOON);
                    break;
                case R.id.radio_button_3:
                    temp.setUnitOfMeasure(UnitOfMeasure.TABLESPOON);
                    break;
                case R.id.radio_button_4:
                    temp.setUnitOfMeasure(UnitOfMeasure.ONCE);
                    break;
                default: //also for radio button1
                    temp.setUnitOfMeasure(UnitOfMeasure.CUP);
            }

            //Add to ingredients list
            ingredients.put(temp.getNameIngredient(),temp);
        }

        if (ingredients.size() == 0) {
            Toast.makeText(this, "Add Ingredient!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // UI Updates
    private void addView() {

         View ingredientCard = getLayoutInflater().inflate(R.layout.ingredients_add_card, null, false);
        // Attributes In Card Ingredient Add
//        TextInputEditText ingredientName = ingredientCard.findViewById(R.id.ingredients_card_EDT_name);
//        RadioGroup radioGroup = ingredientCard.findViewById(R.id.ingredients_card_BTN_radioGroup);
        MaterialTextView amount = ingredientCard.findViewById(R.id.ingredients_card_LBL_amount);
        ImageButton deleteBtn = ingredientCard.findViewById(R.id.ingredients_card_BTN_delete);
        MaterialButton plusBtn = ingredientCard.findViewById(R.id.ingredients_card_BTN_plus);
        MaterialButton minusBtn = ingredientCard.findViewById(R.id.ingredients_card_BTN_minus);

        //Remove View Ingredient
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(ingredientCard);
            }
        });

        //Minus Amount Ingredient
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 27/06/2022 add all the options
                double min = Double.parseDouble(amount.getText().toString());
                if (min > 0.0) {
                    amount.setText(String.valueOf(min - 0.25));
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 27/06/2022 add all the options
                double max = Double.parseDouble(amount.getText().toString());
                if (max < 99.9) {
                    amount.setText(String.valueOf(max + 0.25));
                }
            }
        });

        ((LinearLayoutCompat)create_recipe_LAY_ingredients_list).addView(ingredientCard);
    }

    private void removeView(View view) {
        create_recipe_LAY_ingredients_list.removeView(view);
    }


}