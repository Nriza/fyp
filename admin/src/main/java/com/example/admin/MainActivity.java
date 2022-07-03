package com.example.admin;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RelativeLayout layout_nurse, layout_doctor;
    String role = "";
    Button selectbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_doctor = findViewById(R.id.doctor_layout);
        layout_nurse = findViewById(R.id.nurse_layout);
        selectbtn = findViewById(R.id.selectbtn);

        Drawable border = getResources().getDrawable( R.drawable.relative_border );

        layout_nurse.setBackground(null);
        layout_doctor.setBackground(null);

        layout_nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_nurse.setBackground(border);
                layout_doctor.setBackground(null);
                role = "nurse";
            }
        });

        layout_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_doctor.setBackground(border);
                layout_nurse.setBackground(null);
                role = "doctor";
            }
        });

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!role.equals("")){
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("role", role);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Select Your Role", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}