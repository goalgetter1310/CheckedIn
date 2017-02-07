package com.checkedin.contentobeserver;

import android.database.ContentObserver;

import com.checkedin.utility.Utility;

public class ContactObserver extends ContentObserver {

        public ContactObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Utility.logUtils("observer called");

        }

}