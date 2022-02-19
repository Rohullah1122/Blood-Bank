package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.ActivityMoreinfoBinding;
import com.example.bload_bank.fragments.bottom_sheetFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_moreinfo extends AppCompatActivity {
        ActivityMoreinfoBinding binding;
        FirebaseAuth auth;
        FirebaseDatabase database;
        DatabaseReference databaseReference;
        cls_user user;
        long hid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoreinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String name,phone,mail,location,bgroup,img;
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        mail = getIntent().getStringExtra("mail");
        location = getIntent().getStringExtra("location");
        bgroup = getIntent().getStringExtra("bgroup");
        img = getIntent().getStringExtra("img");
        String hid1 = getIntent().getStringExtra("hid");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        binding.txtnamee.setText(name +"____نام");
        binding.ptxtlocation.setText(location   +"_____آدرس");
        binding.ptxtphone.setText(phone +"_________موبایل");
        binding.ptxtmail.setText(mail +"____ایمیل آدرس");
        binding.boodgroup.setText(bgroup  +"________نویت خون");
        Glide.with(activity_moreinfo.this).load(img)
                .into(binding.userimage);






        binding.btnSaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getInstance().getReference().child("Saves").child(auth.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    cls_user users = snapshot1.getValue(cls_user.class);
                                    if (users.getEmail().equals(mail)) {
                                        Toast.makeText(activity_moreinfo.this, "already exist", Toast.LENGTH_SHORT).show();

                                    }else{
                                        cls_user user = new cls_user(hid1,name,phone,location,mail,img,bgroup);
                                        database.getReference("Saves")
                                                .child(auth.getUid())
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(activity_moreinfo.this, "Saved", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(activity_moreinfo.this, "not Working", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        binding.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                          Bundle bundle = new Bundle();
                          bundle.putString("userid", hid1);
                          bundle.putString("username",name);
                          bundle.putString("img",img);
                          bundle.putString("phone",phone);
                          bottom_sheetFragment fragment = new bottom_sheetFragment();
                          fragment.show(getSupportFragmentManager(),fragment.getTag());
                          fragment.setArguments(bundle);
            }
        });




    }
}