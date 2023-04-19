package com.example.recipesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.Activities.CreateGroupActivity;
import com.example.recipesapp.Adapters.GroupAdapter;
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

public class GroupListFragment extends Fragment {

    private View view;
    private RecyclerView listRecycler;

    private DataManager dataManager = DataManager.getInstance();
    private DatabaseReference userGroupsRef,GroupRef;
    private GroupAdapter adapter;
    private FloatingActionButton main_FAB_fab;
    private MaterialToolbar main_TOB_up;

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_groups, container, false);

        listRecycler = view.findViewById(R.id.listRecycler);
        listRecycler.setLayoutManager(new LinearLayoutManager(getContext()));



        main_FAB_fab = getActivity().findViewById(R.id.main_FAB_fab);
        main_TOB_up = getActivity().findViewById(R.id.main_TOB_up);
        main_TOB_up.setTitle("My Groups");
        main_TOB_up.getMenu().getItem(0).setVisible(false);


        userGroupsRef = dataManager.usersListReference().child(dataManager.getFirebaseAuth().getUid()).child(Keys.KEY_USER_GROUPS_IDS);
        GroupRef = dataManager.groupsListReference();
        GroupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String rem = snapshot.getKey();
                dataManager.getCurrentUser().getGroupsIds().remove(rem);
                userGroupsRef.child(rem).removeValue();
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

        main_FAB_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add Group to Group Lists", Toast.LENGTH_LONG).show();
                /**
                 * Open Create new Group activity
                 */
                startActivity(new Intent(getContext(), CreateGroupActivity.class));
            }
        });
//Adapter init
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(userGroupsRef,String.class)
                .build();
        adapter = new GroupAdapter(options,this.getContext());
        listRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



}