package com.example.bload_bank.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bload_bank.R;
import com.example.bload_bank.adabters.cls_adater_gv_layout;
import com.example.bload_bank.classess.cls_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_search extends Fragment {
    AutoCompleteTextView txtsearch;
    RecyclerView gvsearch;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<cls_user> ALH;
    cls_adater_gv_layout gv_layout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public fragment_search() {
        // Required empty public constructor


    }


    public static fragment_search newInstance(String param1, String param2) {
        fragment_search fragment = new fragment_search();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        txtsearch = (AutoCompleteTextView) view.findViewById(R.id.txtsearch);
        gvsearch = (RecyclerView) view.findViewById(R.id.gvsearch);
        database = FirebaseDatabase.getInstance();
        ALH = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        gv_layout = new cls_adater_gv_layout(getContext(), ALH);

        gvsearch.setAdapter(gv_layout);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String location = ds.child("location").getValue(String.class);
                    String Bloadgroup = ds.child("blooadgroup").getValue(String.class);
                    autoComplete.add(location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtsearch.setAdapter(autoComplete);
        txtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = txtsearch.getText().toString();
                database.getInstance().getReference().child("Users")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ALH.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    cls_user users = snapshot1.getValue(cls_user.class);
                                    if (users.getLocation().equals(location)) {
                                        ALH.add(users);
                                        gv_layout.notifyDataSetChanged();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "not Working", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });


        return view;

    }
          public void select(String location){

              database.getInstance().getReference().child("Users")
                      .addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              ALH.clear();
                              for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                  cls_user users = snapshot1.getValue(cls_user.class);
                                  if (users.getLocation().equals(location)) {
                                      ALH.add(users);
                                      gv_layout.notifyDataSetChanged();
                                  }
                              }
                          }
                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {
                              Toast.makeText(getContext(), "not Working", Toast.LENGTH_SHORT).show();
                          }
                      });

          }




}