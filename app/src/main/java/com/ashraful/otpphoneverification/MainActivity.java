package com.ashraful.otpphoneverification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.etPhone);
        button=findViewById(R.id.btnSend);
    }

    public void sendPhone(View view) {

        String number= editText.getText().toString().trim();
        if (number.isEmpty() || number.length() < 10 || number.length() >14){
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_LONG).show();
            editText.requestFocus();
            return;
        }

        String phoneNumber = "+88" + number;

        Intent intent=new Intent(MainActivity.this, VerificationActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
}