package com.example.bload_bank.adabters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bload_bank.R;
import com.example.bload_bank.activities.activity_moreinfo;
import com.example.bload_bank.classess.cls_user;
import com.example.bload_bank.databinding.LayoutUserinfoBinding;

import java.util.ArrayList;

public class cls_adater_gv_layout extends RecyclerView.Adapter<cls_adater_gv_layout.recyclerviewholder> {
        Context context;
        ArrayList<cls_user> ALuserinfo;

    public cls_adater_gv_layout(Context context, ArrayList<cls_user> ALuserinfo) {
        this.context = context;
        this.ALuserinfo = ALuserinfo;
    }

    @NonNull
    @Override
    public recyclerviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_userinfo,parent,false);
        return new recyclerviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerviewholder holder, int position) {
        cls_user user = ALuserinfo.get(position);
        holder.binding.txtuname.setText(user.getUname()  +"_____نام");
        holder.binding.txtblooad.setText(user.getBlooadgroup()  +"_____ نویت خون");
        holder.binding.txtlocation.setText(user.getLocation() +"_____آدرس");
        Glide.with(context).load(user.getImg())
                .placeholder(R.drawable.profile)
                .into(holder.binding.imguser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mail,phone,location,blogoroup,hid;
                Intent intent = new Intent(context, activity_moreinfo.class);
                intent.putExtra("name",user.getUname());
                intent.putExtra("hid",user.getHid());
                intent.putExtra("mail",user.getEmail());
                intent.putExtra("bgroup",user.getBlooadgroup());
                intent.putExtra("location",user.getLocation());
                intent.putExtra("phone",user.getPhone());
                intent.putExtra("img",user.getImg());
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return ALuserinfo.size();
    }

    public class recyclerviewholder extends RecyclerView.ViewHolder {
        LayoutUserinfoBinding binding;
        public recyclerviewholder(@NonNull View itemView) {
            super(itemView);
            binding = LayoutUserinfoBinding.bind(itemView);
        }
    }

}
