package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class search_bloadgroup extends AppCompatActivity {
    AutoCompleteTextView txtbsearch;
    RecyclerView rvb;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ArrayList<cls_user> ALH;
    cls_adater_gv_layout gv_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bloadgroup);
        txtbsearch = findViewById(R.id.txtsearchbload);
        rvb = findViewById(R.id.gvsearchbload);
        database = FirebaseDatabase.getInstance();
        ALH = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<String>(search_bloadgroup.this, android.R.layout.simple_list_item_1);
        gv_layout = new cls_adater_gv_layout(search_bloadgroup.this, ALH);



        rvb.setAdapter(gv_layout);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String location = ds.child("location").getValue(String.class);
                    String Bloadgroup = ds.child("blooadgroup").getValue(String.class);
                    autoComplete.add(Bloadgroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        txtbsearch.setAdapter(autoComplete);
        txtbsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bloadgroup = txtbsearch.getText().toString();
                database.getInstance().getReference().child("Users")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ALH.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    cls_user users = snapshot1.getValue(cls_user.class);
                                    if (users.getBlooadgroup().equals(bloadgroup)) {
                                        ALH.add(users);
                                        gv_layout.notifyDataSetChanged();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(search_bloadgroup.this, "not Working", Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });

    }

    public void select(String Bloadgroup){

        database.getInstance().getReference().child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ALH.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            cls_user users = snapshot1.getValue(cls_user.class);
                            if (users.getBlooadgroup().equals(Bloadgroup)) {
                                ALH.add(users);
                                gv_layout.notifyDataSetChanged();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(search_bloadgroup.this, "not Working", Toast.LENGTH_SHORT).show();
                    }
                });





    }


}