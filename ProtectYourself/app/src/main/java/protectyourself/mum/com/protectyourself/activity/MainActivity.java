package protectyourself.mum.com.protectyourself.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import protectyourself.mum.com.protectyourself.R;
import protectyourself.mum.com.protectyourself.utility.Contact;
import protectyourself.mum.com.protectyourself.utility.ContactListAdapter;
import protectyourself.mum.com.protectyourself.utility.ContactListModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private SearchView searchView;
    public ArrayList<String> nameArr;
    private ArrayList<String> numberArr;
    public ArrayList<String> numbers;
    private ContactListAdapter ad;
    public Cursor cursor;

    public LocationListener locatlist;
    public ArrayList<ContactListModel> models;
    public DatabaseHandler db;
    public List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_contacts);
        db = new DatabaseHandler(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        
        navigationView.setNavigationItemSelectedListener(this);


        nameArr = new ArrayList<String>();
        numberArr = new ArrayList<String>();
        ContentResolver cr = this.getContentResolver(); // Activity/Application
        // android.content.Context
        cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer
                        .parseInt(cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[] { id }, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String name = pCur
                                .getString(pCur
                                        .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

                        numberArr.add(contactNumber);
                        nameArr.add(name);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }

        models = new ArrayList<ContactListModel>();
        for (int i = 0; i < numberArr.size(); i++) {
            models.add(new ContactListModel(nameArr.get(i), numberArr.get(i)));
        }

        ad = new ContactListAdapter(this, models);

        ListView listDiscovery = (ListView) findViewById(R.id.listDiscovery);
        listDiscovery.setAdapter(ad);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showToast(query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                int textlength = s.length();
                ArrayList<ContactListModel> tempArrayList = new ArrayList<ContactListModel>();
                for (ContactListModel c : models) {
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase()
                                .contains(s.toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                ListView listDiscovery = (ListView) findViewById(R.id.listDiscovery);
                ad = new ContactListAdapter(MainActivity.this, tempArrayList);
                listDiscovery.setAdapter(ad);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_contacts) {
            Log.d("sabeen","all contact");
                startActivity(new Intent(MainActivity.this,MainActivity.class));
        } else if (id == R.id.nav_add_contacts) {
            showToast("addeddss");
            startActivity(new Intent(MainActivity.this,AddContactsAcivity.class));
        } else if (id == R.id.nav_browse) {
            showToast("browse");
        }  else if (id == R.id.nav_share) {            Log.d("sabeen","share");
        } else if (id == R.id.nav_send) {

        }else{
            Log.d("sabeen","nothing");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showToast(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void onAdd(View view) {



        final String number = (String) view.getTag(R.string.checka);
        final String name = (String) view.getTag(R.string.checkb);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        db.addContact(new Contact(name,number));


                        Toast.makeText(getApplicationContext(), name+" "+number+" has been added to your list", Toast.LENGTH_LONG).show();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add to you Emergency list")
                .setMessage("Add this number " + number + "?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)

                .setCancelable(false).show();




    }
    public void onDelete(View view) {



        final String number = (String) view.getTag(R.string.checka);
        final String name = (String) view.getTag(R.string.checkb);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
//				numbers.add(number);
//				for (int i = 0; i < numbers.size(); i++) {
                        Log.e("s", number);
//				}


                        db.deleteContact(name);

                        Toast.makeText(getApplicationContext(), name+" "+number+" has been deleted from your list", Toast.LENGTH_LONG).show();
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete from your Emergency list")
                .setMessage("Delete this number " + number + "?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)

                .setCancelable(false).show();




    }


}
