package com.checkedin.model.response;

import com.checkedin.model.FriendsActivity;

public class PostDetailsModel extends BaseModel {
	private FriendsActivity data;

	public FriendsActivity getData() {
		return data;
	}

	public void setData(FriendsActivity data) {
		this.data = data;
	}
}
