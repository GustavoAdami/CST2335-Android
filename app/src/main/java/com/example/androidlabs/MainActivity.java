package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_linear);

        sp = getSharedPreferences("EmailsFile", Context.MODE_PRIVATE);
        email = (EditText)findViewById(R.id.email);
        String savedEmail = sp.getString("UserEmail", "");

        email.setText(savedEmail);

        Button login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener( b -> {
            //Give directions to go from this page, to SecondActivity
            Intent nextPage = new Intent(MainActivity.this, ProfileActivity.class);
            email = (EditText)findViewById(R.id.email);
            nextPage.putExtra("email", email.getText().toString());

            //Now make the transition:
            startActivityForResult(nextPage, 345);
        });
    }

    /*
    In the onPause() function, use SharedPreferences to save the user’s email address.
    Then, in the onCreate function, load the SharedPreferences and load the user’s email address under the reservation name you used.
    If nothing is reserved, use the empty string “” as the default value.
    That way, the EditText hint will show. Debug your program to make sure that you are saving the user’s input correctly and reloading it next time.
     */

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String spEmail = email.getText().toString();
        editor.putString("UserEmail", spEmail);

        //write it to disk:
        editor.commit();
    }
}
