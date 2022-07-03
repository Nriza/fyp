package com.example.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class hisAdapter extends RecyclerView.Adapter<hisAdapter.Myviewholder> {

    Context context;

    ArrayList<treatList>list;

    public hisAdapter(Context context, ArrayList<treatList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_his, parent, false);
        return  new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        treatList treatList = list.get(position);
        holder.teethnum.setText(treatList.teethnum);
        holder.type.setText(treatList.getType());
        holder.status.setText(treatList.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView teethnum, type, status;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            teethnum = itemView.findViewById(R.id.tvname);
            type = itemView.findViewById(R.id.tvtype);
            status = itemView.findViewById(R.id.tvstatus);

        }

        @Override
        public void onClick(View v) {

        }
    }

}
