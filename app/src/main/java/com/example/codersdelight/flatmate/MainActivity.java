package com.example.codersdelight.flatmate;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;
    public static final String ANONYMOUS="anonymous";
    public static final int MAX_MSG_LEN_LIM=1000;
    public static final int RC_SIGN_IN=1;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar ProgressBar;
    private Button ib;
    private EditText MessageEditText;
    private Button SendButton;
    private String mUsername;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername=ANONYMOUS;
        ProgressBar= (android.widget.ProgressBar) findViewById(R.id.progressBar);
        MessageEditText= (EditText) findViewById(R.id.messageEditText);
        SendButton= (Button) findViewById(R.id.sendButton);
        mMessageListView= (ListView) findViewById(R.id.messageListView);
        ib= (Button) findViewById(R.id.imagebutton);
        firebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabaseReference=firebaseDatabase.getReference().child("message");
        //initialising the message list view
        final List<chatMessages> chatMessages=new ArrayList<>();
        mMessageAdapter=new MessageAdapter(this,R.layout.item_message,chatMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        ProgressBar.setVisibility(ProgressBar.INVISIBLE);

        //getting the data from dialog box






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

MessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_MSG_LEN_LIM)});
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMessages chatMessages1=new chatMessages(MessageEditText.getText().toString(),mUsername);
                firebaseDatabaseReference.push().setValue(chatMessages1);
                MessageEditText.setText("");

            }
        });
       mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out

                signUp();
                }
            }
        };
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MoneyDialog.class);
                startActivity(intent);
            }


        });
    recieveData();
    }
    void recieveData(){
     if(mFirebaseAuth.getCurrentUser()!=null){
      FirebaseUser user=mFirebaseAuth.getCurrentUser();
        mUsername=user.getDisplayName();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            String getAmount = (String) bundle.get("amount");
            String getNote = (String) bundle.get("note");
            chatMessages chatMessages = new chatMessages("Paid Rs." + getAmount + " for " + getNote, mUsername);
            firebaseDatabaseReference.push().setValue(chatMessages);
        }
        else{
            signUp();
        }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();

                finish();
            }

        }
    }
    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        mMessageAdapter.clear();

        detachDatabaseReadListener();
    }
    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    chatMessages chatMessage = dataSnapshot.getValue(chatMessages.class);
                        mMessageAdapter.add(chatMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            firebaseDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            firebaseDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
public void signUp(){
    onSignedOutCleanup();
    startActivityForResult(
            AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),

                            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                    .build(),
            RC_SIGN_IN);
}

}
