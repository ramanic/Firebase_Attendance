package com.ramanic.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class teacherActivity extends AppCompatActivity {
    Spinner spinner1 ;
    Spinner spinner2 ;
    EditText date;
    Spinner std_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        this.setTitle("Teacher");
        date = findViewById(R.id.date_teachers);
        std_class =findViewById(R.id.class_teachers);
    }

    public  void manageAttendance(View v){
        spinner1 = findViewById(R.id.spinnerClassAttedance);
        spinner2= findViewById(R.id.spinnerPeriodAttedance);
        Intent intent = new Intent(getApplicationContext(), manageAttendance.class);
        intent.putExtra("PERIOD",spinner2.getSelectedItem().toString());
        intent.putExtra("CLASS",spinner1.getSelectedItem().toString());
        startActivity(intent);
    }
    public void view_attendance(View v) {
        if (date.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Date Required !1", Toast.LENGTH_LONG).show();

        } else {

            Intent intent = new Intent(getApplicationContext(), view_students_attendance.class);
            intent.putExtra("CLASS", std_class.getSelectedItem().toString());
            intent.putExtra("DATE", date.getText().toString());
            startActivity(intent);
        }
    }
}

