package com.example.androidlabs;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    Toolbar myToolbar;
    String message = "You clicked on the overflow menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        myToolbar = (Toolbar)findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        //Show the toast immediately:
        Toast.makeText(this, "Welcome to Menu Example", Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.toolbar_menu, menu);

	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                returnbtn false;
            }  });

	    */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this, "This is the initial message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                newMessage();
                break;
            case R.id.item3:
                // Snackbar to go back
                Snackbar snackbar = Snackbar.make(myToolbar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GO!", e -> finish());
                snackbar.show();
                break;
            case R.id.overflowMenu:
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void newMessage(){
        View middle = getLayoutInflater().inflate(R.layout.dialogboxnewmessage, null);
        //Button btn = (Button)middle.findViewById(R.id.item2);
        EditText newMessage = middle.findViewById(R.id.editMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
        builder.setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        message = newMessage.getText().toString();
                    }
                })
                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                })  .setView(middle);
        builder.create().show();
    }
}
