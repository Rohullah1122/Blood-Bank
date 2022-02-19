package com.example.bload_bank.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.bload_bank.R;
import com.example.bload_bank.adabters.adabter_save;
import com.example.bload_bank.classess.cls_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class fragment_save extends Fragment {
    RecyclerView rvsaves;
    FirebaseAuth auth;
    FirebaseDatabase database;
    adabter_save adabter_save;
    ArrayList<cls_user> ALU;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public fragment_save() {

    }


    public static fragment_save newInstance(String param1, String param2) {
        fragment_save fragment = new fragment_save();
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
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rvsaves = (RecyclerView) view.findViewById(R.id.GVsaves);
        ALU = new ArrayList<>();
        adabter_save = new adabter_save(getContext(),ALU);
        rvsaves.setAdapter(adabter_save);


        database.getReference().child("Saves").child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ALU.clear();
                        for (DataSnapshot snapshot1 :snapshot.getChildren()){
                            cls_user clsHouseinfo = snapshot1.getValue(cls_user.class);
                                ALU.add(clsHouseinfo);
                        }
                        adabter_save.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        return  view;
    }
}