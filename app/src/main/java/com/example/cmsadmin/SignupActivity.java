package com.example.cmsadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.Reference;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailid;
    EditText psswd;
    EditText Fname;
    EditText Lname;
    Button signupBtn;
    Spinner unitid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.entered_emai_for_signup);
        psswd = findViewById(R.id.entered_password_for_signup);
        Fname = findViewById(R.id.enterfirstname);
        Lname = findViewById(R.id.enterlastname);
        unitid = findViewById(R.id.spinner);
        signupBtn = findViewById(R.id.signup_button);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    SignUp();
                }
            }
        });


    }

    void SignUp(){
        mAuth.createUserWithEmailAndPassword(emailid.getText().toString().trim(), psswd.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("successmsg", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference itemstobeuploaded = db.collection("Users").document(user.getUid());
                            SetUpUser userdetails = new SetUpUser(unitid.getSelectedItem().toString(),Fname.getText().toString().trim(),Lname.getText().toString().trim());
                            itemstobeuploaded.set(userdetails);
                            Toast.makeText(SignupActivity.this, "User Added Successfully",Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failuremsg", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }

    boolean validate(){
        if(emailid.getText().toString().trim().isEmpty() || psswd.getText().toString().trim().isEmpty() || unitid.getSelectedItem().toString()=="Select-UNIT ID"||Fname.getText().toString().trim().isEmpty()||Lname.getText().toString().trim().isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please Fill All The Details", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
