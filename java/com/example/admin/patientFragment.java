package com.example.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class patientFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Bundle bundle;
    String username, status;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    PatientAdap adapter;
    ArrayList<PatientList> list, filterlist;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patientlist);

        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        status = bundle.getString("role");

        recyclerView = findViewById(R.id.patientList);
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        adapter = new PatientAdap(this, list, username, status);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PatientList app = dataSnapshot.getValue(PatientList.class);
                    list.add(app);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        navigationview();

    }

    private void filter(String toString) {
        filterlist = new ArrayList<>();
        for (PatientList item : list){
            if (item.getUsername().toLowerCase().contains(toString.toLowerCase()) || item.getEmail().toLowerCase().contains(toString.toLowerCase())){
                filterlist.add(item);
            }
        }
        adapter.filterlist(filterlist);

    }

    private void navigationview() {
        drawerLayout = findViewById(R.id.drawer_layout1);
        navigationView = findViewById(R.id.navi_view1);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navi_home:
                Intent home = new Intent(getApplicationContext(), docMain.class);
                home.putExtra("username", username);
                home.putExtra("role", status);
                startActivity(home);
                break;
            case R.id.navi_list:
                Intent list = new Intent(getApplicationContext(), patientFragment.class);
                list.putExtra("username", username);
                list.putExtra("role", status);
                startActivity(list);
                break;
            case R.id.navi_Appointment:
                Intent schedule = new Intent(getApplicationContext(), docappointment.class);
                schedule.putExtra("username", username);
                schedule.putExtra("role", status);
                startActivity(schedule);
                break;
            case R.id.navi_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to Logout?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent logintent = new Intent(getApplicationContext(), Login.class);
                                startActivity(logintent);
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
