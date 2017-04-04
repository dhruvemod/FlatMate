package com.example.codersdelight.flatmate;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by codersdelight on 4/4/17.
 */

public class MessageAdapter extends ArrayAdapter<chatMessages>{
    public MessageAdapter(Context context, int resource, List<chatMessages> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        chatMessages message = getItem(position);

        messageTextView.setText(message.getMessage());
        authorTextView.setText(message.getName());

        return convertView;
    }
}
