package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.chatapp.Adapters.GroupChatAdapter;
import com.example.chatapp.Models.GroupChat;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class GroupChatFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatGroups,userInfo,userChatGroups;
    FirebaseUser currentUser;
    RecyclerView recyclerView;
    private GroupChatAdapter adapter;

    public GroupChatFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatGroups = firebaseDatabase.getReference("Groups");
        userChatGroups = firebaseDatabase.getReference("UserChatGroup");
        userInfo = firebaseDatabase.getReference("User");
        currentUser = mAuth.getCurrentUser();
        recyclerView = view.findViewById(R.id.group_chat_rv);

        setUpRecyclerView();
        showAllGroups();

        return view;
    }

    private void setUpRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new GroupChatAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void showAllGroups() {
        final List<GroupChat> groupChatList = new ArrayList<>();
        chatGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot ds : children)
                {
                    GroupChat value = ds.getValue(GroupChat.class);
                    groupChatList.add(value);
                    adapter.setData(getActivity(),groupChatList);
                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}