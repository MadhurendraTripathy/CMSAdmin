package com.example.cmsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminCMS extends AppCompatActivity {
    Button viewComplaintsBtn,addUserBtn;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    TextView name,society;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cms);
        viewComplaintsBtn=findViewById(R.id.buttonComplaintSection);
        name=findViewById(R.id.textHiName2);
        society=findViewById(R.id.CMSSociety);
        addUserBtn = findViewById(R.id.buttonAddUser);
        uid = mAuth.getUid();
        db.collection("Admin").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.get("Name").toString());
                society.setText(documentSnapshot.get("Society").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminCMS.this, "Unable to fetch data at the moment", Toast.LENGTH_SHORT).show();
            }
        });

        viewComplaintsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminCMS.this,AllComplaints.class));
            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminCMS.this,SignupActivity.class));
            }
        });

    }
}
