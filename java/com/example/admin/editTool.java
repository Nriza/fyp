package com.example.admin;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class editTool extends AppCompatActivity {

    String name, quantity, names, status;
    EditText etname, etquantity;
    Button edit, delete;
    DatabaseReference ref;
    int minteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tool);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        quantity = bundle.getString("quantity");
        minteger = Integer.parseInt(quantity);

        etname = findViewById(R.id.add_name);
        etquantity = findViewById(R.id.integer_number);

        etname.setText(name);
        etquantity.setText(quantity);

        edit = findViewById(R.id.updatebtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editTool.this);
                alertDialogBuilder.setMessage("Are You Sure Want to Update This Tool?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                names = etname.getText().toString().trim();
                                quantity = etquantity.getText().toString().trim();

                                ref = FirebaseDatabase.getInstance().getReference("Stock");
                                ref.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                                String key=datas.getKey();
                                                ref.child(key).child("name").setValue(names);
                                                ref.child(key).child("quantity").setValue(quantity);
                                                Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), nurseMain.class);
                                                /*.putExtra("username", username);
                                                intent.putExtra("role", status);*/
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        delete = findViewById(R.id.deletebtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(editTool.this);
                alertDialogBuilder.setMessage("Are You Sure Want to Delete This Tool?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                ref = FirebaseDatabase.getInstance().getReference("Stock");
                                Query applesQuery = ref.child(name);

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                            Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), nurseMain.class);
                                            startActivity(intent);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

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

        etquantity.setText("" + number);
    }
}