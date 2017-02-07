package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.ContactInviteAdapter;
import com.checkedin.model.PhoneContact;

import java.util.ArrayList;


public class ContactInviteFrg extends Fragment {
    private View view;
    private RecyclerView rvInvite;
    private AsyncTask<Void, Void, Void> contactAsync;
    private ArrayList<PhoneContact> contacts;
    public ContactInviteAdapter inviteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_contact_list, container, false);
        initUI();
        loadContacts();
        return view;
    }

    private void initUI() {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_invite));
        rvInvite = (RecyclerView) view.findViewById(R.id.rv_contact_list);
        LinearLayoutManager mListLayoutManager = new LinearLayoutManager(getActivity());
        rvInvite.setLayoutManager(mListLayoutManager);
    }

    private void loadContacts() {
        if (contactAsync != null && (contactAsync.getStatus().equals(AsyncTask.Status.RUNNING) || contactAsync.getStatus().equals(AsyncTask.Status.PENDING)))
            return;
        if (contactAsync != null) {
            contactAsync.cancel(true);
            contactAsync = null;
        }
        contactAsync = new ContactLoaderAsync();
        contactAsync.execute();
    }

    private class ContactLoaderAsync extends AsyncTask<Void, Void, Void> {
        private Cursor mailCursor;
        private Uri emailContacts;

        @SuppressLint("NewApi")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contacts = new ArrayList<>();

            try {
                emailContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] emailProjection = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), emailContacts, emailProjection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE NOCASE ASC");
                mailCursor = cursorLoader.loadInBackground();
                inviteAdapter = new ContactInviteAdapter(getActivity(), contacts);
                rvInvite.setAdapter(inviteAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mailCursor.moveToFirst()) {
                try {
                    final int displayNameIndex = mailCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    final int typeIndex = mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    final int photoIndex = mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
                    String displayName, type, photoUri;
                    do {
                        displayName = mailCursor.getString(displayNameIndex);
                        type = mailCursor.getString(typeIndex);
                        photoUri = mailCursor.getString(photoIndex);
                        final PhoneContact contact = new PhoneContact(displayName, type, photoUri);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    contacts.add(contact);
                                    inviteAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } while (mailCursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mailCursor.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

}
