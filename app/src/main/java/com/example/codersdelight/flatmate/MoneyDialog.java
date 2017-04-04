package com.example.codersdelight.flatmate;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MoneyDialog extends Activity {
    private EditText amount,note;
    private Button credit,debit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_dialog);
        amount= (EditText) findViewById(R.id.amount);
        note= (EditText) findViewById(R.id.note);

        credit= (Button) findViewById(R.id.credit);
        debit=(Button)findViewById(R.id.debit);
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendData();
            }
        });
        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });




}
public void sendData(){
    if(amount.getText().toString()!=null){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra(amount.getTag().toString(),amount.getText().toString());
        intent.putExtra(note.getTag().toString(),note.getText().toString());
        startActivity(intent);
        Log.i("done","success");

    }
    else{
        Toast.makeText(getApplicationContext(),"Enter all details",Toast.LENGTH_SHORT).show();
    }
}}
