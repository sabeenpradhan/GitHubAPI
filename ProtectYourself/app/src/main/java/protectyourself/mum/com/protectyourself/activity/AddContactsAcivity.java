package protectyourself.mum.com.protectyourself.activity;

import android.database.Cursor;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import protectyourself.mum.com.protectyourself.R;
import protectyourself.mum.com.protectyourself.utility.Contact;
import protectyourself.mum.com.protectyourself.utility.ContactListModel;
import protectyourself.mum.com.protectyourself.utility.ListAdapterForAdded;

public class AddContactsAcivity extends AppCompatActivity {
     public ArrayList<String> nameArr;

    public ArrayList<String> numbers;
    public EditText searchText;
    private ListAdapterForAdded ad;
    public Cursor cursor;
    public LocationListener locatlist;
    public ArrayList<ContactListModel> models;
    public List<Contact> contactsFr;
    public DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_contacts);
        databaseHandler = new DatabaseHandler(this);
        contactsFr = databaseHandler.getAllContacts();


        if(!contactsFr.isEmpty()){
        models = new ArrayList<ContactListModel>();

        for (Contact cn: contactsFr) {
            models.add(new ContactListModel(cn.getName(), cn.getPhoneNumber()));
        }

        ad = new ListAdapterForAdded(this, models);


        ListView listDiscovery = (ListView) findViewById(R.id.listDiscovery);
        listDiscovery.setAdapter(ad);
    }else{
        Toast.makeText(getParent(), "There are no added Contacts", Toast.LENGTH_LONG).show();
    }
    }
}
