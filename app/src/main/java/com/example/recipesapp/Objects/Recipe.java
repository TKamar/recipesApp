package com.example.recipesapp.Objects;

import com.example.recipesapp.Constants.Keys;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Recipe {

    private String idRecipe;
    private String name;
    private String description;
    private String img;
    private String steps;
    private HashMap<String,Ingredient> ingredients;
    private HashMap<String,NutritionFacts> nutritionFacts;

    public Recipe() { }

    public Recipe(String name) {
        this.idRecipe = UUID.randomUUID().toString();
        this.name = name;
        this.description = "description";
        this.img = "https://firebasestorage.googleapis.com/v0/b/grandma-s-secrets.appspot.com/o/food_dish.png?alt=media&token=0bbd4c23-7cfd-4ff9-b21f-365a736be9f6";// TODO: 02/07/2022 Change image
        this.steps = "steps";
        this.ingredients = new HashMap<>();
        this.nutritionFacts = new HashMap<>();
    }

    public String getIdRecipe() {
        return idRecipe;
    }

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Recipe setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Recipe setImg(String img) {
        this.img = img;
        return this;
    }

    public HashMap<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(HashMap<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public HashMap<String, NutritionFacts> getNutritionFacts() {
        return nutritionFacts;
    }

    public Recipe setNutritionFacts(HashMap<String, NutritionFacts> nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
        return this;
    }

    public String getSteps() {
        return steps;
    }

    public Recipe setSteps(String steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "idRecipe='" + idRecipe + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img='" + img + '\'' +
                ", steps='" + steps + '\'' +
                ", ingredients=" + ingredients +
                ", nutritionFacts=" + nutritionFacts +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> mapRes = new HashMap<>();
        mapRes.put(Keys.KEY_RECIPE_ID, idRecipe);
        mapRes.put(Keys.KEY_RECIPE_NAME, name);
        mapRes.put(Keys.KEY_RECIPE_IMG, img);
        mapRes.put(Keys.KEY_RECIPE_DESCRIPTION, description);
        mapRes.put(Keys.KEY_RECIPE_STEPS, steps);
        mapRes.put(Keys.KEY_RECIPE_FACTS_LIST, nutritionFacts);
        mapRes.put(Keys.KEY_RECIPE_INGREDIENTS_LIST, ingredients);
        return mapRes;
    }
}
