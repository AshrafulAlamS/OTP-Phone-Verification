package com.ashraful.otpphoneverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity implements OnOtpCompletionListener {
    private static final String TAG = "VerificationActivity";

    Button button;
    OtpView otpView;
    FirebaseAuth firebaseAuth;
    String verificationId;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        //FirebaseApp.initializeApp(this);

        phoneNumber = getIntent().getStringExtra("phoneNumber");

        otpView=findViewById(R.id.otp_view);
        button=findViewById(R.id.btn);
        firebaseAuth = FirebaseAuth.getInstance();

        sendVerification(phoneNumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpView.getText().toString().trim();
                if (code.isEmpty() || code.length() <4){
                    Toast.makeText(VerificationActivity.this, "Enter Code...", Toast.LENGTH_SHORT).show();
                    otpView.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);

    }

    private void signInWithCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                        }else {
                            Toast.makeText(VerificationActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerification(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(VerificationActivity.this, "Thank you. Your number already verified.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VerificationActivity.this, Home.class);
            startActivity(intent);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Log.e(TAG, "onVerificationFailed: ", e);
            Toast.makeText(VerificationActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onOtpCompleted(String otp) {

    }
}