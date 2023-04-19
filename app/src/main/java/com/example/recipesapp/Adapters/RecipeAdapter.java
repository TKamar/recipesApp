package com.example.recipesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.recipesapp.Activities.RecipeActivity;
import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class RecipeAdapter extends FirebaseRecyclerAdapter<String, RecipeAdapter.RecipeHolder> {

    private DataManager dataManager = DataManager.getInstance();
    private DatabaseReference recipeRef = dataManager.recipessListReference();
    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecipeAdapter(@NonNull FirebaseRecyclerOptions<String> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecipeHolder holder, int position, @NonNull String model) {

        String recipeIDs = getRef(position).getKey();
        recipeRef.child(recipeIDs).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String name = snapshot.child(Keys.KEY_RECIPE_NAME).getValue(String.class);
                    String des = snapshot.child(Keys.KEY_RECIPE_DESCRIPTION).getValue(String.class);
                    String img = snapshot.child(Keys.KEY_RECIPE_IMG).getValue(String.class);

                    Glide.with(context)
                            .load(img)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.recipe_item_LBL_title.setText(name);
                                    holder.recipe_item_LBL_sub_title.setText(des);
                                    holder.recipe_item_IMG_img.setImageResource(R.drawable.ic_image_error);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.recipe_item_LBL_title.setText(name);
                                    holder.recipe_item_LBL_sub_title.setText(des);
                                    return false;
                                }
                            })
                            .error(R.drawable.ic_image_error)
                            .override(300, 300)
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.recipe_item_IMG_img);

                    HashMap<String, Objects> nutFactsRecipe = (HashMap<String, Objects>) snapshot.child(Keys.KEY_RECIPE_FACTS_LIST).getValue();
                    Collection<String> n = nutFactsRecipe.keySet();
                    ArrayList<String> names = new ArrayList<>(n);
                    for (String facts : names) { //for arrayList
                        switch (facts) {
                            case "ORGANIC":
                                holder.nutrition_IMG_nut1.setImageResource(R.drawable.ic_organic_food);
                                holder.nutrition_IMG_nut1.setVisibility(View.VISIBLE);
                                break;
                            case "CARBOHYDRATES":
                                holder.nutrition_IMG_nut2.setImageResource(R.drawable.ic_carbohydrates);
                                holder.nutrition_IMG_nut2.setVisibility(View.VISIBLE);
                                break;
                            case "DAIRY":
                                holder.nutrition_IMG_nut3.setImageResource(R.drawable.ic_dairy);
                                holder.nutrition_IMG_nut3.setVisibility(View.VISIBLE);
                                break;
                            case "NO_EGG":
                                holder.nutrition_IMG_nut4.setImageResource(R.drawable.ic_no_eggs);
                                holder.nutrition_IMG_nut4.setVisibility(View.VISIBLE);
                                break;
                            case "NO_PEANUT":
                                holder.nutrition_IMG_nut5.setImageResource(R.drawable.ic_no_peanut);
                                holder.nutrition_IMG_nut5.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataManager.setCurrentIdRecipe(recipeIDs);
                            Intent intent = new Intent(context, RecipeActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_card, parent, false);
        RecipeHolder holder = new RecipeHolder(view);
        return holder;
    }

    // External class - HOLDER
    public class RecipeHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView recipe_item_IMG_img;
        private MaterialTextView recipe_item_LBL_title;
        private MaterialTextView recipe_item_LBL_sub_title;
        private AppCompatImageView nutrition_IMG_nut1;
        private AppCompatImageView nutrition_IMG_nut2;
        private AppCompatImageView nutrition_IMG_nut3;
        private AppCompatImageView nutrition_IMG_nut4;
        private AppCompatImageView nutrition_IMG_nut5;


        public RecipeHolder(View itemView) {
            super(itemView);

            recipe_item_IMG_img = itemView.findViewById(R.id.recipe_item_IMG_img);
            recipe_item_LBL_title = itemView.findViewById(R.id.recipe_item_LBL_title);
            recipe_item_LBL_sub_title = itemView.findViewById(R.id.recipe_item_LBL_sub_title);
            nutrition_IMG_nut1 = itemView.findViewById(R.id.nutrition_IMG_nut1);
            nutrition_IMG_nut2 = itemView.findViewById(R.id.nutrition_IMG_nut2);
            nutrition_IMG_nut3 = itemView.findViewById(R.id.nutrition_IMG_nut3);
            nutrition_IMG_nut4 = itemView.findViewById(R.id.nutrition_IMG_nut4);
            nutrition_IMG_nut5 = itemView.findViewById(R.id.nutrition_IMG_nut5);

//            itemView.setVisibility(View.INVISIBLE);
        }
    }
