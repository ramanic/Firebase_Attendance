package com.ramanic.attendance;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class manageAttendance extends AppCompatActivity {
    DatabaseReference db;
    String studentClass;
    String period;

    ListView mListView;
    Context c;
    FirebaseHelper helper;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance);

        studentClass = getIntent().getExtras().getString("CLASS");
        period = getIntent().getExtras().getString("PERIOD");

        mListView = (ListView) findViewById(R.id.myListViewAttendance);

        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db, this, mListView);


    }

    public class FirebaseHelper {
        DatabaseReference db;
        Boolean saved;
        ArrayList<Students> students2 = new ArrayList<>();
        ListView mListView;
        Context c;

        public FirebaseHelper(DatabaseReference db, Context context, ListView mListView) {
            this.db = db;
            this.c = context;
            this.mListView = mListView;
            this.retrieve();

        }



        public ArrayList<Students> retrieve() {
            db.child("Students").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    students2.clear();
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Students students = ds.getValue(Students.class);
                            if(students.getclass().contains(studentClass)||studentClass.contains(students.getclass())) {
                                students2.add(students);
                            }

                        }
                        adapter = new CustomAdapter(c, students2);
                        mListView.setAdapter(adapter);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.smoothScrollToPosition(students2.size());
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("mTAG", databaseError.getMessage());
                    Toast.makeText(c, "ERROR " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return students2;
        }
    }

    class CustomAdapter extends BaseAdapter {
        Context c;
        ArrayList<Students> student;
        public CustomAdapter(Context c, ArrayList<Students> teachers) {
            this.c = c;
            this.student = teachers;
        }
        @Override
        public int getCount() {
            return student.size();
        }
        @Override
        public Object getItem(int position) {
            return student.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.model_student_attendance, parent, false);
            }
            RadioButton preset = convertView.findViewById(R.id.present);
            RadioButton absent = convertView.findViewById(R.id.absent);
            TextView nameTextView = convertView.findViewById(R.id.nameTextViewStdAtd);


            TextView classTextView = convertView.findViewById(R.id.textStdAtd);

            final Students s = (Students) this.getItem(position);
            nameTextView.setText("Name: " + s.getName() );


            classTextView.setText("Class: " + s.getclass()+"\n ID :" + s.getid());

            preset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.child("Attendance").child(date).child(s.getid()).child(period).setValue("P");

                }
            });
            absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.child("Attendance").child(date).child(s.getid()).child(period).setValue("A");
                }
            });
            return convertView;
        }
    }

}
