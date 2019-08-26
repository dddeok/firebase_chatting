package com.example.firebase_chat2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class ChatAdapter extends ArrayAdapter<ChatData> {
    private  final SimpleDateFormat simpleDateFormat= new SimpleDateFormat("a h:mm",Locale.getDefault());

    public ChatAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_chat, null);

            viewHolder = new ViewHolder();
            viewHolder.mTextUserName = (TextView) convertView.findViewById(R.id.txt_userName);
            viewHolder.mTextMessage = (TextView) convertView.findViewById(R.id.txt_message);
            viewHolder.mTextTime = (TextView) convertView.findViewById(R.id.txt_time);

            convertView.setTag(viewHolder);

        } else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ChatData chatData = getItem(position);
        viewHolder.mTextUserName.setText(chatData.userName);
        viewHolder.mTextMessage.setText(chatData.message);
        viewHolder.mTextTime.setText(simpleDateFormat.format(chatData.time));

        return convertView;
    }

    private class ViewHolder {
        private TextView mTextUserName;
        private TextView mTextMessage;
        private TextView mTextTime;
    }
}
