package com.example.clinicsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class treatAdapter extends RecyclerView.Adapter<treatAdapter.Myviewholder> {

    Context context;

    ArrayList<treatList> list;

    public treatAdapter(Context context, ArrayList<treatList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return  new Myviewholder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        treatList treatList = list.get(position);
        holder.teethnum.setText(treatList.getTeethnum());
        holder.tratment_type.setText(treatList.getType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Myviewholder extends RecyclerView.ViewHolder{

        TextView teethnum, tratment_type;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            teethnum = itemView.findViewById(R.id.tvteeth);
            tratment_type = itemView.findViewById(R.id.tvtype);
        }
    }

}
