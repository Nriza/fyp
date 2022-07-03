package com.example.clinicsystem;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Appointment extends AppCompatActivity {

    CalendarView DatePicker;
    TextView time1, time2, time3;
    private String day;
    Button app_btn;
    app app;
    String time;
    DatabaseReference ref;
    ArrayList<String> time_slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

       DatePicker = findViewById(R.id.app_date);
        time1 = findViewById(R.id.app_time1);
        time2 = findViewById(R.id.app_time2);
        time3 = findViewById(R.id.app_time3);
        app_btn = findViewById(R.id.app_btn);

        DatePicker.setMinDate(System.currentTimeMillis() - 1000);

        DatePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;

                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    Date dates = inFormat.parse(date);
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    day = outFormat.format(dates);

                    if (day.trim().equals("Sunday") || day.trim().equals("Monday") || day.trim().equals("Tuesday") || day.trim().equals("Wednesday") || day.trim().equals("Thursday")){

                        time_slot = new ArrayList<>();
                        ref = FirebaseDatabase.getInstance().getReference("Appointment");
                        ref.orderByChild("date").equalTo(date).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    String timedb = dataSnapshot.child("time").getValue().toString().trim();
                                    if (timedb.equals(time1.getText().toString().trim())){
                                        time1.setVisibility(view.INVISIBLE);

                                    }
                                    if (timedb.equals(time2.getText().toString().trim())){
                                        time2.setVisibility(view.INVISIBLE);
                                    }
                                    if (timedb.equals(time3.getText().toString().trim())){
                                        time3.setVisibility(view.INVISIBLE);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        time1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        time2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        time3.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        time1.setVisibility(view.getVisibility());
                        time2.setVisibility(view.getVisibility());
                        time3.setVisibility(view.getVisibility());

                        time1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                time = time1.getText().toString().trim();
                                time1.setBackgroundColor(Color.parseColor("#f7838d"));
                                time2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                time3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                        });

                        time2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                time = time2.getText().toString().trim();
                                time2.setBackgroundColor(Color.parseColor("#f7838d"));
                                time1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                time3.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                        });
                        time3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                time = time3.getText().toString().trim();
                                time3.setBackgroundColor(Color.parseColor("#f7838d"));
                                time2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                time1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            }
                        });

                        app_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");

                                app = new app();

                                reference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        Bundle bundle = getIntent().getExtras();
                                        String username = bundle.getString("username");

                                        app.setUsername(username.trim());
                                        app.setDate(date.trim());
                                        app.setTime(time.trim());

                                        reference.child(username).setValue(app);
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), Profile.class);
                                        intent.putExtra("username", username);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        });

                    }

                    else {
                        time1.setVisibility(view.INVISIBLE);
                        time2.setVisibility(view.INVISIBLE);
                        time3.setVisibility(view.INVISIBLE);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}