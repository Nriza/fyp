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

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.Myviewholder> {

    Context context;
    public boolean isClickable = true;
    String username, status;

    ArrayList<AppList>list;

    public AppAdapter(Context context, ArrayList<AppList> list, String username, String status) {
        this.context = context;
        this.list = list;
        this.username = username;
        this.status = status;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return  new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        AppList appList = list.get(position);
        holder.usernames.setText(appList.getUsername());
        holder.date.setText(appList.getDate());
        holder.time.setText(appList.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView usernames, date, time;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            usernames = itemView.findViewById(R.id.tvusername);
            date = itemView.findViewById(R.id.tvdate);
            time = itemView.findViewById(R.id.tvtime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(!isClickable){
                return;
            }
            else {
                int position = getAdapterPosition();
                Intent intent = new Intent(context, AppTreatment.class);
                intent.putExtra("patient", list.get(position).getUsername());
                intent.putExtra("username", username);
                intent.putExtra("role", status);
                context.startActivity(intent);
            }

        }
    }

}
