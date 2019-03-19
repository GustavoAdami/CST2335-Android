package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    String results = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView msgList = findViewById(R.id.msgList);
        EditText message = findViewById(R.id.txtMsg);
        Button send = findViewById(R.id.btnSend);
        Button receive = findViewById(R.id.btnReceive);
        List<Message> messageList = new ArrayList<>();

        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_SENT, MyDatabaseOpenHelper.COL_RECEIVED};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int sentColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_SENT);
        int receivedColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_RECEIVED);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, returnbtn true if there is a next item:
        while(results.moveToNext())
        {
            String msg = results.getString(sentColumnIndex);
            boolean received = results.getInt(receivedColIndex) != 0;
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            messageList.add(new Message(msg, received, id));
        }

        ListAdapter aListAdapter = new ListAdapter(messageList, getApplicationContext());
        msgList.setAdapter(aListAdapter);

        send.setOnClickListener(c -> {
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, message.getText().toString());
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, true);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            Message msg = new Message(message.getText().toString(), true, newId);
            messageList.add(msg);
            message.setText("");
            aListAdapter.notifyDataSetChanged();
        });

        receive.setOnClickListener(c -> {
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_SENT, message.getText().toString());
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_RECEIVED, false);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            Message msg = new Message(message.getText().toString(), false, newId);
            messageList.add(msg);
            message.setText("");
            aListAdapter.notifyDataSetChanged();
        });

        printCursor(results);
    }

    private class ListAdapter extends BaseAdapter {
        private List<Message> msg;
        private Context ctx;
        private LayoutInflater inflater;

        public ListAdapter(List<Message> msg, Context ctx){
            this.msg = msg;
            this.ctx = ctx;
            this.inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return msg.size();
        }

        @Override
        public Message getItem(int position) {
            return msg.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            if (msg.get(position).isSend()){
                newView = inflater.inflate(R.layout.send_msg, null);
            } else {
                newView = inflater.inflate(R.layout.receive_msg, null);
            }

            TextView messageText = newView.findViewById(R.id.textViewMessage);
            messageText.setText(msg.get(position).getMessage());
            return newView;
        }
    }

    public void printCursor( Cursor c){
        Log.d("Printing cursor", "DB version: " + MyDatabaseOpenHelper.VERSION_NUM + " Number of columns: " + c.getColumnCount());

        for(int i = 0; i < c.getColumnCount(); i++){
            Log.d("Name of columns: ", c.getColumnName(i));
        }

        Log.d("Number of results: ", + c.getCount() + "");

        c.moveToFirst();
        do{
            Log.d("Results: ", c.getString(c.getColumnIndex(MyDatabaseOpenHelper.COL_ID))
                            + " " + c.getString(c.getColumnIndex(MyDatabaseOpenHelper.COL_SENT))
                            + c.getInt(c.getColumnIndex(MyDatabaseOpenHelper.COL_RECEIVED)));
        } while (c.moveToNext());
    }
}


