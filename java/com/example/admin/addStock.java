package com.example.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class addStock extends AppCompatActivity {

    int minteger = 1;
    EditText displayInteger, tool_name;
    DatabaseReference databaseReference;
    stockList stockList;
    Button addbtn;
    String name = "", quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        displayInteger = findViewById(R.id.integer_number);
        tool_name = findViewById(R.id.add_name);

        addbtn = findViewById(R.id.add);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = tool_name.getText().toString().trim();
                quantity = displayInteger.getText().toString().trim();

                if (!name.equals("") && !quantity.equals("")){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Stock");
                    Query checkName = databaseReference.orderByChild("name").equalTo(name);
                    stockList = new stockList();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(name)){
                                Toast.makeText(getApplicationContext(), "The Tool name Already Exist", Toast.LENGTH_LONG).show();
                            }
                            else {
                                stockList.setName(name);
                                stockList.setQuantity(quantity);
                                databaseReference.child(name).setValue(stockList);

                                Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_LONG).show();

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(addStock.this);
                                alertDialogBuilder.setMessage("Do You Want to Add More Tool?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                finish();
                                                startActivity(getIntent());

                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), nurseMain.class);
                                        startActivity(intent);
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Fill in All the Blank", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void increaseInteger(View view) {

        if (minteger < 0){
            minteger = 0;
        }

        minteger = minteger + 1;
        display(minteger);

    }public void decreaseInteger(View view) {

        if (minteger < 0){
            minteger = 0;
        }

        minteger = minteger - 1;
        display(minteger);
    }

    private void display(int number) {

        if (number < 0){
            number = 0;
        }

        displayInteger.setText("" + number);
    }

}