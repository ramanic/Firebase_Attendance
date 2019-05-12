package com.ramanic.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class adminActivity extends AppCompatActivity {
    String uid;
    TextView welcome;
    EditText date;
    Spinner std_class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        welcome = findViewById(R.id.welcomeadmin);
        this.setTitle("Admin");
       uid = getIntent().getExtras().getString("UID");
        welcome.setText("Welcome " + uid + " !!!");
        date = findViewById(R.id.date_admin);
        std_class = findViewById(R.id.class_admin);


    }

    public void view_student(View v) {
        Intent intent = new Intent(getApplicationContext(), manageStudents.class);

        startActivity(intent);
    }

    public void view_teachers(View v) {
        Intent intent = new Intent(getApplicationContext(), manageTeachers.class);

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

