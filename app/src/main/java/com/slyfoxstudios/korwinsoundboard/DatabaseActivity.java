package com.slyfoxstudios.korwinsoundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    Button btnBack;
    Button btnAddToDb;

    EditText txtDescription;
    EditText txtDuration;
    EditText txtPart;
    ListView lstView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

         myDb = new DatabaseHelper(this);   //deploying a class that supports a database

        //BACK TO MainActivity
        btnBack = (Button) findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //ADD to DB
        btnAddToDb = (Button) findViewById(R.id.btn_AddToDB);
        btnAddToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddData();
            }
        });

        //TEXTS
        txtDescription = (EditText)findViewById(R.id.txt_soundDescription);
        txtDuration= (EditText)findViewById(R.id.txt_duration) ;
        txtPart = (EditText) findViewById(R.id.txt_part);
        ShowSounds();
    }



    public  void AddData()
    {
        boolean isInserted = myDb.insertData(txtDescription.getText().toString(),
                lstView.getItemAtPosition(lstView.getCheckedItemPosition()).toString(),
                Integer.parseInt(txtDuration.getText().toString()),
                Integer.parseInt(txtPart.getText().toString()));

        if(isInserted)
            Toast.makeText(DatabaseActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(DatabaseActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
    }


    public void ShowSounds()
    {

        List<String> rawNames = new ArrayList<>();
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            rawNames.add(fields[count].getName());
        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, rawNames);

        lstView = (ListView) findViewById(R.id.lst_soundsList);
        lstView.setAdapter(adapter);

    }
}
