package com.example.bload_bank.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bload_bank.R;
import com.example.bload_bank.activities.message_activity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class bottom_sheetFragment extends BottomSheetDialogFragment {
    Button btncall,btnmsg;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public bottom_sheetFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static bottom_sheetFragment newInstance(String param1, String param2) {
        bottom_sheetFragment fragment = new bottom_sheetFragment();
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
        View view =  inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        btncall = view.findViewById(R.id.btncall);
        btnmsg = view.findViewById(R.id.btnmsg);

        String name = getArguments().getString("username");
        String userid = getArguments().getString("userid");
        String bimg = getArguments().getString("img");
        String phone = getArguments().getString("phone");

        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    startActivity(callIntent);
                }
            }
        });

        btnmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), message_activity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("username",name);
                intent.putExtra("imag",bimg);
                startActivity(intent);


            }
        });

        return view;
    }
}