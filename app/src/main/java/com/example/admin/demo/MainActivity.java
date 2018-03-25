package com.example.admin.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //controls
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginbtn;
    private Button mRegisterbtn1;

    //firebase user authentication
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mEmailField=(EditText)findViewById(R.id.mEmail);
            mPasswordField=(EditText)findViewById(R.id.mPassword);
            mLoginbtn=(Button)findViewById(R.id.btnLogin);
            mRegisterbtn1=(Button)findViewById(R.id.btnRegister);

            //getting the instance of the firebase authentication
            mAuth=FirebaseAuth.getInstance();

            //intent to the register activity
            mRegisterbtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }
            });

            //login button
            mLoginbtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    startlogin();
                    //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();
                }
            });

        //validation if  the current user isnt null i.e if logged in redirect him/her to the user profile activity
        if(mAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }
    }

    private void startlogin(){
        String email=mEmailField.getText().toString().trim();
        String password=mPasswordField.getText().toString().trim();

        //validations for the email and phone number field
        if(email.isEmpty())
        {
            mEmailField.setError("Email required");
            mEmailField.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailField.setError("Enter a valid Email");
            mEmailField.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            mPasswordField.setError("Email required");
            mPasswordField.requestFocus();
            return;
        }

        if(password.length()<6){
            mPasswordField.setError("Minimum length of password is 6");
            mPasswordField.requestFocus();
            return;
        }
        Toast.makeText(MainActivity.this,"Logging in..",Toast.LENGTH_LONG);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Login SuccessFul",Toast.LENGTH_LONG);
                    startActivity(new Intent(MainActivity.this,UserActivity.class));
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Login unsuccessful",Toast.LENGTH_LONG);
                }
            }
        });
    }

}