//
//        public interface RecipeListener {
//                void recipeClicked(Recipe recipe, int position);
//        }
//
//    private Activity activity;
////    private HashMap<String,Recipe> recipes = new HashMap<>();
//    private ArrayList<Recipe> recipes = new ArrayList<>(); //Fore arrayList
//    private RecipeListener listener;
//
//    //Constructor
//    public RecipeAdapter(Activity activity, ArrayList<Recipe> Recipes) {
//        this.activity = activity;
//        this.recipes = Recipes;
//    }
//
//
////    public RecipeAdapter(Activity activity, HashMap<String, Recipe> recipes) {
////        this.activity = activity;
////        this.recipes = recipes;
////    }
//
//    public RecipeAdapter setListener(RecipeListener listener) {
//        this.listener = listener;
//        return this;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item_card, parent, false);
//        return new RecipeHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//
//        final RecipeHolder holder = (RecipeHolder) viewHolder;
//        //get the recipe in the specific position
//        Recipe recipe = getRecipe(position);
//        //Update the ui
//        holder.recipe_item_LBL_title.setText(recipe.getName());
//        holder.recipe_item_LBL_sub_title.setText(recipe.getDescription());
//
//        // Get the image of the recipe dish from Data Storage WIth Glide
//            // TODO: 29/06/2022 Check if glide work with thr uri/l image
//        Glide.with(activity).load(recipe.getImg()).into(holder.recipe_item_IMG_img);
//
//        ArrayList<NutritionFacts> nutFactsRecipe = recipe.getNutritionFacts().values().stream().collect(Collectors.toCollection(ArrayList::new));
//        //get the Images of the nutrition facts (not from data Storage
//        for (NutritionFacts facts : nutFactsRecipe) { //for arrayList
//             switch (facts.getNameNutrition()) {
//                 case ORGANIC:
//                        holder.nutrition_IMG_nut1.setImageResource(R.drawable.ic_organic_food);
//                        holder.nutrition_IMG_nut1.setVisibility(View.VISIBLE);
//                        break;
//                    case CARBOHYDRATES:
//                        holder.nutrition_IMG_nut2.setImageResource(R.drawable.ic_carbohydrates);
//                        holder.nutrition_IMG_nut2.setVisibility(View.VISIBLE);
//                        break;
//                    case DAIRY:
//                        holder.nutrition_IMG_nut3.setImageResource(R.drawable.ic_dairy);
//                        holder.nutrition_IMG_nut3.setVisibility(View.VISIBLE);
//                        break;
//                    case NO_EGG:
//                        holder.nutrition_IMG_nut4.setImageResource(R.drawable.ic_no_eggs);
//                        holder.nutrition_IMG_nut4.setVisibility(View.VISIBLE);
//                        break;
//                    case NO_PEANUT:
//                        holder.nutrition_IMG_nut5.setImageResource(R.drawable.ic_no_peanut);
//                        holder.nutrition_IMG_nut5.setVisibility(View.VISIBLE);
//                        break;
//                }
//
//
//
////        for (Map.Entry<String, NutritionFacts> entry : recipe.getNutritionFacts().entrySet() ) {
////            switch (entry.getKey()) {
////                case "ORGANIC":
////                    holder.nutrition_IMG_nut1.setImageResource(R.drawable.ic_organic_food);
////                    holder.nutrition_IMG_nut1.setVisibility(View.VISIBLE);
////                    break;
////                case "CARBOHYDRATES":
////                    holder.nutrition_IMG_nut2.setImageResource(R.drawable.ic_carbohydrates);
////                    holder.nutrition_IMG_nut2.setVisibility(View.VISIBLE);
////                    break;
////                case "DAIRY":
////                    holder.nutrition_IMG_nut3.setImageResource(R.drawable.ic_dairy);
////                    holder.nutrition_IMG_nut3.setVisibility(View.VISIBLE);
////                    break;
////                case "NO_EGG":
////                    holder.nutrition_IMG_nut4.setImageResource(R.drawable.ic_no_eggs);
////                    holder.nutrition_IMG_nut4.setVisibility(View.VISIBLE);
////                    break;
////                case "NO_PEANUT":
////                    holder.nutrition_IMG_nut5.setImageResource(R.drawable.ic_no_peanut);
////                    holder.nutrition_IMG_nut5.setVisibility(View.VISIBLE);
////                    break;
////            }
//        }
//
////                Check if don't work otherwise
////                int resourceId = activity.getResources().getIdentifier(recipe.getImage(), "drawable", activity.getPackageName());
////                holder.Recipe_IMG_image.setImageResource(resourceId);
//    }
//
//    @Override
//    public int getItemCount() {
//        return recipes.size();
//    }
//
//    // Get the recipe in the specific position
//    public Recipe getRecipe(int position) {
//        return recipes.get(position);
//    }
//
//    // External class - HOLDER
//    public class RecipeHolder extends RecyclerView.ViewHolder {
//
//        private ShapeableImageView recipe_item_IMG_img;
//        private MaterialTextView recipe_item_LBL_title;
//        private MaterialTextView recipe_item_LBL_sub_title;
//        private AppCompatImageView nutrition_IMG_nut1;
//        private AppCompatImageView nutrition_IMG_nut2;
//        private AppCompatImageView nutrition_IMG_nut3;
//        private AppCompatImageView nutrition_IMG_nut4;
//        private AppCompatImageView nutrition_IMG_nut5;
//
//
//        public RecipeHolder(View itemView) {
//            super(itemView);
//
//            recipe_item_IMG_img = itemView.findViewById(R.id.recipe_item_IMG_img);
//            recipe_item_LBL_title = itemView.findViewById(R.id.recipe_item_LBL_title);
//            recipe_item_LBL_sub_title = itemView.findViewById(R.id.recipe_item_LBL_sub_title);
//            nutrition_IMG_nut1 = itemView.findViewById(R.id.nutrition_IMG_nut1);
//            nutrition_IMG_nut2 = itemView.findViewById(R.id.nutrition_IMG_nut2);
//            nutrition_IMG_nut3 = itemView.findViewById(R.id.nutrition_IMG_nut3);
//            nutrition_IMG_nut4 = itemView.findViewById(R.id.nutrition_IMG_nut4);
//            nutrition_IMG_nut5 = itemView.findViewById(R.id.nutrition_IMG_nut5);
//
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    listener.recipeClicked(getRecipe(getAdapterPosition()), getAdapterPosition());
////                }
////            });
//        }
//    }
}