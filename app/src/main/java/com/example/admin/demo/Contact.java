package com.example.admin.demo;

/**
 * Created by ADMIN on 22-03-2018.
 */

// CONTACT CLASS TO STORE INFORMATION
public class Contact {
    private String id;
    private String name;
    private String phno;
    private String latitude;
    private String longitude;

    //CONSTRUCTOR
    public Contact()
    {
        this.id="0";
        this.name="null";
        this.phno="null";
    }
    public Contact(String id,String name,String phno){
        this.id=id;
        this.name=name;
        this.phno=phno;
    }


    public void setLocation(String latitude,String longitude){
        this.latitude=latitude;
        this.longitude=longitude;

    }
    //GETTER MEDTHODS
    public String getId(){
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public String getPhno()
    {
        return this.phno;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public String getLongitude()
    {
        return this.longitude;
    }
}
