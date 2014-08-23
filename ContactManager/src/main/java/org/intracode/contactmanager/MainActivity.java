package org.intracode.contactmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText fnameTxt, lnameTxt, phoneTxt, emailTxt, addressTxt;
    ImageView contactImageImgView;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageUri = Uri.parse("android.resource://org.intracode.contactmanager/drawable/no_user_logo");
    DatabaseHandler dbHandler;
    Uri wesImg = Uri.parse("android.resource://org.intracode.contactmanager/drawable/wesleyimage");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        setContentView(R.layout.activity_main);

        fnameTxt = (EditText) findViewById(R.id.txtFirstName);
        lnameTxt = (EditText) findViewById(R.id.txtLastName);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        emailTxt = (EditText) findViewById(R.id.txtEmail);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        contactListView = (ListView) findViewById(R.id.listView);
        contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        dbHandler = new DatabaseHandler(getApplicationContext());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);





        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contact contact = new Contact(dbHandler.getContactsCount(), String.valueOf(fnameTxt.getText()),String.valueOf(lnameTxt.getText()), String.valueOf(phoneTxt.getText()), String.valueOf(emailTxt.getText()), String.valueOf(addressTxt.getText()), imageUri);
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    Toast.makeText(getApplicationContext(), String.valueOf(fnameTxt.getText()) + " has been added to your Contacts!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(fnameTxt.getText()) + " already exists. Please use a different name.", Toast.LENGTH_SHORT).show();
            }
        });



        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Contact c =  Contacts.get(position);
                String fname = c.getFName();
                String lname = c.getLName();
                String phone = c.getPhone();
                String email = c.getEmail();
                String address = c.getAddress();
                Uri img = c.getImageURI();
                // When clicked, show a toast with the TextView text
                dlgAlert.setMessage("First Name:  " +String.valueOf(fname)+"\n\nLast Name: "+String.valueOf(lname)+"\n\nPhoneNumber: "+String.valueOf(phone)+"\n\nEmail: "+String.valueOf(email)+"\n\nAddress: "+String.valueOf(address));
                dlgAlert.setTitle("Contact Info");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();


                    //code specific to first list item


            }
        });

        fnameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                addBtn.setEnabled(String.valueOf(fnameTxt.getText()).trim().length() > 0);
        }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        contactImageImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }

            });

        if (dbHandler.getContactsCount() != 0)
            Contacts.addAll(dbHandler.getAllContacts());

        Contact c1 = new Contact(0, "Wesley", "Martins", "0844009418", "wesleymartins34@hotmail.com", "12 Vineyard Road", wesImg);
        dbHandler.createContact(c1);
        Contacts.add(c1);
        Contact c2 = new Contact(1, "Joe", "Smith", "0835003218", "jsmith@hotmail.com", "43 Glendale Close", imageUri);
        dbHandler.createContact(c2);
        Contacts.add(c2);


        populateList();
    }

    private boolean contactExists(Contact contact) {
        String name = contact.getFName();
        int contactCount = Contacts.size();

        for (int i = 0; i < contactCount; i++) {
            if (name.compareToIgnoreCase(Contacts.get(i).getFName()) == 0)
                return true;
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                contactImageImgView.setImageURI(data.getData());
            }
        }
    }

    private void populateList() {
        ArrayAdapter<Contact> adapter = new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

    private class ContactListAdapter extends ArrayAdapter<Contact>
    {
        public ContactListAdapter()
        {
            super (MainActivity.this, R.layout.listview_item, Contacts);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView name = (TextView) view.findViewById(R.id.txtLastNameList);
            name.setText(currentContact.getLName());
            TextView phone = (TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());

            ImageView ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
            ivContactImage.setImageURI(currentContact.getImageURI());

            return view;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
