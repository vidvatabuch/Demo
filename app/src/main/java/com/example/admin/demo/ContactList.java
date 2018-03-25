package com.example.admin.demo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
import android.app.Activity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ADMIN on 22-03-2018.
 */

//CUSTOM ADAPTER FOR THE LIST VIEW
public class ContactList extends ArrayAdapter<Contact> {
    private Activity context;
    private List<Contact> contactList;

    public ContactList(Activity context,List<Contact> contactList){
        super(context,R.layout.activity_list_layout,contactList);
        this.context=context;
        this.contactList=contactList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.activity_list_layout,null,true);
        TextView textViewName=(TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewPhno=(TextView) listViewItem.findViewById(R.id.textViewPhno);

        Contact contact=contactList.get(position);
        textViewName.setText(contact.getName());
        textViewPhno.setText(contact.getPhno());

        return listViewItem;
    }
}
