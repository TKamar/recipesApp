package com.example.recipesapp.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.Fragments.RecipeListFragment;
import com.example.recipesapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class GroupAdapter extends FirebaseRecyclerAdapter<String, GroupAdapter.GroupHolder> {

    private DataManager dataManager = DataManager.getInstance();
    private DatabaseReference GroupRef = dataManager.groupsListReference();
    private Context context;

    /**
     * Initialize Group Adapter
     * @param options
     * @param context
     */
    public GroupAdapter(@NonNull FirebaseRecyclerOptions<String> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupHolder holder, int position, @NonNull String model) {
        String groupIDs = getRef(position).getKey();
        GroupRef.child(groupIDs).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child(Keys.KEY_GROUP_NAME).getValue(String.class);
                    String des = snapshot.child(Keys.KEY_GROUP_DESCRIPTION).getValue(String.class);
                    String img = snapshot.child(Keys.KEY_GROUP_IMG).getValue(String.class);

                    Glide.with(context)
                            .load(img)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.group_TXV_group_name.setText(name);
                                    holder.group_TXV_subtitle_description.setText(des);
                                    holder.group_IMG_img_ltr.setImageResource(R.drawable.ic_image_error);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.group_TXV_group_name.setText(name);
                                    holder.group_TXV_subtitle_description.setText(des);
                                    return false;
                                }
                            })
                            .error(R.drawable.ic_image_error)
                            .override(200, 200)
                            .into(holder.group_IMG_img_ltr);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dataManager.setCurrentIdGroup(groupIDs);
                            ((FragmentActivity) context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack("groups fragment")
                                    .replace(R.id.main_FRG_container, new RecipeListFragment())
                                    .commit();
                        }
                    });
                }
            }
        });


    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card_ltr, parent, false);
        GroupHolder holder = new GroupHolder(view);

        return holder;
    }


    public static class GroupHolder extends RecyclerView.ViewHolder {

        public ShapeableImageView group_IMG_img_ltr;
        public MaterialTextView group_TXV_group_name;
        public MaterialTextView group_TXV_subtitle_description;

        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            this.group_IMG_img_ltr = itemView.findViewById(R.id.group_IMG_img_ltr);
            this.group_TXV_group_name = itemView.findViewById(R.id.group_TXV_group_name_list);
            this.group_TXV_subtitle_description = itemView.findViewById(R.id.group_TXV_subtitle_description_list);
        }
    }

}