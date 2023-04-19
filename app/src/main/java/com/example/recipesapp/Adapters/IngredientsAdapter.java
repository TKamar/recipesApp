package com.example.recipesapp.Adapters;//package com.example.grandmassecrets.Adapters;
//
//import android.app.Activity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.appcompat.widget.AppCompatImageView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.recipesapp.Objects.NutritionFacts;
//import com.example.recipesapp.Objects.Ingredient;
//import com.example.recipesapp.R;
//import com.google.android.material.imageview.ShapeableImageView;
//import com.google.android.material.textview.MaterialTextView;
//
//import java.util.ArrayList;
//
//public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        public interface IngredientListener {
//                void IngredientClicked(Ingredient Ingredient, int position);
//                void PlusClicked(Ingredient Ingredient, int position);
//                void MinusClicked(Ingredient Ingredient, int position);
//                void RemoveClicked(Ingredient Ingredient, int position);
//        }
//
//    private Activity activity;
//    private ArrayList<Ingredient> Ingredients = new ArrayList<>();
//    private IngredientListener listener;
//
//    //Constructor
//    public IngredientAdapter(Activity activity, ArrayList<Ingredient> Ingredients) {
//        this.activity = activity;
//        this.Ingredients = Ingredients;
//    }
//
//    public IngredientAdapter setListener(IngredientListener listener) {
//        this.listener = listener;
//        return this;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_add_card, parent, false);
//        return new IngredientHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//
//        final IngredientHolder holder = (IngredientHolder) viewHolder;
//        //get the Ingredient in the specific position
//        Ingredient Ingredient = getIngredient(position);
//        //Update the ui
//        holder.Ingredient_item_LBL_title.setText(Ingredient.getName());
//        holder.Ingredient_item_LBL_sub_title.setText(Ingredient.getDescription());
//
//        // Get the image of the Ingredient dish from Data Storage WIth Glide
//            // TODO: 29/06/2022 Check if glide work with thr uri/l image
//        Glide.with(activity).load(Ingredient.getImg()).into(holder.Ingredient_item_IMG_img);
//
//        //get the Images of the nutrition facts (not from data Storage
//        for (NutritionFacts facts : Ingredient.getNutritionFacts()) {
//            switch (facts.getNameNutrition()) {
//                case ORGANIC:
//                    holder.nutrition_IMG_nut1.setImageResource(R.drawable.ic_organic_food);
//                    holder.nutrition_IMG_nut1.setVisibility(View.VISIBLE);
//                    break;
//                case CARBOHYDRATES:
//                    holder.nutrition_IMG_nut2.setImageResource(R.drawable.ic_carbohydrates);
//                    holder.nutrition_IMG_nut2.setVisibility(View.VISIBLE);
//                    break;
//                case DAIRY:
//                    holder.nutrition_IMG_nut3.setImageResource(R.drawable.ic_dairy);
//                    holder.nutrition_IMG_nut3.setVisibility(View.VISIBLE);
//                    break;
//                case NO_EGG:
//                    holder.nutrition_IMG_nut4.setImageResource(R.drawable.ic_no_eggs);
//                    holder.nutrition_IMG_nut4.setVisibility(View.VISIBLE);
//                    break;
//                case NO_PEANUT:
//                    holder.nutrition_IMG_nut5.setImageResource(R.drawable.ic_no_peanut);
//                    holder.nutrition_IMG_nut5.setVisibility(View.VISIBLE);
//                    break;
//            }
//        }
//
////                Check if don't work otherwise
////                int resourceId = activity.getResources().getIdentifier(Ingredient.getImage(), "drawable", activity.getPackageName());
////                holder.Ingredient_IMG_image.setImageResource(resourceId);
//    }
//
//    @Override
//    public int getItemCount() {
//        return Ingredients.size();
//    }
//
//    // Get the Ingredient in the specific position
//    public Ingredient getIngredient(int position) {
//        return Ingredients.get(position);
//    }
//
//    // External class - HOLDER
//    class IngredientHolder extends RecyclerView.ViewHolder {
//
//        private ShapeableImageView Ingredient_item_IMG_img;
//        private MaterialTextView Ingredient_item_LBL_title;
//        private MaterialTextView Ingredient_item_LBL_sub_title;
//        private AppCompatImageView nutrition_IMG_nut1;
//        private AppCompatImageView nutrition_IMG_nut2;
//        private AppCompatImageView nutrition_IMG_nut3;
//        private AppCompatImageView nutrition_IMG_nut4;
//        private AppCompatImageView nutrition_IMG_nut5;
//
//
//        public IngredientHolder(View itemView) {
//            super(itemView);
//
////            Ingredient_item_IMG_img = itemView.findViewById(R.id.Ingredient_item_IMG_img);
////            Ingredient_item_LBL_title = itemView.findViewById(R.id.Ingredient_item_LBL_title);
////            Ingredient_item_LBL_sub_title = itemView.findViewById(R.id.Ingredient_item_LBL_sub_title);
//            nutrition_IMG_nut1 = itemView.findViewById(R.id.nutrition_IMG_nut1);
//            nutrition_IMG_nut2 = itemView.findViewById(R.id.nutrition_IMG_nut2);
//            nutrition_IMG_nut3 = itemView.findViewById(R.id.nutrition_IMG_nut3);
//            nutrition_IMG_nut4 = itemView.findViewById(R.id.nutrition_IMG_nut4);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.IngredientClicked(getIngredient(getAdapterPosition()), getAdapterPosition());
//                }
//            });
//        }
//    }
//}