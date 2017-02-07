package com.checkedin.model.response;

import com.checkedin.model.FriendsActivity;

import java.util.List;

public class FriendActivityModel extends BaseModel {
	private Data data;

	public class Data {
		private List<FriendsActivity> records;
		private String totalRecords;

		public String getTotalRecords() {
			return totalRecords;
		}

		public void setTotalRecords(String totalRecords) {
			this.totalRecords = totalRecords;
		}

		public List<FriendsActivity> getRecords() {
			return records;
		}

		public void setRecords(List<FriendsActivity> records) {
			this.records = records;
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
