package com.checkedin.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.checkedin.model.EducationInfo;
import com.checkedin.model.LivingPlace;
import com.checkedin.model.Message;
import com.checkedin.model.UserDetail;
import com.checkedin.model.WorkInfo;

import java.util.ArrayList;

public class ManageDatabase {
    private static final String databaseName = "checkin_db";
    private static final String chatMessageTbl = "chat_message_tbl";
    private static final String userTbl = "user_tbl";
    private static final String eduInfoTbl = "edu_info_tbl";
    private static final String livingPlaceInfoTbl = "living_place_info_tbl";
    private static final String workInfoTbl = "work_info_tbl";


    private SQLiteDatabase dbCheckin;


    public void openDatabase(Context context) {
        dbCheckin = context.openOrCreateDatabase(databaseName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String createRequestTbl = "CREATE TABLE IF NOT EXISTS " + chatMessageTbl + " (msg_id INTEGER PRIMARY KEY , receiver_id TEXT, message TEXT, msg_type TEXT)";
        String createUserTbl = "CREATE TABLE IF NOT EXISTS " + userTbl + " (id INTEGER PRIMARY KEY , user_id TEXT, first_name TEXT, last_name TEXT,email TEXT, image TEXT, username TEXT, birthday TEXT, marital_status TEXT, state TEXT, city TEXT, country TEXT, mobile TEXT, gender TEXT )";
        String createEduInfoTbl = "CREATE TABLE IF NOT EXISTS " + eduInfoTbl + " (id INTEGER PRIMARY KEY, edu_id TEXT, user_id TEXT, type_id INTEGER, name TEXT, full_address TEXT, latitude TEXT, longitude TEXT, from_date TEXT, to_date TEXT, complete INTEGER )";
        String createLivingPlaceInfoTbl = "CREATE TABLE IF NOT EXISTS " + livingPlaceInfoTbl + " (id INTEGER PRIMARY KEY, living_id TEXT, user_id TEXT, latitude TEXT,longitude TEXT, location_type TEXT )";
        String createWorkTbl = "CREATE TABLE IF NOT EXISTS " + workInfoTbl + " (id INTEGER PRIMARY KEY, work_id TEXT, user_id TEXT, company_name TEXT,designation TEXT, full_address TEXT, latitude TEXT, longitude TEXT, from_date TEXT, to_date TEXT, currently_working INTEGER)";

        dbCheckin.execSQL(createRequestTbl);
        dbCheckin.execSQL(createUserTbl);
        dbCheckin.execSQL(createEduInfoTbl);
        dbCheckin.execSQL(createLivingPlaceInfoTbl);
        dbCheckin.execSQL(createWorkTbl);
    }

    public void saveLoginUserDetails(UserDetail user) {

        ContentValues valuesUser = new ContentValues();
        valuesUser.put("user_id", user.getId());
        valuesUser.put("first_name", user.getFirstName());
        valuesUser.put("last_name", user.getLastName());
        valuesUser.put("email", user.getEmail());
        valuesUser.put("image", user.getImageUrl());
        valuesUser.put("username", user.getUsername());
        valuesUser.put("birthday", user.getDatOfBirth());
        valuesUser.put("marital_status", user.getMaritalStatus());
        valuesUser.put("state", user.getState());
        valuesUser.put("city", user.getCity());
        valuesUser.put("country", user.getCountry());
        valuesUser.put("mobile", user.getMobileNo());
        valuesUser.put("gender", user.getGender());

        dbCheckin.insert(userTbl, null, valuesUser);

        saveLoginEduInfo(user.getId(), (ArrayList<EducationInfo>) user.getEducationInfoList());
        saveLoginLivingPlaceInfo(user.getId(), (ArrayList<LivingPlace>) user.getLivingPlaceInfoList());
        saveLoginWorkInfo(user.getId(), (ArrayList<WorkInfo>) user.getWorkInfoList());

    }


    private void saveLoginEduInfo(String userId, ArrayList<EducationInfo> alEduInfo) {
        for (EducationInfo eduInfo : alEduInfo) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("edu_id", eduInfo.getId());
            values.put("type_id", eduInfo.getTypeId());
            values.put("name", eduInfo.getName());
            values.put("full_address", eduInfo.getFullAddress());
            values.put("latitude", eduInfo.getLatitude());
            values.put("longitude", eduInfo.getLongitude());
            values.put("from_date", eduInfo.getFromDate());
            values.put("to_date", eduInfo.getToDate());
            values.put("complete", eduInfo.getCompleted());
            dbCheckin.insert(eduInfoTbl, null, values);
        }
    }

    private void saveLoginLivingPlaceInfo(String userId, ArrayList<LivingPlace> alLivingPlace) {
        for (LivingPlace livingPlace : alLivingPlace) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("living_id", livingPlace.getId());
            values.put("location_type", livingPlace.getType());
            values.put("latitude", livingPlace.getLatitude());
            values.put("longitude", livingPlace.getLongitude());
            dbCheckin.insert(livingPlaceInfoTbl, null, values);
        }
    }


    private void saveLoginWorkInfo(String userId, ArrayList<WorkInfo> alWorkInfo) {
        for (WorkInfo workInfo : alWorkInfo) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("work_id", workInfo.getId());
            values.put("company_name", workInfo.getCompanyName());
            values.put("designation", workInfo.getDesignation());
            values.put("full_address", workInfo.getFullAddress());
            values.put("latitude", workInfo.getLatitude());
            values.put("longitude", workInfo.getLongitude());
            values.put("from_date", workInfo.getFromDate());
            values.put("to_date", workInfo.getToDate());
            values.put("current_complete", workInfo.getCurrentWorking());
            dbCheckin.insert(workInfoTbl, null, values);
        }
    }


    public void removeChatMsg(int id) {
        dbCheckin.delete(chatMessageTbl, "msg_id=?", new String[]{String.valueOf(id)});
    }

    public Message fetchChatMsg(Context context) {
        Cursor curChatMsg = dbCheckin.query(chatMessageTbl, null, null, null, null, null, null);
        curChatMsg.moveToFirst();
        Message message = new Message(context);
        message.setMsgId(curChatMsg.getInt(curChatMsg.getColumnIndex("msg_id")));
        message.setMsgType(curChatMsg.getString(curChatMsg.getColumnIndex("msg_type")));
        message.setMsgTextOrImage(curChatMsg.getString(curChatMsg.getColumnIndex("message")));
        message.setReceiverId(curChatMsg.getString(curChatMsg.getColumnIndex("receiver_id")));
        curChatMsg.close();
        return message;
    }

    public boolean isEmptyChatMsg() {
        Cursor curChatMsg = dbCheckin.query(chatMessageTbl, null, null, null, null, null, null);
        int rowCount = curChatMsg.getCount();
        curChatMsg.close();

        return rowCount <= 0;
    }


    public void saveChatMsg(Message message) {
        ContentValues values = new ContentValues();
        values.put("receiver_id", message.getReceiverId());
        values.put("message", message.getMsgTextOrImage());
        values.put("msg_type", message.getMsgType());

        dbCheckin.insert(chatMessageTbl, null, values);
    }

    public void closeDatabase() {
        dbCheckin.close();
        dbCheckin = null;
    }
}
