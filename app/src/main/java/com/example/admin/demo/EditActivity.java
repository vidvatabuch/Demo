package com.example.admin.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
public class EditActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener /*implements OnMapReadyCallback*/{

    //controls
    private EditText personName;
    private EditText personNumber;
    private Button Edit;
    private Button Delete;
    private Button AddMap;
    private Button ViewMap;

    //firebase database
    private FirebaseDatabase database;
    private DatabaseReference ref;



    String latitude="",longitude="";

    //user authentication
    private FirebaseAuth mAuth;


    //Google API
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST=1;

    Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        final String id=bundle.getString("id");

        personName=(EditText)findViewById(R.id.edit_name);
        personNumber=(EditText)findViewById(R.id.edit_phno);

        Edit=(Button)findViewById(R.id.btnEdit);
        Delete=(Button)findViewById(R.id.btnDelete);
        AddMap=(Button)findViewById(R.id.btnAddMap);
        ViewMap=(Button)findViewById(R.id.btnViewMap);
        //personName.setText(name);

        //authentication - getting the current user
        mAuth=FirebaseAuth.getInstance();
        String[] part=mAuth.getCurrentUser().getEmail().split("@");

        //providing reference to the firebase reference to point to that node root
        ref=database.getInstance().getReference(part[0]);
        //Toast.makeText(EditActivity.this,ref.toString(),Toast.LENGTH_SHORT).show();

        //Setting up place picker for selecting location
        //Google API client
        mGoogleApiClient=new GoogleApiClient
                . Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(EditActivity.this,this)
                .build();





        //getting values from database
        ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot contactSnapShot: dataSnapshot.getChildren()){
                    contact=contactSnapShot.getValue(Contact.class);

                    if(contact.getId().equals(id)){
                        personName.setText(contact.getName());
                        personNumber.setText(contact.getPhno());
                        latitude=contact.getLatitude();
                        longitude=contact.getLongitude();
                        //Toast.makeText(EditActivity.this,contact.getLatitude(),Toast.LENGTH_SHORT).show();
                    }
                        //Toast.makeText(EditActivity.this, contact.getName(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //update the contact
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for(DataSnapshot contactSnapShot: dataSnapshot.getChildren()){
                            contact=contactSnapShot.getValue(Contact.class);

                            if(contact.getId().equals(id)){
                                Contact upcontact=new Contact(id,personName.getText().toString().trim(),personNumber.getText().toString().trim());
                                if(!(latitude.equals(""))|| !(longitude.equals("")))
                                {
                                    //Toast.makeText(addActivity.this, "Set the location", Toast.LENGTH_SHORT).show();
                                    upcontact.setLocation(latitude,longitude);
                                }
                                ref.child(id).setValue(upcontact);

                            }

                                Toast.makeText(EditActivity.this, "contact updated!", Toast.LENGTH_SHORT).show();
                                finish();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //delete the contact
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for(DataSnapshot contactSnapShot: dataSnapshot.getChildren()){
                            contact=contactSnapShot.getValue(Contact.class);

                            if(contact.getId().equals(id)){
                               // personName.setText(contact.getName());
                               // personNumber.setText(contact.getPhno());
                                contactSnapShot.getRef().removeValue();
                                Toast.makeText(EditActivity.this, "Contact Deleted!", Toast.LENGTH_SHORT).show();
                                finish();
                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        //ADDMAP to the contact
        AddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(EditActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    ;
                }
            }
        });

        //Viewmap provided in the contact information earlier or during updation
        ViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent=new Intent(EditActivity.this,MapFragment.class);

                //intent.putExtra("latitude",contact.getLatitude());
                //intent.putExtra("longitude",contact.getLongitude());


                ref.addValueEventListener(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for(DataSnapshot contactSnapShot: dataSnapshot.getChildren()){
                            contact=contactSnapShot.getValue(Contact.class);

                            if(contact.getId().equals(id)){
                                // personName.setText(contact.getName());
                                // personNumber.setText(contact.getPhno());
                               latitude=contact.getLatitude();
                               longitude=contact.getLongitude();

                              // Toast.makeText(EditActivity.this,latitude+" "+longitude,Toast.LENGTH_SHORT).show();
                            }


                        }
                        Intent intent=new Intent(EditActivity.this,MActivity.class);
                        intent.putExtra("latitude",latitude);
                        intent.putExtra("longitude",longitude);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               // Toast.makeText(EditActivity.this,latitude+" "+longitude,Toast.LENGTH_SHORT).show();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
