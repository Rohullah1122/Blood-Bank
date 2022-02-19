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
import com.example.bload_bank.databinding.LayoutSavesBinding;

import java.util.ArrayList;

public class adabter_save extends RecyclerView.Adapter<adabter_save.saveviewholder> {
    Context context;
    ArrayList<cls_user> ALSU;

    public adabter_save(Context context, ArrayList<cls_user> ALSU) {
        this.context = context;
        this.ALSU = ALSU;
    }

    @NonNull
    @Override
    public saveviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_saves,parent,false);
        return new saveviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull saveviewholder holder, int position) {
        cls_user user = ALSU.get(position);
        holder.binding.tvsbload.setText(user.getBlooadgroup());
        holder.binding.tvslocation.setText(user.getLocation());
        holder.binding.txtsname.setText(user.getUname());
        Glide.with(context).load(user.getImg()).into(holder.binding.imgsuser);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,mail,phone,location,blogoroup;
                Intent intent = new Intent(context, activity_moreinfo.class);
                intent.putExtra("name",user.getUname());
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
        return ALSU.size();
    }

    public class saveviewholder extends RecyclerView.ViewHolder {
        LayoutSavesBinding binding;
        public saveviewholder(@NonNull View itemView) {
            super(itemView);
            binding = LayoutSavesBinding.bind(itemView);
        }
    }
}
