package com.checkedin.model.response;

import java.util.List;

import com.checkedin.model.Friend;

public class SearchFriendModel extends BaseModel {

	private List<Friend> data;

	public List<Friend> getData() {
		return data;
	}

	public void setData(List<Friend> data) {
		this.data = data;
	}
}
