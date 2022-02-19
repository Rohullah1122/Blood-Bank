package com.example.bload_bank.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bload_bank.R;
import com.example.bload_bank.adabters.cls_adater_gv_layout;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.LayoutUserinfoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class fragment_layout extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<cls_user> ALU;
    cls_user user;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;
    RecyclerView recyclerView;
    cls_adater_gv_layout adabter_gv_layout;
    private String mParam1;
    private String mParam2;

    public fragment_layout( ) {


    }


    public static fragment_layout newInstance(String param1, String param2) {
        fragment_layout fragment = new fragment_layout();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvlayout);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        ALU = new ArrayList<>();
        adabter_gv_layout = new cls_adater_gv_layout(getContext(),ALU);
        recyclerView.setAdapter(adabter_gv_layout);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ALU.clear();
                if (snapshot.exists()){
                    dialog.dismiss();
                    for (DataSnapshot snapshot1 :snapshot.getChildren()){
                        cls_user user1 = snapshot1.getValue(cls_user.class);
                        ALU.add(user1);}
                }
                adabter_gv_layout.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return view;

    }
}