package com.example.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class cscheduleFragment extends AppCompatActivity{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String username, status;
    CheckBox sun, mon, tue, wed, thu;
    Bundle bundle;
    Button uploadbtn;
    DatabaseReference reference;
    StaffSchedule staff;
    ArrayList<String> day;
    int sum = 0, color = Color.parseColor("#EBEBE4");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cschedule);

        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        status = bundle.getString("role");

        sun = findViewById(R.id.sun);
        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        uploadbtn = findViewById(R.id.upload);

        day = new ArrayList<>();

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sun.isChecked()){
                    day.add(sun.getText().toString());
                    sum = sum + 1 ;
                }

                if(mon.isChecked()){
                    day.add(mon.getText().toString());
                    sum = sum + 1 ;
                }

                if(tue.isChecked()){
                    day.add(tue.getText().toString());
                    sum = sum + 1 ;
                }

                if(wed.isChecked()){
                    day.add(wed.getText().toString());
                    sum = sum + 1 ;
                }

                if(thu.isChecked()){
                    day.add(thu.getText().toString());
                    sum = sum + 1 ;
                }

                if (sum != 2){
                    Toast.makeText(getApplicationContext(), "Please Choose 2 days", Toast.LENGTH_LONG).show();
                    sum = 0;
                }
                else{
                    staff = new StaffSchedule();

                    reference = FirebaseDatabase.getInstance().getReference("StaffSchedule");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            staff.setUsername(username.trim());
                            staff.setStatus(status);
                            staff.setDay1(day.get(0).trim());
                            staff.setDay2(day.get(1).trim());

                            reference.child(username).setValue(staff);

                            finish();
                            Intent choose = new Intent(getApplicationContext(), nurseMain.class);
                            choose.putExtra("username", username);
                            choose.putExtra("role", status);
                            startActivity(choose);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

        });


    }

}