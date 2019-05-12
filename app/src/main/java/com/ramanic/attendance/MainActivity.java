package com.ramanic.attendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String TAG = "RAMANIC";
    Button login;
    EditText username;
    EditText password;
    String uid;
    String upass;
    DatabaseReference rootRef;
    DatabaseReference AdminRef;
    DatabaseReference teacherRef;
    DatabaseReference studentRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button);
        username =findViewById(R.id.editText);
        password =findViewById(R.id.editText2);
        rootRef = FirebaseDatabase.getInstance().getReference();
        AdminRef = rootRef.child("Admins");
        studentRef =rootRef.child("Students");
        teacherRef =rootRef.child ("Teachers");

        creat_admin();





    }
    public void onLogin(View v){
        uid = username.getText().toString();
        upass= password.getText().toString();
        try {
            Log.d(TAG,"Inside try !!");

            AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(uid)) {
                        if(snapshot.child(uid).getValue().equals(upass)) {

                            Intent intent = new Intent(getApplicationContext(), adminActivity.class);
                            intent.putExtra("UID", uid);
                            startActivity(intent);
                        }else{
                            Log.d(TAG, snapshot.child(uid).getValue(String.class));
                            Toast.makeText(getApplicationContext(),"Incorrect Password !!",Toast.LENGTH_LONG).show();

                        }



                    }else{
                        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.hasChild(uid)) {
                                    if(snapshot.child(uid).child("pass").getValue().toString().contains(upass)) {


                                        Log.d(TAG, "User  Found" +snapshot.child(uid).child("pass").getValue());
                                        Intent intent = new Intent(getApplicationContext(), teacherActivity.class);
                                        intent.putExtra("UID", uid);
                                        startActivity(intent);
                                    }else{

                                        Log.d(TAG,""+snapshot.child(uid).child("pass").getValue());
                                        Toast.makeText(getApplicationContext(),"Incorrect Password !!",Toast.LENGTH_LONG).show();

                                    }





                                }else{
                                    studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.hasChild(uid)) {
                                                if(snapshot.child(uid).getValue().toString().contains(upass)) {
                                                    Intent intent = new Intent(getApplicationContext(), studentActivity.class);
                                                    intent.putExtra("UID", uid);
                                                    startActivity(intent);



                                                }else{
                                                    Toast.makeText(getApplicationContext(),"Incorrect Password !!",Toast.LENGTH_LONG).show();

                                                }



                                            }else{
                                                Toast.makeText(getApplicationContext(),"Incorrect Username.",Toast.LENGTH_LONG).show();

                                                Log.d(TAG,"No USername Found");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch( Exception e){
            Log.d(TAG,"Databse Error");
            Toast.makeText(this.getApplicationContext(),"Can't Connect To Database",Toast.LENGTH_LONG).show();
            //this.finish();
            //System.exit(0);
        }




    }
    public void creat_admin(){
        try {

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild("Admins")) {

                    } else {
                        AdminRef.child("admin").setValue("1234");



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch( Exception e){
            Toast.makeText(this.getApplicationContext(),"Can't Connect To Database",Toast.LENGTH_LONG).show();
            this.finish();
            System.exit(0);
        }
    }
}
