package com.example.fourthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView txtregister;
    ImageView img_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtregister= findViewById(R.id.txtregister);
        img_login=findViewById(R.id.img_login);

        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Login.this, MainActivity.class);
                startActivity(in);
            }
        });

        //click here to login---------

        EditText txtemail=findViewById(R.id.loginemail);
        EditText txtpassword=findViewById(R.id.loginpassword);
        String email=txtemail.getText().toString();
       String password=txtpassword.getText().toString();


       //-------------------------------Click here to Login--------------------------------


        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=txtemail.getText().toString();
                String password=txtpassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty())
                {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent in=new Intent(Login.this, ChatBox.class);
                            startActivity(in);
                        }
                        else
                        {
                            Toast.makeText(Login.this, "EmailId or password is invalid", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Login.this, "Please fill all fields properiy", Toast.LENGTH_SHORT).show();
                }

                Intent in=new Intent(Login.this, ChatBox.class);
               //startActivity(in);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
           Intent intent=new Intent(this, ChatBox.class);
           startActivity(intent);

            }

        }
}