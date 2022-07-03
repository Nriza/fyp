package com.example.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.Myviewholder> {

    Context context;
    AlertDialog dialog;
    String username, status;
    ArrayList<stockList>list;

    public StockAdapter(Context context, ArrayList<stockList> list, String username, String status) {
        this.context = context;
        this.list = list;
        this.username = username;
        this.status = status;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tools, parent, false);
        return  new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        stockList stockList = list.get(position);
        holder.name.setText(stockList.getName());
        holder.quantity.setText(stockList.getQuantity());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //in AlertDialog Layout
        View view = inflater.inflate(R.layout.update_popup, null);
        TextView title = view.findViewById(R.id.title);
        EditText add_name = view.findViewById(R.id.add_name);
        EditText add_quan = view.findViewById(R.id.add_quan);
        Button btn = view.findViewById(R.id.updatebtn);

        title.setText("UPDATE TOOL");
        add_name.setText(holder.name.getText());
        add_quan.setText(holder.quantity.getText());
        btn.setText("Update");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Stock");

                databaseReference.orderByChild("name").equalTo(holder.name.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                String key = datas.getKey();
                                databaseReference.child(key).child("name").setValue(add_name.getText().toString());
                                databaseReference.child(key).child("quantity").setValue(add_quan.getText().toString());
                                Intent intent = new Intent(context, stockFragment.class);
                                intent.putExtra("username", username);
                                intent.putExtra("role", status);
                                context.startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                });
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();

        holder.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.getWindow().setLayout(1000, 950);
            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("You want to delete this tool");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sname = holder.name.getText().toString();
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Stock");

                        ref.orderByChild("name").equalTo(sname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                        datas.getRef().removeValue();
                                        Intent intent = new Intent(context, stockFragment.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("role", status);
                                        context.startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterlist(ArrayList<stockList> filteredlist){
        list = filteredlist;
        notifyDataSetChanged();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {

        TextView name, quantity;
        Button updatebtn, deletebtn;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            updatebtn = (Button) itemView.findViewById(R.id.editbtn);
            deletebtn = (Button) itemView.findViewById(R.id.deletebtn);
            name = (TextView) itemView.findViewById(R.id.tvname);
            quantity = (TextView) itemView.findViewById(R.id.tvquantity);

        }

    }

}
