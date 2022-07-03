package com.example.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class stockFragment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String username, status;
    Bundle bundle;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    StockAdapter adapter;
    ArrayList<stockList> list, filterlist;
    ImageView addbtn;
    EditText search;
    AlertDialog dialog;
    stockList stockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stock);

        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        status = bundle.getString("role");

        recyclerView = findViewById(R.id.toolList);
        databaseReference = FirebaseDatabase.getInstance().getReference("Stock");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        adapter = new StockAdapter(this, list, username, status);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    stockList stock = dataSnapshot.getValue(stockList.class);
                    list.add(stock);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.update_popup, null);
        TextView title = view.findViewById(R.id.title);
        EditText add_name = view.findViewById(R.id.add_name);
        EditText add_quan = view.findViewById(R.id.add_quan);
        Button btn = view.findViewById(R.id.updatebtn);

        title.setText("ADD TOOL");
        btn.setText("Add");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = add_name.getText().toString().trim();
                String quantity = add_quan.getText().toString().trim();

                if (!name.equals("") && !quantity.equals("")){
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",name);
                    map.put("quantity",quantity);
                    FirebaseDatabase.getInstance().getReference("Stock").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), stockFragment.class);
                            intent.putExtra("username", username);
                            intent.putExtra("role", status);
                            startActivity(intent);

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please Fill in All the Blank", Toast.LENGTH_LONG).show();
                }


                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();


        addbtn = findViewById(R.id.add);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.getWindow().setLayout(1000, 950);
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
        for (stockList item : list){
            if (item.getName().toLowerCase().contains(toString.toLowerCase())){
                filterlist.add(item);
            }
        }
        adapter.filterlist(filterlist);
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