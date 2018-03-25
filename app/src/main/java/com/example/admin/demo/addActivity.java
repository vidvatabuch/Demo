package com.example.admin.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.HashMap;


//Google API
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
public class addActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //controls
    private EditText personName;
    private EditText personNumber;
    private Button addContact;
    private Button addLocation;

    //authentication
    private FirebaseAuth mAuth;

    //Database
    private FirebaseDatabase database;
    private DatabaseReference ref,ref1;

    //Google API
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST=1;


    //Location
    String latitude="";
    String longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //controls
        personName=(EditText)findViewById(R.id.txtname);
        personNumber=(EditText)findViewById(R.id.txtphoneno);
        addContact=(Button)findViewById(R.id.btnaddcontact);
        addLocation=(Button)findViewById(R.id.btnGetLocation);


        //Google API client
        mGoogleApiClient=new GoogleApiClient
                . Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(addActivity.this,this)
                .build();


        //clearing the Edittexts everytime the activity is loaded
        personNumber.setText("");
        personName.setText("");

        //authentication
        mAuth=FirebaseAuth.getInstance();
        /*if(mAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        }*/
        String[] part=mAuth.getCurrentUser().getEmail().split("@");
        //database reference
        ref=database.getInstance().getReference(part[0]);



        //ADD CONTACT
        addContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name=personName.getText().toString().trim();
                String number=personNumber.getText().toString().trim();

                if(name.isEmpty()){
                    personName.setError("Name required");
                    personName.requestFocus();
                    return;
                }

                if(number.isEmpty()){
                    personNumber.setError("Phone number required");
                    personNumber.requestFocus();
                    return;
                }

                if(!Patterns.PHONE.matcher(number).matches()){
                    personNumber.setError("Enter a valid phone number!");
                    personNumber.requestFocus();
                    return;
                }
              /*  ref.push().child("name").setValue(name);

                ref1=database.getInstance().getReference();
                ref.push().child("phno").setValue(number);*/

               /* HashMap<String, String> dataMap = new HashMap<String,String>();
                dataMap.put("name",name);
                dataMap.put("phone",number);*/

               String id=ref.push().getKey();
               Contact contact=new Contact(id,name,number);
               if(!(latitude.equals(""))|| !(longitude.equals("")))
               {
                   //Toast.makeText(addActivity.this, "Set the location", Toast.LENGTH_SHORT).show();
                   contact.setLocation(latitude,longitude);
               }


                ref.child(id).setValue(contact);
                Toast.makeText(addActivity.this,"Contact added successfully!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(addActivity.this,UserActivity.class));


            }
        });

        //get location
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(addActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    ;
                }
            }
        });

        //validation for -- if the user has logged out or is null, control is trandfered to the login activity
        if(mAuth.getCurrentUser() == null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
       // mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // Snackbar.make(fabPickPlace, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                

                String loclatitude = String.valueOf(place.getLatLng().latitude);
                String loclongitude = String.valueOf(place.getLatLng().longitude);

                latitude=loclatitude;
                longitude=loclongitude;
            }
        }
    }
}
