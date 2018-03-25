package com.example.admin.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.Object;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class UserActivity extends AppCompatActivity {

    //authentication
    private FirebaseUser currentuser;
    private FirebaseAuth mAuth;

    //database
    private FirebaseDatabase database;
    private DatabaseReference ref;

    //controls
    private TextView txt2;
    private Button logout;
    private Button add;

   // private Button view;
    private ListView list;


    //arraylist to populate the contact list
    List<Contact> NamesList;

    //arrayadapter to populate listview
    ArrayAdapter<Contact> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //firebase
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();

        //controls
        txt2=(TextView)findViewById(R.id.textView2);
        logout=(Button)findViewById(R.id.btnlogout);
        add=(Button)findViewById(R.id.btnadd);
       // view=(Button)findViewById(R.id.btnview);
        list=(ListView)findViewById(R.id.listview);

        //arraylist to populate listview
        NamesList = new ArrayList<>();



        //database
        String[] part=mAuth.getCurrentUser().getEmail().split("@");
        ref=database.getInstance().getReference(part[0]);



        //GETS THR CURRENTLY LOGGED IN USER'S DETAILS
        String name=currentuser.getDisplayName();
        String email=currentuser.getEmail();
        txt2.setText(email);


      //  list.setVisibility(View.GONE);
        //add
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(UserActivity.this,addActivity.class));
            }
        });



        //list child click events
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               Contact c=(Contact)list.getAdapter().getItem(i);
                Intent intent=new Intent(UserActivity.this,EditActivity.class);
                intent.putExtra("id",c.getId());
                startActivity(intent);
             //  Toast.makeText(UserActivity.this,c.getId(),Toast.LENGTH_LONG).show();


            }
        });
        //logout
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                    //logging out the user
                    mAuth.signOut();
                    //closing activity
                    finish();
                    //starting login activity
                    startActivity(new Intent(UserActivity.this, MainActivity.class));
                }
                //Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();

        });

        //validation for -- if the user has logged out or is null, control is trandfered to the login activity
        if(mAuth.getCurrentUser() == null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }


    //POPULATES THE LISTVIEW WITH THE CONTACT DETAILS OF THE LOGGED IN USER
    @Override
    protected void onStart() {
        super.onStart();


        ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                NamesList.clear();
                for(DataSnapshot contactSnapShot: dataSnapshot.getChildren()){
                    Contact contact=contactSnapShot.getValue(Contact.class);

                    NamesList.add(contact);
                    //Toast.makeText(UserActivity.this, contact.getName(), Toast.LENGTH_SHORT).show();

                }
                adapter=new ContactList(UserActivity.this, NamesList);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
