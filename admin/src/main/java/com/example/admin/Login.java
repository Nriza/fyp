package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private TextView signupbtn, loginbtn, otp;
    private Button login, signup;
    private EditText emailText, passwordText, nameText, confirmText;
    private String email, password, username, confirm;
    DatabaseReference reference, reference2, reference3;
    Doctor doctor;
    Nurse nurse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupbtn = findViewById(R.id.register_btn);
        login = findViewById(R.id.login);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_signup);
                register();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogin();
            }
        });
    }

    private void login() {
        setContentView(R.layout.activity_login);

        signupbtn = findViewById(R.id.register_btn);
        login = findViewById(R.id.login);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_signup);
                register();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogin();
            }
        });
    }

    private void register() {

        loginbtn = findViewById(R.id.login_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
                login();
            }
        });

        emailText = findViewById(R.id.email_signup);
        passwordText = findViewById(R.id.password_signup);
        confirmText = findViewById(R.id.confirm_password);
        nameText = findViewById(R.id.name_signup);
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = String.valueOf(emailText.getText());
                String mSubject = "hi";
                int min = 100000;
                int max = 999999;
                //Generate random int value from 200 to 400
                int code = (int)(Math.random()*(max-min+1)+min);

                String mMessage = String.valueOf(code);

                email = String.valueOf(emailText.getText()).trim();
                password = String.valueOf(passwordText.getText()).trim();
                confirm = String.valueOf(confirmText.getText()).trim();
                username = String.valueOf(nameText.getText()).trim();

                if(!username.equals("") && !email.equals("") && !password.equals("") && !confirm.equals("")){

                    String regex = "^(.+)@(.+)$";

                    if(email.matches(regex)){
                        if (confirm.equals(password)){

                            reference = FirebaseDatabase.getInstance().getReference("Login");

                            Query checkUser = reference.orderByChild("username").equalTo(username);

                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        nameText.setError("The Username Already Exist");
                                        nameText.requestFocus();
                                    }
                                    else{
                                        nameText.setError(null);
                                        reference = FirebaseDatabase.getInstance().getReference().child("Login");
                                        Query query = reference.orderByChild("email").equalTo(email);
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    emailText.setError("The Email Already Registered");
                                                    emailText.requestFocus();
                                                }
                                                else {
                                                    JavaMailAPI javaMailAPI = new JavaMailAPI(email, mSubject, mMessage);

                                                    javaMailAPI.execute();

                                                    setContentView(R.layout.verification_code);
                                                    verification(email, code, password, username);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "The Confirm Password is not match with the Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "The Email is not valid", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "All Field Required", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void verification(String email, int code, String password, String username) {

        EditText codenum1;
        Button verify;

        codenum1 = findViewById(R.id.code_box);
        verify = findViewById(R.id.verify);
        otp = findViewById(R.id.otp);

        otp.setText("Enter Code send to " + email);


        String number = String.valueOf(code);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code1 = codenum1.getText().toString().trim();

                if(code1.equals(number.trim())){

                    reference = FirebaseDatabase.getInstance().getReference("Login");
                    reference2 = FirebaseDatabase.getInstance().getReference("Doctor");
                    reference3 = FirebaseDatabase.getInstance().getReference("Nurse");


                    doctor = new Doctor();
                    nurse = new Nurse();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Bundle bundle=getIntent().getExtras();
                            String status = bundle.getString("position");

                            if (status.equals("doctor")){
                                doctor.setEmail(email.trim());
                                doctor.setPassword(password.trim());
                                doctor.setUsername(username.trim());
                                doctor.setStatus(status);

                                reference.child(username).setValue(doctor);
                                reference2.child(username).setValue(doctor);
                            }
                            else if (status.equals("nurse")){
                                nurse.setEmail(email.trim());
                                nurse.setPassword(password.trim());
                                nurse.setUsername(username.trim());
                                nurse.setStatus(status);

                                reference.child(username).setValue(nurse);
                                reference3.child(username).setValue(nurse);
                            }

                            Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                            login();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    codenum1.setText("");
                    Toast.makeText(getApplicationContext(), "Wrong code", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void confirmLogin() {

        nameText = findViewById(R.id.username_login);
        passwordText = findViewById(R.id.password_login);

        username = String.valueOf(nameText.getText()).trim();
        password = String.valueOf(passwordText.getText()).trim();

        if(!username.equals("") && !password.equals("")){
            reference = FirebaseDatabase.getInstance().getReference("Login");

            Query checkUser = reference.orderByChild("username").equalTo(username);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        nameText.setError(null);
                        String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                        String statusFromDB = snapshot.child(username).child("status").getValue(String.class);

                        Bundle bundle=getIntent().getExtras();
                        String status = bundle.getString("role");

                        if (statusFromDB.equals(status)){
                            nameText.setError(null);
                            if (passwordFromDB.equals(password)){
                                passwordText.setError(null);
                                if (status.equals("doctor")){
                                    Intent intent = new Intent(getApplicationContext(), docMain.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("role", status);
                                    startActivity(intent);
                                }
                                else if (status.equals("nurse")){
                                    Intent intent = new Intent(getApplicationContext(), nurseMain.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("role", status);
                                    startActivity(intent);
                                }
                            }
                            else {
                                passwordText.setError("Wrong Password");
                                passwordText.requestFocus();
                            }
                        }
                        else {
                            nameText.setError("Wrong Username");
                            nameText.requestFocus();
                        }
                    }
                    else{
                        nameText.setError("The username not Exist");
                        nameText.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "All Field Required", Toast.LENGTH_LONG).show();
        }

    }

}