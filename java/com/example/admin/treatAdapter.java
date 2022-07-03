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
import java.util.Locale;

public class treatAdapter extends RecyclerView.Adapter<treatAdapter.Myviewholder> {

    Context context;
    AlertDialog dialog;
    String username, status, patient;
    ArrayList<treatList>list;

    public treatAdapter(Context context, ArrayList<treatList> list, String username, String status, String patient) {
        this.context = context;
        this.list = list;
        this.username = username;
        this.status = status;
        this.patient = patient;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return  new Myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        treatList stockList = list.get(position);
        holder.name.setText(stockList.getTeethnum());
        holder.type.setText(stockList.getType());

        if (holder.name.getText().toString().equals("all")){
            holder.updatebtn.setEnabled(false);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //in AlertDialog Layout
        View view = inflater.inflate(R.layout.treatment_popup, null);
        TextView title = view.findViewById(R.id.title);
        TextView add_name = view.findViewById(R.id.tvadd_name);
        Button btnextraction = view.findViewById(R.id.extraction);
        Button btnfilling = view.findViewById(R.id.filling);

        title.setText("UPDATE TREATMENT");
        add_name.setText(holder.name.getText());
        btnextraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Treatment");

                databaseReference.orderByChild("username").equalTo(patient).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                String pstatus = datas.child("status").getValue().toString();
                                String ptype = datas.child("type").getValue().toString();
                                String ptheeth = datas.child("teethnum").getValue().toString();
                                if (pstatus.equals("ongoing") && ptype.equals(holder.type.getText().toString()) && ptheeth.equals(holder.name.getText().toString())){
                                    String key = datas.getKey();
                                    databaseReference.child(key).child("type").setValue("extraction");
                                    Intent intent = new Intent(context, Treatment.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("patient", patient);
                                    intent.putExtra("status", status);
                                    context.startActivity(intent);
                                }
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

        btnfilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Treatment");

                databaseReference.orderByChild("username").equalTo(patient).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                String pstatus = datas.child("status").getValue().toString();
                                String ptype = datas.child("type").getValue().toString();
                                String ptheeth = datas.child("teethnum").getValue().toString();
                                if (pstatus.equals("ongoing") && ptype.equals(holder.type.getText().toString()) && ptheeth.equals(holder.name.getText().toString())){
                                    String key = datas.getKey();
                                    databaseReference.child(key).child("type").setValue("filling");
                                    Intent intent = new Intent(context, Treatment.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("patient", patient);
                                    intent.putExtra("status", status);
                                    context.startActivity(intent);
                                }

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
                dialog.getWindow().setLayout(1000, 800);
            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.type.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("You want to delete this tool");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sname = holder.name.getText().toString();
                        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Treatment");

                        ref.orderByChild("teethnum").equalTo(sname).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                        String pname = datas.child("username").getValue().toString();
                                        String pstatus = datas.child("status").getValue().toString();
                                        String ptype = datas.child("type").getValue().toString();
                                        if (pname.equals(patient) && pstatus.equals("ongoing") && ptype.equals(holder.type.getText().toString())){
                                            datas.getRef().removeValue();
                                            Intent intent = new Intent(context, Treatment.class);
                                            intent.putExtra("username", username);
                                            intent.putExtra("patient", patient);
                                            intent.putExtra("status", status);
                                            context.startActivity(intent);
                                        }
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

    public class Myviewholder extends RecyclerView.ViewHolder {

        TextView name, type;
        Button updatebtn, deletebtn;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);

            updatebtn = (Button) itemView.findViewById(R.id.editbtn);
            deletebtn = (Button) itemView.findViewById(R.id.deletebtn);
            name = (TextView) itemView.findViewById(R.id.tvname);
            type = (TextView) itemView.findViewById(R.id.tvtype);

        }

    }

}
