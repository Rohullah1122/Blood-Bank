package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bload_bank.R;
import com.example.bload_bank.adabters.msg_layoutadabter;
import com.example.bload_bank.classess.cls_msg;
import com.example.bload_bank.classess.cls_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Messaged_users extends AppCompatActivity {
    RecyclerView rvmsgusers;
    ArrayList<cls_msg> ALU;
    FirebaseAuth auth;
    DatabaseReference database;
    com.example.bload_bank.adabters.
    msg_layoutadabter msg_layoutadabter;
    String Senderid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaged_users);
        rvmsgusers = findViewById(R.id.rvmsgusers);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("chats");
        ALU = new ArrayList<>();
        msg_layoutadabter = new msg_layoutadabter(Messaged_users.this,ALU);
        rvmsgusers.setAdapter(msg_layoutadabter);
        String randomKey = database.push().getKey();
        database.child(randomKey).child("SenderID").equals(auth.getUid());


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ALU.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    cls_msg cls_msg = snapshot1.getValue(cls_msg.class);

                    if (!cls_msg.getSenderID().equals(FirebaseAuth.getInstance().getUid())){
                        ALU.add(cls_msg);
                    }

                }

                msg_layoutadabter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}