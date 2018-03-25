package com.example.admin.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class MActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    List<Contact> contactList;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    String lat="";
    String lon="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        lat=bundle.getString("latitude");
         lon=bundle.getString("longitude");
       // Toast.makeText(MActivity.this,lat+" "+lon,Toast.LENGTH_SHORT).show();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        //validation for -- if the user has logged out or is null, control is trandfered to the login activity
        if(mAuth.getCurrentUser() == null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
        addMarker();
    }

    private void addMarker() {


        Double latitude=Double.parseDouble(lat);
        Double longitude=Double.parseDouble(lon);
        //Toast.makeText(MActivity.this,Double.toString(latitude)+" "+Double.toString(longitude),Toast.LENGTH_SHORT).show();
        LatLng location = new LatLng(latitude,longitude);
        Marker bang = map.addMarker(new MarkerOptions().position(location).title("contact"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }


    public void setUpMap() {

        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        lat=bundle.getString("latitude");
        lon=bundle.getString("longitude");


    }
}
