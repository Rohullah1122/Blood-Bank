package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class user_profile extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    Uri imgstore;
    String name,phone,loc,mail,blodtype;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(user_profile.this);

        binding.userimage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });


        database = FirebaseDatabase.getInstance();
        database.getReference("Users")
                .child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cls_user loginuser = snapshot.getValue(cls_user.class);
                        if (loginuser != null){

                             name = loginuser.getUname();
                             phone  = loginuser.getPhone();
                             loc = loginuser.getLocation();
                             blodtype = loginuser.getBlooadgroup();
                            mail = loginuser.getEmail().toString();
                            binding.txtnamee.setText(name);
                            String strArray [] = blodtype. split(" ");
                            binding.ptxtphone.setText(phone);
                            binding.ptxtlocation.setText(loc);
                            binding.spinnerKhon2.setAdapter(new ArrayAdapter<String>(user_profile.this,android.R.layout.simple_spinner_item, strArray));

                            Glide.with(user_profile.this)
                                    .load(loginuser.getImg())
                                    .into(binding.userimage1);
                        }else{
                            Toast.makeText(user_profile.this, "Can not load data", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(user_profile.this, "Did not work", Toast.LENGTH_SHORT).show();
                    }
                });




        final String blooadtype[] =
                {"A", "B", "AB", "O",};
        ArrayAdapter myAdapter = new ArrayAdapter<String>(user_profile.this,
                android.R.layout.simple_spinner_item, blooadtype);

        binding.spinnerKhon2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                binding.spinnerKhon2.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = binding.txtnamee.getText().toString();
                String phon =binding.ptxtphone.getText().toString();
                String Location = binding.ptxtlocation.getText().toString();
                String btpe1 = binding.spinnerKhon2.getSelectedItem().toString();
                dialog.show();

                if (imgstore != null) {
                    StorageReference reference = storage.getReference().child("userimg").child(auth.getUid());
                    reference.putFile(imgstore).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageuri = uri.toString();
                                        if (task.isSuccessful()) {
                                            String uid = auth.getUid();
                                            cls_user user = new cls_user(uid, namee, phon, Location, mail, imageuri, btpe1);
                                            database.getReference("Users")
                                                    .child(uid)
                                                    .setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            dialog.dismiss();
                                                            select();


                                                        }
                                                    });
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(user_profile.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });

                            }
                        }
                    });
                }
//                }else if(){
//
//
//
                 else{

                    String uid = auth.getUid();
                    cls_user user = new cls_user(uid, name, phone, loc, mail, "no image", btpe1);
                    database.getReference("Users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(user_profile.this, "unSuccefully", Toast.LENGTH_SHORT).show();

                                }
                            });




                }
            }
        });




    }




    private void select(){

        database = FirebaseDatabase.getInstance();
        database.getReference("Users")
                .child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cls_user loginuser = snapshot.getValue(cls_user.class);
                        if (loginuser != null){
                            String name = loginuser.getUname();
                            String phone  = loginuser.getPhone();
                            String loc = loginuser.getLocation();
                            String mail;
                            String blodtype = loginuser.getBlooadgroup();
                            mail = loginuser.getEmail().toString();
                            binding.txtnamee.setText(name);
                            String strArray [] = blodtype. split(" ");
                            binding.ptxtphone.setText(phone);
                            binding.ptxtlocation.setText(loc);
                            binding.spinnerKhon2.setAdapter(new ArrayAdapter<String>(user_profile.this,android.R.layout.simple_spinner_item, strArray));

                            Glide.with(user_profile.this)
                                    .load(loginuser.getImg())
                                    .into(binding.userimage1);
                        }else{
                            Toast.makeText(user_profile.this, "Can not load data", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(user_profile.this, "Did not work", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (data != null) {
                binding.userimage1.setImageURI(data.getData());
                imgstore = data.getData();

            }    }
    }

}