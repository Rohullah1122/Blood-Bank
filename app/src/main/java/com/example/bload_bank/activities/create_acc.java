package com.example.bload_bank.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.ActivityCreateAccBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class create_acc extends AppCompatActivity {
    ActivityCreateAccBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseFirestore firestore;
    Uri imgstore;
    FirebaseStorage storage;
    WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST1 = 999;
    private final static int PLACE_PICKER_REQUEST2 = 9991;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();
        account = GoogleSignIn.getLastSignedInAccount(create_acc.this);
        wifiManager= (WifiManager) this.getApplicationContext().getSystemService(create_acc.WIFI_SERVICE);
        final String blooadtype[] =
                {"A", "B", "AB", "O",};
        ArrayAdapter myAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, blooadtype);
        binding.spinnerKhon2.setAdapter(myAdapter);

        binding.userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PLACE_PICKER_REQUEST2);
            }
        });

        String mail = account.getEmail().toString();
//            getIntent().getStringExtra("mail");


        binding.ptxtlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disable Wifi
                wifiManager.setWifiEnabled(false);
                openPlacePicker();
            }
        });


        binding.btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, phone, loc, bloadtype;
                name = binding.txtnamee.getText().toString();
                phone = binding.ptxtphone.getText().toString();
                bloadtype = binding.spinnerKhon2.getSelectedItem().toString();
                loc = binding.ptxtlocation.getText().toString();


                if (name.equals("")) {
                    binding.txtnamee.setError("لوطفن نام خودرا ازافه نماید");
                    binding.txtnamee.requestFocus();
                    return;
                }


                if (phone.equals("")) {
                    binding.ptxtphone.setError("لوطفن شماره خودرا ازافه نماید");
                    binding.ptxtphone.requestFocus();
                    return;
                }
                if (loc.equals("")) {
                    binding.ptxtlocation.setError("لوطفن موقید خودرا ازافه نماید");
                    binding.ptxtlocation.requestFocus();
                    return;
                }
                if (bloadtype.equals("")) {
                    Toast.makeText(create_acc.this, "لوطفن نوید خون خودرا ازافه نماید", Toast.LENGTH_SHORT).show();
                    binding.spinnerKhon2.requestFocus();
                    return;
                }


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
                                            cls_user user = new cls_user(uid, name, phone, loc, mail, imageuri, bloadtype);
                                            database.getReference()
                                                    .child("Users")
                                                    .child(uid)
                                                    .setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent = new Intent(create_acc.this, Main_layout.class);
                                                            startActivity(intent);
                                                            finishAffinity();
                                                        }
                                                    });
                                        }
                                    }
                                });

                            }
                        }
                    });

                } else {
                    String uid = auth.getUid();
                    cls_user user = new cls_user(uid, name, phone, loc, mail, "no image", bloadtype);
                    database.getReference()
                            .child("Users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(create_acc.this, Main_layout.class);
                                    startActivity(intent);
                                    finishAffinity();

                                }
                            });

                }

            }
        });


    }

    private void openPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST1);

            //Enable Wifi
            wifiManager.setWifiEnabled(true);

        } catch (GooglePlayServicesRepairableException e) {
            Log.d("Exception", e.getMessage());

            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("Exception", e.getMessage());

            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST2){
            if (data != null) {
                binding.userimage.setImageURI(data.getData());
                imgstore = data.getData();

        }    } else if (requestCode == PLACE_PICKER_REQUEST1) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case PLACE_PICKER_REQUEST1:
                        Place place = PlacePicker.getPlace(create_acc.this, data);

                        double latitude = place.getLatLng().latitude;
                        double longitude = place.getLatLng().longitude;
                        String PlaceLatLng = String.valueOf(latitude) + " , " + String.valueOf(longitude);
                        binding.ptxtlocation.setText(PlaceLatLng.toString());

                }
            }
        }
    }
}







