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
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class manageTeachers extends AppCompatActivity {
    DatabaseReference db;

    ListView mListView;
    Context c;
    FirebaseHelper helper;
    CustomAdapter adapter;

    EditText nameEditTxt, classEditText, passEditText,idEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_teachers);

        mListView = (ListView) findViewById(R.id.myListView2);

        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db, this, mListView);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
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
        ArrayList<Teachers> teachers2 = new ArrayList<>();
        ListView mListView;
        Context c;

        public FirebaseHelper(DatabaseReference db, Context context, ListView mListView) {
            this.db = db;
            this.c = context;
            this.mListView = mListView;
            this.retrieve();
        }



        public ArrayList<Teachers> retrieve() {
            db.child("Teachers").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    teachers2.clear();
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Teachers teachers = ds.getValue(Teachers.class);
                            teachers2.add(teachers);
                        }
                        adapter = new CustomAdapter(c, teachers2);
                        mListView.setAdapter(adapter);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.smoothScrollToPosition(teachers2.size());
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
            return teachers2;
        }
    }

    class CustomAdapter extends BaseAdapter {
        Context c;
        ArrayList<Teachers> teachers;
        public CustomAdapter(Context c, ArrayList<Teachers> teachers) {
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
                convertView = LayoutInflater.from(c).inflate(R.layout.model_teacher, parent, false);
            }
            TextView nameTextView = convertView.findViewById(R.id.nameTextView2);



            final Button dlteach = convertView.findViewById(R.id.dltTeacher);
            TextView subTextView = convertView.findViewById(R.id.teacherText);
            final Teachers s = (Teachers) this.getItem(position);
            nameTextView.setText("Name : "+s.getName());

            subTextView.setText("Subject : " + s.getSub() + "\n ID: " +s.getid());

            dlteach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.child("Teachers").child(s.getid()).removeValue();
                    Log.d("RAMANIC",""+getCount());

                    notifyDataSetChanged();
                }
            });



            return convertView;
        }
    }
    private void displayInputDialog() {

        Dialog d = new Dialog(this);
        d.setTitle("Create New Teacher");
        d.setContentView(R.layout.input_teacher);

        nameEditTxt = d.findViewById(R.id.nameEditText2);
        classEditText = d.findViewById(R.id.subEditText);
        idEditText = d.findViewById(R.id.idEditText2);
        passEditText = d.findViewById(R.id.passEditText2);
        Button saveBtn = d.findViewById(R.id.saveBtn2);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();


                String name = nameEditTxt.getText().toString();
                String std_pass = passEditText.getText().toString();
                String std_class = classEditText.getText().toString();

                db.child("Teachers").child(id).child("name").setValue(name);
                db.child("Teachers").child(id).child("pass").setValue(std_pass);
                db.child("Teachers").child(id).child("sub").setValue(std_class);
                db.child("Teachers").child(id).child("id").setValue(id);

            }
        });
        d.show();
    }
}
