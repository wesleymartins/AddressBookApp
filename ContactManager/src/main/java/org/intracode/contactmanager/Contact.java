package org.intracode.contactmanager;

import android.net.Uri;

/**
 * Created by Wesley Martins on 23.07.13.
 */
public class Contact {

    private String _fname, _lname, _phone, _email, _address;
    private Uri _imageURI;
    private int _id;

    public Contact (int id, String fname,String lname, String phone, String email, String address, Uri imageURI) {
        _id = id;
        _fname = fname;
        _lname = lname;
        _phone = phone;
        _email = email;
        _address = address;
        _imageURI = imageURI;
    }

    public int getId() { return _id; }

    public String getFName() {
        return _fname;
    }

    public String getLName() {
        return _lname;
    }

    public String getPhone() {
        return _phone;
    }

    public String getEmail() {
        return _email;
    }

    public String getAddress() {
        return _address;
    }

    public Uri getImageURI() { return _imageURI; }
}
