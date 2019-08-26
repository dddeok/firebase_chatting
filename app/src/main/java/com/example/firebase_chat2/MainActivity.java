package com.example.firebase_chat2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;
    private EditText mEditmsg;
    private FirebaseDatabase mFirebaseDB;
    private DatabaseReference mDataReference;
    private ChildEventListener mChildEventListener;
    private ChatAdapter mAdapter;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFirebaseDB();
        initValue();
        
        
    }

    private void initValue() {
        userName = "Guest" + new Random().nextInt(5000);
    }

    private void initFirebaseDB() {
        mFirebaseDB = FirebaseDatabase.getInstance() ;
        mDataReference = mFirebaseDB.getReference("message");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                chatData.firebaseKey = dataSnapshot.getKey();
                mAdapter.add(chatData);
                mListView.smoothScrollToPosition(mAdapter.getCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String firebaseKey = dataSnapshot.getKey();
                int count = mAdapter.getCount();
                for (int i=0; i<count; i++){
                    if(mAdapter.getItem(i).firebaseKey.equals(firebaseKey)){
                        mAdapter.remove(mAdapter.getItem(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDataReference.addChildEventListener(mChildEventListener);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_msg);
        mAdapter = new ChatAdapter(this, R.layout.listitem_chat);
        mListView.setAdapter(mAdapter);
        mEditmsg = (EditText)findViewById(R.id.edit_msg);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String message = mEditmsg.getText().toString();
        if(!TextUtils.isEmpty(message)){
            mEditmsg.setText("");
            ChatData chatData = new ChatData();
            chatData.userName = userName;
            chatData.message = message;
            chatData.time = System.currentTimeMillis();
            mDataReference.push().setValue(chatData);
        }
    }
}
