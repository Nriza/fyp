package com.example.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Treatment extends AppCompatActivity {

    RecyclerView recyclerView, recyclerView1, recyclerView2;
    DatabaseReference databaseReference;
    treatAdapter adapter;
    ArrayList<treatList> list;
    RecyclerView.Adapter programAdapter, bellowadap;
    RecyclerView.LayoutManager layoutmanager;
    String[] uppernum = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16"};
    // Define an integer array to hold the image recourse ids
    int[] upperimg = {R.drawable.t1, R.drawable.t2,
            R.drawable.t3, R.drawable.t4, R.drawable.t5,
            R.drawable.t6, R.drawable.t7, R.drawable.t8,
            R.drawable.t9, R.drawable.t10, R.drawable.t11,
            R.drawable.t12, R.drawable.t13, R.drawable.t14,
            R.drawable.t15, R.drawable.t16};
    String[] bellownum = {"32", "31", "30", "29", "28", "27", "26", "25", "24", "23",
            "22", "21", "20", "19", "18", "17"};
    // Define an integer array to hold the image recourse ids
    int[] bellowimg = {R.drawable.t32, R.drawable.t31,
            R.drawable.t30, R.drawable.t29, R.drawable.t28,
            R.drawable.t27, R.drawable.t26, R.drawable.t25,
            R.drawable.t24, R.drawable.t23, R.drawable.t22,
            R.drawable.t21, R.drawable.t20, R.drawable.t19,
            R.drawable.t18, R.drawable.t17};
    Button scalling;
    TextView history;
    String patient, username, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        Bundle bundle = getIntent().getExtras();
        patient = bundle.getString("patient");
        username = bundle.getString("username");
        status = bundle.getString("status");

        recyclerView = findViewById(R.id.history);
        databaseReference = FirebaseDatabase.getInstance().getReference("Treatment");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        adapter = new treatAdapter(this, list, username, status, patient);
        recyclerView.setAdapter(adapter);

        databaseReference.orderByChild("username").equalTo(patient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String statusDb = dataSnapshot.child("status").getValue().toString();
                    if (statusDb.equals("ongoing")){
                        treatList app = dataSnapshot.getValue(treatList.class);
                        list.add(app);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        recyclerView1 = findViewById(R.id.upper);
        // You may use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView1.setHasFixedSize(true);
        // Use a linear layout manager
        layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutmanager);
        // Create an instance of ProgramAdapter. Pass context and all the array elements to the constructor
        programAdapter = new TeethAdapt(this, uppernum, upperimg, username, status, patient);
        // Finally, attach the adapter with the RecyclerView
        recyclerView1.setAdapter(programAdapter);

        recyclerView2 = findViewById(R.id.bellow);
        // You may use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView2.setHasFixedSize(true);
        // Use a linear layout manager
        layoutmanager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutmanager);
        // Create an instance of ProgramAdapter. Pass context and all the array elements to the constructor
        bellowadap = new lowertAdap(this, bellownum, bellowimg, username, status, patient);
        // Finally, attach the adapter with the RecyclerView
        recyclerView2.setAdapter(bellowadap);

        scalling = findViewById(R.id.add);
        scalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addScaling();
            }
        });

        history = findViewById(R.id.hitext);
        history.setTextColor(Color.parseColor("#000000"));
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setTextColor(Color.parseColor("#f7838d"));
                Intent intent = new Intent(Treatment.this, history.class);
                intent.putExtra("username", username);
                intent.putExtra("patient", patient);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });

    }

    private void addScaling() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Treatment");
        Query query = ref.orderByChild("username").equalTo(patient);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String statusDb = dataSnapshot.child("status").getValue().toString();
                    String typeDb = dataSnapshot.child("type").getValue().toString();
                    if (statusDb.equals("ongoing") && typeDb.equals("scaling")){
                        return;
                    }
                }
                Map<String,Object> map = new HashMap<>();
                map.put("teethnum","all");
                map.put("status","ongoing");
                map.put("type","scaling");
                map.put("username",patient);
                FirebaseDatabase.getInstance().getReference("Treatment").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Treatment.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Treatment.this, Treatment.class);
                        intent.putExtra("username", username);
                        intent.putExtra("patient", patient);
                        intent.putExtra("status", status);
                        startActivity(intent);

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Treatment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}