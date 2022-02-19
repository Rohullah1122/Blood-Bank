package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.bload_bank.R;
import com.example.bload_bank.classess.cls_rating;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.ActivityMainLayoutBinding;
import com.example.bload_bank.fragments.fragment_layout;
import com.example.bload_bank.fragments.fragment_save;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_layout extends AppCompatActivity{
    ActivityMainLayoutBinding binding;
    Fragment fragment = null;
    FirebaseAuth auth;
    FirebaseDatabase database;
    cls_user user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String uid = auth.getUid();

        database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(cls_user.class);
                if (user != null){
                    View header = binding.drawer.getHeaderView(0);
                    TextView tv1 = (TextView) header.findViewById(R.id.txtname1);
                    ImageView im1 = (ImageView) header.findViewById(R.id.userimage);
                    tv1.setText(user.getUname());
                    Glide.with(Main_layout.this).load(user.getImg()).into(im1);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerlayout, binding.toolbar, R.string.open_navigation, R.string.close_navigation);
        binding.drawerlayout.addDrawerListener(toggle);
        toggle.syncState();







        fragment = new fragment_layout();
        loadfragment(fragment);
        binding.drawerlayout.closeDrawer(GravityCompat.START);
        binding.drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {




                switch (item.getItemId()){


                    case R.id.btn_logout:
                        auth.signOut();
                        Intent intent = new Intent(Main_layout.this,Login.class);
                        startActivity(intent);
                        binding.drawerlayout.closeDrawer(GravityCompat.START);
                        finishAffinity();
                        break;



            case R.id.btn_profile:
//                fragment = new Profile_activity();
//                loadfragment(fragment);
//                binding.drawerlayout.closeDrawer(GravityCompat.START);
                Intent intent11 = new Intent(Main_layout.this,user_profile.class);
                startActivity(intent11);
                break;

            case R.id.btn_saves:

                fragment = new fragment_save ();
                loadfragment(fragment);
                binding.drawerlayout.closeDrawer(GravityCompat.START);
                break;

                    case R.id.btn_home:
                        fragment = new fragment_layout();
                        loadfragment(fragment);
                        binding.drawerlayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.btn_messsage:
                        Intent intent12 = new Intent(Main_layout.this,Messaged_users.class);
                        startActivity(intent12);
                        break;


                    case R.id.btn_feedback:
                        AlertDialog.Builder alert = new AlertDialog.Builder(Main_layout.this);
                        alert.setTitle("درج نظریات");
                        View view = getLayoutInflater().inflate(R.layout.feed_back_layout,null);
                        alert.setView(view);
                        alert.setCancelable(false);
                        alert.setPositiveButton("زخیره", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText Name = view.findViewById(R.id.tvalertd);
                                String feedback = Name.getText().toString();
                                if (feedback.isEmpty()) {
                                    Name.setError("لوطفن بنوسید");
                                    Name.requestFocus();
                                    return;
                                }else{
                                    cls_rating cls_rating = new cls_rating();
                                    cls_rating.setUid(auth.getUid());
                                    cls_rating.setUname(user.getUname());
                                    cls_rating.setFeedback(feedback);
                                    database.getReference().child("feedback").child("feedbacks").setValue(cls_rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Main_layout.this,   "ممنون از نظریات شما" +user.getUname(), Toast.LENGTH_SHORT).show();
                                            alert.setCancelable(true);
                                        }
                                    });
                                }
                            }

                        });

                        alert.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alert.setCancelable(true);
                                    }
                                });

                                alert.show();

                        break;


                    case R.id.btn_rate:
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(Main_layout.this);
                        alert1.setTitle("درج رات");
                        View view1 = getLayoutInflater().inflate(R.layout.rating_layout,null);
                        alert1.setView(view1);
                        alert1.setCancelable(false);

                        alert1.setPositiveButton("زخیره", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RatingBar ratingbar = view1.findViewById(R.id.ratingBar);
                                int rating = ratingbar.getNumStars();
                                if (rating == 0) {
                                    Toast.makeText(Main_layout.this,  "لوطفن رات نماید" +user.getUname(), Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    cls_rating cls_rating = new cls_rating();
                                    cls_rating.setUid(auth.getUid());
                                    cls_rating.setUname(user.getUname());
                                    cls_rating.setUname(rating +"");
                             database.getReference().child("rating").child("rates").setValue(cls_rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void aVoid) {
                                     Toast.makeText(Main_layout.this, "ممنون از نظریات شما", Toast.LENGTH_SHORT).show();
                                     alert1.setCancelable(true);
                                 }
                             });
                                }
                            }

                        });

                        alert1.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert1.setCancelable(true);
                            }
                        });

                        alert1.show();

                        break;




                    case R.id.btn_search:


                        AlertDialog.Builder alert2 = new AlertDialog.Builder(Main_layout.this);
                        View view3 = getLayoutInflater().inflate(R.layout.search_layout,null);
                        alert2.setView(view3);
                        alert2.setCancelable(false);
                        Button btn_searchl,searchb;
                        btn_searchl = view3.findViewById(R.id.button_bload);
                        searchb = view3.findViewById(R.id.btn_location);
                        btn_searchl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Main_layout.this, search_bloadgroup.class);
                                startActivity(intent);
                                alert2.setCancelable(true);

                            }
                        });

                        searchb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Main_layout.this, search_activity.class);
                                startActivity(intent);
                                alert2.setCancelable(true);
                            }
                        });


                        alert2.setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alert2.setCancelable(true);
                            }
                        });

                        alert2.show();
                          break;

                }
                return true;
            }
        });

    }

    public void loadfragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment).commit();
        binding.drawerlayout.closeDrawer(GravityCompat.START);
    }


    public void headerlayout(String name,String imge){
        View view = getLayoutInflater().inflate(R.layout.header_layout,null);
        ImageView img = (ImageView) view.findViewById(R.id.userimage);
        TextView tv = (TextView) view.findViewById(R.id.txtname1);
        Glide.with(Main_layout.this)
        .load(imge)
        .into(img);
        tv.setText(name);

    }



}