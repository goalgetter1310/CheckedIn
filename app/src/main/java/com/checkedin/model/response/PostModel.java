package com.checkedin.model.response;

public class PostModel extends BaseModel {
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private String iPostID;

		public String getiPostID() {
			return iPostID;
		}

		public void setiPostID(String iPostID) {
			this.iPostID = iPostID;
		}

	}
}
