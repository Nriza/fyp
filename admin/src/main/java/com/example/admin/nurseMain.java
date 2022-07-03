package com.example.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class nurseMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Bundle bundle;
    String username, status;
    TextView day1, day2;
    DatabaseReference databaseReference;
    String getday1, getday2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schedule);

        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        status = bundle.getString("role");

        day1 = findViewById(R.id.day1);
        day2= findViewById(R.id.day2);

        databaseReference = FirebaseDatabase.getInstance().getReference("StaffSchedule");
        Query checkUser = databaseReference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    getday1 = snapshot.child(username).child("day1").getValue(String.class);
                    getday2 = snapshot.child(username).child("day2").getValue(String.class);
                    day1.setText(getday1);
                    day2.setText(getday2);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Choose the shift day first", Toast.LENGTH_LONG).show();
                    Intent choose = new Intent(getApplicationContext(), cscheduleFragment.class);
                    choose.putExtra("username", username);
                    choose.putExtra("role", status);
                    startActivity(choose);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String weekday = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());

        /*if (weekday.equals("Fri")){
            databaseReference = FirebaseDatabase.getInstance().getReference("StaffSchedule");
            Query query = databaseReference.child(username);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                        Toast.makeText(getApplicationContext(), "Please Choose the shift day first", Toast.LENGTH_LONG).show();
                        Intent choose = new Intent(getApplicationContext(), cscheduleFragment.class);
                        choose.putExtra("username", username);
                        choose.putExtra("role", status);
                        startActivity(choose);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }*/

        navigationview();
    }

    private void navigationview() {
        drawerLayout = findViewById(R.id.nurse_drawer_layout);
        navigationView = findViewById(R.id.nurse_navi_view);
        toolbar = (Toolbar) findViewById(R.id.nurse_toolbar);

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
            case R.id.nurse_navi_home:
                Intent home = new Intent(getApplicationContext(), nurseMain.class);
                home.putExtra("username", username);
                home.putExtra("role", status);
                startActivity(home);
                break;
            case R.id.nurse_navi_appointment:
                Intent schedule = new Intent(getApplicationContext(), appointment.class);
                schedule.putExtra("username", username);
                schedule.putExtra("role", status);
                startActivity(schedule);
                break;
            case R.id.nurse_stock:
                Intent stock = new Intent(getApplicationContext(), stockFragment.class);
                stock.putExtra("username", username);
                stock.putExtra("role", status);
                startActivity(stock);
                break;
            case R.id.nurse_navi_logout:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to Logout?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent logintent = new Intent(getApplicationContext(), MainActivity.class);
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