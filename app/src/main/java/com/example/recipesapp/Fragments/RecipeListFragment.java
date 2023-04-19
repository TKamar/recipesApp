package com.example.recipesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.Activities.CreateRecipeActivity;
import com.example.recipesapp.Activities.ShareContactsActivity;
import com.example.recipesapp.Adapters.RecipeAdapter;
import com.example.recipesapp.Constants.Keys;
import com.example.recipesapp.Firebase.DataManager;
import com.example.recipesapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


public class RecipeListFragment extends Fragment {

    private View view;
    private RecyclerView listRecycler;

    private DataManager dataManager = DataManager.getInstance();
    private DatabaseReference groupRecipesRef, recipeRef;
    private RecipeAdapter adapter;
    private FloatingActionButton main_FAB_fab;
    private MaterialToolbar main_TOB_up;


    public RecipeListFragment() {
        // Required empty public constructor
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recip_list, container, false);
        main_FAB_fab = getActivity().findViewById(R.id.main_FAB_fab);
        main_TOB_up = getActivity().findViewById(R.id.main_TOB_up);
        main_TOB_up.setTitle("Group Recipes");
        main_TOB_up.getMenu().getItem(0).setVisible(true).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), ShareContactsActivity.class);
                startActivity(intent);
                return false;
            }
        });

        /**
         * Add button clicked = Move to add new Group activity
         * this activity wont close
         */
        main_FAB_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Open Create new Group activity
                 */
                startActivity(new Intent(getActivity(), CreateRecipeActivity.class));
            }
        });

        listRecycler = view.findViewById(R.id.recipe_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        listRecycler.setLayoutManager(gridLayoutManager);

        groupRecipesRef = dataManager.groupsListReference().child(dataManager.getCurrentIdGroup()).child(Keys.KEY_GROUP_RECIPES_LIST);
        recipeRef = dataManager.recipessListReference();
        recipeRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String rem = snapshot.getKey();
                groupRecipesRef.child(rem).removeValue();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //init Adapter
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(groupRecipesRef, String.class)
                .build();
        adapter = new RecipeAdapter(options, this.getContext());
        listRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}