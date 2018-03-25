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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //textboxes and buttons
    private EditText Emailtxt;
    private EditText Passwordtxt;
    private Button SignupBtn;

    //authentication
    private FirebaseAuth mAuth;


    //Database
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //controls
        Emailtxt=(EditText)findViewById(R.id.txtEmail);
        Passwordtxt=(EditText)findViewById(R.id.txtPassword);
        SignupBtn=(Button)findViewById(R.id.btnSignup);
        //authentication
        mAuth=FirebaseAuth.getInstance();

        //database reference
        ref=database.getInstance().getReference("Users");



        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
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
    private void startRegister(){
        final String email=Emailtxt.getText().toString().trim();
        final String password=Passwordtxt.getText().toString().trim();

        //validations
        if(email.isEmpty())
        {
            Emailtxt.setError("Email required");
            Emailtxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Emailtxt.setError("Enter a valid Email");
            Emailtxt.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            Passwordtxt.setError("Email required");
            Passwordtxt.requestFocus();
            return;
        }

        if(password.length()<6){
            Passwordtxt.setError("Minimum length of password is 6");
            Passwordtxt.requestFocus();
            return;
        }
        Toast.makeText(RegisterActivity.this, "registering!", Toast.LENGTH_SHORT).show();
        //register the user
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registration Successfull!", Toast.LENGTH_SHORT).show();

                        String[] part=email.split("@");
                        ref.child(part[0]).setValue(password);
                        startActivity(new Intent(RegisterActivity.this,UserActivity.class));
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
            }
        });

        Emailtxt.setText("");
        Passwordtxt.setText("");

    }
}
