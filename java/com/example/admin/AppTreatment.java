package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppTreatment extends AppCompatActivity {

    RecyclerView recyclerView, recyclerView1, recyclerView2;
    DatabaseReference databaseReference, ref;
    hisAdapter adapter;
    ArrayList<treatList> list;
    String username, status, patient;
    Button competebtn;
    int filling = 0, scaling = 0, extraction = 0, total = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_treatment);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        status = bundle.getString("role");
        patient = bundle.getString("patient");

        recyclerView = findViewById(R.id.history);
        databaseReference = FirebaseDatabase.getInstance().getReference("Treatment");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        adapter = new hisAdapter(this, list);
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

        competebtn = findViewById(R.id.complete);
        competebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
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
    }

    private void complete() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Treatment");

        databaseReference.orderByChild("username").equalTo(patient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String typedb = datas.child("type").getValue().toString();
                        String statusDb = datas.child("status").getValue().toString();
                        if (statusDb.equals("ongoing")){
                            if (typedb.equals("filling")){
                                filling ++ ;
                            }
                            if (typedb.equals("scaling")){
                                scaling ++ ;
                            }
                            if (typedb.equals("extraction")){
                                extraction ++ ;
                            }
                            String key = datas.getKey();
                            //databaseReference.child(key).child("status").setValue("complete");
                        }
                    }
                    Toast.makeText(getApplicationContext(), "" + filling + " " + scaling + " " + extraction, Toast.LENGTH_LONG).show();
                    total = (filling*1) + (scaling*5) + (extraction*6);
                    Toast.makeText(getApplicationContext(), "inside: " + total, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        Toast.makeText(getApplicationContext(), "outside: " + total, Toast.LENGTH_LONG).show();

        /*ref = FirebaseDatabase.getInstance().getReference("Appointment").child(username);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), docappointment.class);
                    intent.putExtra("username", username);
                    intent.putExtra("role", status);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}