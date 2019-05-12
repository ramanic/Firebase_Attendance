package com.ramanic.attendance;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class studentActivity extends AppCompatActivity {
    TextView text1;
    TextView text2;
    String std;
    DatabaseReference rootRef;
    DatabaseReference AttendRef;
    EditText dateSelect;
    final ArrayList atd_dates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        this.setTitle("Student");
        rootRef = FirebaseDatabase.getInstance().getReference();
        AttendRef = rootRef.child("Attendance");
        std = getIntent().getExtras().getString("UID");
        dateSelect =findViewById(R.id.date_picker);
        text1 = findViewById(R.id.textView5);
        text2 = findViewById(R.id.textView6);







    }
    public void viewAttend(View v) {
        if (dateSelect.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Date Required !1", Toast.LENGTH_LONG).show();

        } else {

            AttendRef.child(dateSelect.getText().toString()).child(std).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        text1.setText(text1.getText() + "\n" + postSnapshot.getKey());
                        text2.setText(text2.getText() + "\n" + postSnapshot.getValue());


                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                    Log.d("Ramanix", "ERROR!!!!", error.toException());
                }
            });

        }
    }


}
