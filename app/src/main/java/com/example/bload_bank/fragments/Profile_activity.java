package com.example.bload_bank.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bload_bank.R;
import com.example.bload_bank.classess.cls_user;
import com.google.android.gms.auth.api.signin.internal.Storage;
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

public class Profile_activity extends Fragment {
    EditText name1,phone1,location1;
    Spinner btpe;
    ImageView img1;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Context context;
    String mail;
    FirebaseStorage storage;
    Button btn_update;
    Uri imgstore;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Profile_activity() {



    }

    public static Profile_activity newInstance(String param1, String param2) {
        Profile_activity fragment = new Profile_activity();
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
        View view =  inflater.inflate(R.layout.fragment_profile_activity, container, false);
        name1 = (EditText) view.findViewById(R.id.txtnamee);
        phone1 = (EditText) view.findViewById(R.id.ptxtphone);
        location1 =(EditText) view.findViewById(R.id.ptxtlocation);
        btpe = (Spinner) view.findViewById(R.id.spinner_khon2);
        img1 = (ImageView) view.findViewById(R.id.userimage1);
        storage = FirebaseStorage.getInstance();
        btn_update = (Button)view.findViewById(R.id.btn_update);
        auth = FirebaseAuth.getInstance();
        String userid = auth.getUid();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);

            }
        });

        final String blooadtype[] =
                {"A", "B", "AB", "O",};
        ArrayAdapter myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, blooadtype);

        btpe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                btpe.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        select();

    btn_update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String namee = name1.getText().toString();
            String phon = phone1.getText().toString();
            String Location = location1.getText().toString();
            String btpe1 = btpe.getSelectedItem().toString();

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
                                        database.getReference()
                                                .child("Users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        select();

                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getActivity(), "unsuccessful", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }
                    }
                });




            }
        }
    });










        return view;
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
                            String blodtype = loginuser.getBlooadgroup();
                            mail = loginuser.getEmail().toString();
                            name1.setText(name);
                            String strArray [] = blodtype. split(" ");
                            phone1.setText(phone);
                            location1.setText(loc);
                            phone1.setText(phone);
                            btpe.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, strArray));

                            Glide.with(getContext())
                                    .load(loginuser.getImg())
                                    .into(img1);
                        }else{
                            Toast.makeText(getContext(), "Can not load data", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Did not work", Toast.LENGTH_SHORT).show();
                    }
                });


    }









}