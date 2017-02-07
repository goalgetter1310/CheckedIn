package com.checkedin.model.response;

import java.util.List;

import com.checkedin.model.FriendsActivity;

public class UseCheckinModel extends BaseModel {
	private Data data;

	public class Data {
		private int totalActivities, totalCheckins;
		private List<FriendsActivity> activities, checkins;

		public int getTotalActivities() {
			return totalActivities;
		}

		public void setTotalActivities(int totalActivities) {
			this.totalActivities = totalActivities;
		}

		public int getTotalCheckins() {
			return totalCheckins;
		}

		public void setTotalCheckins(int totalCheckins) {
			this.totalCheckins = totalCheckins;
		}

		public List<FriendsActivity> getActivities() {
			return activities;
		}

		public void setActivities(List<FriendsActivity> activities) {
			this.activities = activities;
		}

		public List<FriendsActivity> getCheckins() {
			return checkins;
		}

		public void setCheckins(List<FriendsActivity> checkins) {
			this.checkins = checkins;
		}

	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
