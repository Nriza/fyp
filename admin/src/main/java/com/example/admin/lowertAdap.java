package com.example.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Create the adapter by extending RecyclerView.Adapter. This custom ViewHolder will give access to your views
public class lowertAdap extends RecyclerView.Adapter<lowertAdap.ViewHolder> {
    // Declare variables to store data from the constructor
    Context context;
    String[] programNameList;
    int[] images;
    String patient, username, status;
    AlertDialog dialog;

    // Create a static inner class and provide references to all the Views for each data item.
    // This is particularly useful for caching the Views within the item layout for fast access.
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Declare member variables for all the Views in a row
        TextView rowName;
        TextView rowDescription;
        ImageView rowImage;
        // Create a constructor that accepts the entire row and search the View hierarchy to find each subview
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Store the item subviews in member variables
            rowName = itemView.findViewById(R.id.textView1);
            rowDescription = itemView.findViewById(R.id.textView2);
            rowImage = itemView.findViewById(R.id.imageView);
        }
    }
    // Provide a suitable constructor
    public lowertAdap(Context context, String[] programNameList, int[] images, String username, String status, String patient){
        // Initialize the class scope variables with values received from constructor
        this.context = context;
        this.programNameList = programNameList;
        this.images = images;
        this.username = username;
        this.status = username;
        this.patient = patient;
    }

    // Create new views to be invoked by the layout manager
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a LayoutInflater object
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.lowetteeth, parent, false);
        // To attach OnClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rowName = v.findViewById(R.id.textView1);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView rowName = v.findViewById(R.id.textView1);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        //in AlertDialog Layout
                        View view = inflater.inflate(R.layout.treatment_popup, null);
                        TextView title = view.findViewById(R.id.title);
                        TextView add_name = view.findViewById(R.id.tvadd_name);
                        Button btnextraction = view.findViewById(R.id.extraction);
                        Button btnfilling = view.findViewById(R.id.filling);

                        title.setText("ADD TREATMENT");
                        add_name.setText(rowName.getText().toString());
                        btnextraction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String,Object> map = new HashMap<>();
                                map.put("teethnum",rowName.getText().toString());
                                map.put("status","ongoing");
                                map.put("type","extraction");
                                map.put("username",patient);
                                FirebaseDatabase.getInstance().getReference("Treatment").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, Treatment.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("patient", patient);
                                        intent.putExtra("status", status);
                                        context.startActivity(intent);

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
                            }
                        });

                        btnfilling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String,Object> map = new HashMap<>();
                                map.put("teethnum",rowName.getText().toString());
                                map.put("status","ongoing");
                                map.put("type","filling");
                                map.put("username",patient);
                                FirebaseDatabase.getInstance().getReference("Treatment").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Successfully Added", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context, Treatment.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("patient", patient);
                                        intent.putExtra("status", status);
                                        context.startActivity(intent);

                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                dialog.dismiss();
                            }
                        });

                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setLayout(1000, 950);
                    }
                });

            }
        });

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view to be invoked by the layout manager
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the contents of the View with that element
        holder.rowName.setText(programNameList[position]);
        holder.rowImage.setImageResource(images[position]);
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return programNameList.length;
    }
}