package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.bload_bank.adabters.MessageAdabter;
import com.example.bload_bank.classess.cls_msg;
import com.example.bload_bank.databinding.ActivityMessageActivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class message_activity extends AppCompatActivity {
    ActivityMessageActivityBinding binding;
    MessageAdabter adapter;
    ArrayList<cls_msg> messages;

    String senderRoom, receiverRoom;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;
    String senderUid;
    String receiverUid;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);

        messages = new ArrayList<>();


        name = getIntent().getStringExtra("username");
        String profile = getIntent().getStringExtra("imag");
        receiverUid = getIntent().getStringExtra("userid");


        binding.tvusername.setText(name);

        Glide.with(message_activity.this).load(profile)
                .into(binding.userimagemsg);




        senderUid = FirebaseAuth.getInstance().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        adapter = new MessageAdabter(this, messages, senderRoom, receiverRoom);
        binding.rvchatting.setLayoutManager(new LinearLayoutManager(this));
        binding.rvchatting.setAdapter(adapter);

        database.getReference().child("chats")
                .child(receiverRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            cls_msg messaging = snapshot1.getValue(cls_msg.class);
                            messages.add(messaging);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        binding.btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.keyboard.getText().toString();

                Date date = new Date();
                cls_msg message = new cls_msg(senderUid,messageTxt,name,profile, date.getTime());
                binding.keyboard.setText("");

                String randomKey = database.getReference().push().getKey();

                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMsgs());
                lastMsgObj.put("lastMsgTime", date.getTime());
                lastMsgObj.put("lastMsgname",message.getUsername());
                lastMsgObj.put("lastMsgProfile",message.getUserimg());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .child(randomKey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                });
            }
        });
    }

}