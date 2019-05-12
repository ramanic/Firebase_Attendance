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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class manageStudents extends AppCompatActivity {
    DatabaseReference db;

    ListView mListView;
    Context c;
    FirebaseHelper helper;
    CustomAdapter adapter;

    EditText nameEditTxt, classEditText, passEditText,idEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        mListView = (ListView) findViewById(R.id.myListView);

        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db, this, mListView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.smoothScrollToPosition(4);
                displayInputDialog();
            }
        });
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
                            students2.add(students);
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
        ArrayList<Students> teachers;
        public CustomAdapter(Context c, ArrayList<Students> teachers) {
            this.c = c;
            this.teachers = teachers;
        }
        @Override
        public int getCount() {
            return teachers.size();
        }
        @Override
        public Object getItem(int position) {
            return teachers.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(c).inflate(R.layout.model_student, parent, false);
            }
            TextView nameTextView = convertView.findViewById(R.id.nameTextViewStd);
            Button dltbtn = convertView.findViewById(R.id.dltstudent);

            TextView classTextView = convertView.findViewById(R.id.textStd);

             final Students s = (Students) this.getItem(position);
            nameTextView.setText("Name: " + s.getName() );

            dltbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.child("Students").child(s.getid()).removeValue();
                    Log.d("RAMANIC",""+getCount());

                    notifyDataSetChanged();
                }
            });


            classTextView.setText("Class: " + s.getclass()+"\n ID :" + s.getid());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }
    private void displayInputDialog() {

        final Dialog d = new Dialog(this);
        d.setTitle("Create New Student");
        d.setContentView(R.layout.input_student);

        nameEditTxt = d.findViewById(R.id.nameEditTextStd);
        final Spinner classdrop = d.findViewById(R.id.classTextStd);
        idEditText = d.findViewById(R.id.idEditTextStd);
        passEditText = d.findViewById(R.id.passEditTextStd);

        Button saveBtn = d.findViewById(R.id.saveBtnStd);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();


                String name = nameEditTxt.getText().toString();
                String std_pass = passEditText.getText().toString();
                String std_class;
                std_class = classdrop.getSelectedItem().toString();

                db.child("Students").child(id).child("name").setValue(name);
                db.child("Students").child(id).child("pass").setValue(std_pass);
                db.child("Students").child(id).child("std_class").setValue(std_class);
                db.child("Students").child(id).child("id").setValue(id);


            }
        });
        d.show();
    }
}
