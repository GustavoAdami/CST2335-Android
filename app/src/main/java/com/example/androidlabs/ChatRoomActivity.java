package com.example.androidlabs;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomactivity);

        ListView msgList = findViewById(R.id.msgList);
        EditText message = findViewById(R.id.txtMsg);
        Button send = findViewById(R.id.btnSend);
        Button receive = findViewById(R.id.btnReceive);
        List<Message> messageList = new ArrayList<>();

                //String[] s = {"1", "2", "3"};
        ListAdapter aListAdapter = new ListAdapter(messageList, getApplicationContext());
        msgList.setAdapter(aListAdapter);

        send.setOnClickListener(c -> {
            Message msg = new Message(message.getText().toString(), true);
            messageList.add(msg);
            message.setText("");
            aListAdapter.notifyDataSetChanged();
        });

        receive.setOnClickListener(c -> {
            Message msg = new Message(message.getText().toString(), false);
            messageList.add(msg);
            message.setText("");
            aListAdapter.notifyDataSetChanged();
        });
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
}


