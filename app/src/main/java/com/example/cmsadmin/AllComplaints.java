package com.example.cmsadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllComplaints extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter_AdminCMS mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ComplaintParameters> mArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_complaints);

        getSupportActionBar().setTitle("Complaint Details");//Name to be displayed in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//showing back navigation button in toolbar

        if(mArrayList==null){
            mArrayList = new ArrayList<>();
            db.collection("Complaints").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d: list){
                            ComplaintParameters cp_obj = d.toObject(ComplaintParameters.class);
                            cp_obj.ComplainId=d.getId();
                            Log.d("Assigned To = ","");
                            mArrayList.add(cp_obj);
                        }
                        //setting up recycler view
                        if(mArrayList!=null){
                            Toast.makeText(AllComplaints.this, "Data Fetch Successful", Toast.LENGTH_SHORT).show();
                            setupRecyclerView(mArrayList);
                        }
                        else{
                            Toast.makeText(AllComplaints.this, "Empty List", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AllComplaints.this, "Unable To Fetch Records", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    //connecting the menu items created into the actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_complaints_action_buttons,menu);

        MenuItem searchitem = menu.findItem(R.id.menu_button_search_bar);
        SearchView searchView  = (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_button_search_bar:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }//showing menu items in actionbar completed

    void setupRecyclerView(ArrayList<ComplaintParameters> mArrayList){
            recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new RecyclerViewAdapter_AdminCMS(mArrayList);
            mAdapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);

            mAdapter.openCardView(new RecyclerViewAdapter_AdminCMS.clickOnCardViewHandler() {
                @Override
                public void CardClicked(int pos) {
                    showComplaintInDetail(pos);
                }
            });
    }

    void showComplaintInDetail(int pos){
        Intent intent  = new Intent(AllComplaints.this,ComplaintInDetail.class);
        intent.putExtra("current_position",pos);
        Bundle b = new Bundle();
        b.putSerializable("mArrayList",mArrayList);
        intent.putExtras(b);
        startActivity(intent);
    }


}
