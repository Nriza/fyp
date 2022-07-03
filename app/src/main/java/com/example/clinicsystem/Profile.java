package com.example.clinicsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Profile extends AppCompatActivity {

    //public static final int CAMERA_PERM_CODE = 101;
    TextView name;
    ImageView pic;
    TextView app_btn, app_datetime;
    DatabaseReference reference, databaseReference;
    treatAdapter adapter;
    ArrayList<treatList> list;
    String emailFromDB;
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        pic = findViewById(R.id.image);
        app_btn = findViewById(R.id.app_btn);
        app_datetime = findViewById(R.id.app_datetime);

        Bundle bundle=getIntent().getExtras();
        String username = bundle.getString("username");

        name.setText(username);

        recyclerView = findViewById(R.id.history);
        databaseReference = FirebaseDatabase.getInstance().getReference("Treatment").child(username);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        list = new ArrayList<>();
        adapter = new treatAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(adapter);

        //List of treatment History
        databaseReference.orderByChild("status").equalTo("complete").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    treatList app = dataSnapshot.getValue(treatList.class);
                    list.add(app);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        /*pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

        reference = FirebaseDatabase.getInstance().getReference("Treatment");

        //Check if user need to make an appointment
        Query checkOn = reference.child(username).orderByChild("status").equalTo("ongoing");
        checkOn.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Appointment");
                    Query check = refer.child(username);
                    check.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                app_btn.setText("Cancel The Appointment");
                                //To cancel the Appointment
                                app_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Profile.this);
                                        alertDialogBuilder.setMessage("Are you sure, You wanted to Cancel this Appointment?");
                                        alertDialogBuilder.setPositiveButton("yes",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        Query applesQuery = ref.child("Appointment").orderByChild("username").equalTo(username);

                                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                                    appleSnapshot.getRef().removeValue();
                                                                }
                                                                finish();
                                                                startActivity(getIntent());
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
                                            }
                                        });

                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
                                });
                            }
                            else {
                                app_btn.setText("Make an Appointment");
                                app_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), Appointment.class);
                                        intent.putExtra("username",username);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointment");

        Query checkUser = reference.orderByChild("username").equalTo(username);

        //Display User Appointment
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Date c = Calendar.getInstance().getTime();

                    String dateFromDB = snapshot.child(username).child("date").getValue(String.class);
                    String timeFromDB = snapshot.child(username).child("time").getValue(String.class);
                    app_datetime.setText("Appointment Date: " + dateFromDB + " " + timeFromDB);


                    try {
                        Date date1 =new SimpleDateFormat("dd-MM-yyyy").parse(dateFromDB);
                        int result = date1.compareTo(c);

                        if (result == 1 || result == -1){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Patient");

                            Query check = ref.orderByChild("username").equalTo(username);
                            check.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    emailFromDB = snapshot.child(username).child("email").getValue(String.class);
                                    String mSubject = "Temangan Dental Clinic Appointment";
                                    String mMessage = "Don't forget! You have an appointment on " + dateFromDB + " at " + timeFromDB;
                                    JavaMailAPI javaMailAPI = new JavaMailAPI(emailFromDB, mSubject, mMessage);

                                    javaMailAPI.execute();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    /*private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    askCamera();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void askCamera() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else{
            openCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Toast.makeText(this, "Camera Open Request", Toast.LENGTH_SHORT).show();
    }*/

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    pic.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gal", picturePath+"");
                pic.setImageBitmap(thumbnail);
            }
        }
    }*/
}
