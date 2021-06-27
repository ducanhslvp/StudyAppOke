package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ducanh.appchat.LoginActivity;
import com.ducanh.appchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassWordActivity extends AppCompatActivity {
    Button btnReset;
    EditText txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_word);

        btnReset=findViewById(R.id.btn_reset);
        txtEmail=findViewById(R.id.edit_email);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString();
                if (email.equals("")){

                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPassWordActivity.this,"Xem Email de doi mat khau",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPassWordActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }
}