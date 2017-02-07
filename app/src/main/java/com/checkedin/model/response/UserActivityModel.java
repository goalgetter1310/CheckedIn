package com.checkedin.model.response;

import java.util.ArrayList;

import com.checkedin.model.FriendsActivity;

public class UserActivityModel extends BaseModel {
	private ArrayList<Data> data;

	public class Data {
		private ArrayList<FriendsActivity> activities;

		private String post_count, iCategoryID, vCategoryName;

		public String getPost_count() {
			return post_count;
		}

		public void setPost_count(String post_count) {
			this.post_count = post_count;
		}

		public String getiCategoryID() {
			return iCategoryID;
		}

		public void setiCategoryID(String iCategoryID) {
			this.iCategoryID = iCategoryID;
		}

		public String getvCategoryName() {
			return vCategoryName;
		}

		public void setvCategoryName(String vCategoryName) {
			this.vCategoryName = vCategoryName;
		}

		public ArrayList<FriendsActivity> getActivities() {
			return activities;
		}

		public void setActivities(ArrayList<FriendsActivity> activities) {
			this.activities = activities;
		}

	}

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data = data;
	}

}
