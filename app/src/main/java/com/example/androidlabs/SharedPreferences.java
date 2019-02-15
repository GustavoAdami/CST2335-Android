package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class SharedPreferences extends AppCompatActivity {

    android.content.SharedPreferences sp;
    EditText typeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        typeField = (EditText)findViewById(R.id.email);
        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "Default value");

        typeField.setText(savedString);
    }
}
