package com.example.codersdelight.flatmate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;
    public static final int MAX_MSG_LEN_LIM=1000;
    public static final int RC_SIGN_IN=1;
    private ListView mMessageListView;
    private ArrayAdapter arrayAdapter;
    private ProgressBar ProgressBar;
    private EditText MessageEditText;
    private Button SendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar= (android.widget.ProgressBar) findViewById(R.id.progressBar);
        MessageEditText= (EditText) findViewById(R.id.messageEditText);
        SendButton= (Button) findViewById(R.id.sendButton);
        mMessageListView= (ListView) findViewById(R.id.messageListView);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseDatabaseReference=firebaseDatabase.getReference().child("message");
        //initialising the message list view
        List<chatMessages> chatMessages=new ArrayList<>();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatMessages);
        mMessageListView.setAdapter(arrayAdapter);
        ProgressBar.setVisibility(ProgressBar.INVISIBLE);
        MessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().trim().length()>0)
                SendButton.setEnabled(true);
                else
                    SendButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
