package com.example.cmsadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText emailEditText;
    EditText passwordEditText;
    Button loginBtn;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextEnterEmailId);
        passwordEditText = findViewById(R.id.editTextEnterPassword);
        loginBtn = findViewById(R.id.buttonAdminLogin);

        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(MainActivity.this,AdminCMS.class));
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "", "Please wait while you are logged in...", true);
                if(emailEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter All Login Details", Toast.LENGTH_SHORT).show();
                }
                else{
                    Login(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim());
                }
            }
        });


    }

    void Login(String email,String password){
        if(email.contains("@society.com")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signin success", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "User Validated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,AdminCMS.class));
                                finish();
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Log.w("signin failed", "signInWithEmail:failure", task.getException());
                                if(task.getException().toString().equals("com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred.")){
                                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }

                        }
                    });
        }
        else{
            Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            passwordEditText.setText(null);
            emailEditText.setText(null);
        }

    }

}
