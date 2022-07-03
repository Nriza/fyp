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

public class PatientAdap extends RecyclerView.Adapter<PatientAdap.Myviewholder> {

    Context context;
    String nusername, status;
    ArrayList<PatientList> list;

    public PatientAdap(Context context, ArrayList<PatientList> list, String nusername, String status) {
        this.context = context;
        this.list = list;
        this.nusername = nusername;
        this.status = status;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_2, parent, false);
        return  new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        PatientList patientList = list.get(position);
        holder.username.setText(patientList.getUsername());
        holder.email.setText(patientList.getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterlist(ArrayList<PatientList> filteredlist){
        list = filteredlist;
        notifyDataSetChanged();
    }


    public class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView username, email;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvusername);
            email = itemView.findViewById(R.id.tvemail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, Treatment.class);
            intent.putExtra("patient", list.get(position).getUsername());
            intent.putExtra("username", nusername);
            intent.putExtra("status", status);
            context.startActivity(intent);
        }
    }

}

