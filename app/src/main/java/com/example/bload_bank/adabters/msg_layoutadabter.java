package com.example.bload_bank.adabters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bload_bank.R;
import com.example.bload_bank.activities.message_activity;
import com.example.bload_bank.classess.cls_msg;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.MsgUsersBinding;

import java.util.ArrayList;

import static com.example.bload_bank.R.id.imguser;

public class msg_layoutadabter extends RecyclerView.Adapter<msg_layoutadabter.RVUSER>{
    Context context;
    ArrayList<cls_msg> ALU;

    public msg_layoutadabter(Context context, ArrayList<cls_msg> ALU) {
        this.context = context;
        this.ALU = ALU;
    }

    @NonNull
    @Override
    public RVUSER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_users,parent,false);
        return new RVUSER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVUSER holder, int position) {
        cls_msg msguser = ALU.get(position);
        holder.binding.userName.setText(msguser.getUsername());
        Glide.with(context).load(msguser.getUserimg())
                .placeholder(R.drawable.profile)
                .into(holder.binding.imguser);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, message_activity.class);
                intent.putExtra("Uid",msguser.getSenderID());
                intent.putExtra("Username",msguser.getUserimg());
                intent.putExtra("image1", msguser.getUserimg());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return ALU.size();
    }

    public class RVUSER extends RecyclerView.ViewHolder {
        MsgUsersBinding binding;
        public RVUSER(@NonNull View itemView) {
            super(itemView);
            binding = MsgUsersBinding.bind(itemView);

        }
    }
}
