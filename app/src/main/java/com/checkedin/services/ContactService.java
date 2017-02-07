package com.checkedin.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.checkedin.activity.MainActivity;
import com.checkedin.model.ContactList;
import com.checkedin.utility.MyPrefs;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

public class ContactService extends IntentService {

    public ContactService() {
        super("Contact Service");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            String data=readAllContact();
            MyPrefs.getInstance(getApplicationContext()).set("ContactList", data);
            MyPrefs.getInstance(getApplicationContext()).set("dayContact", Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_MONTH)+"");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readAllContact() throws Exception{
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        LinkedList<String> stringList=new LinkedList<>();
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        stringList.add(phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        ContactList contactList = new ContactList();
        contactList.setContactList(stringList);
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        return json;
    }

}