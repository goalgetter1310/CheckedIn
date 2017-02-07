package com.checkedin.model;

public class PhoneContact {
	private String name;
	private String mobileNo;
	private String imageUrl;

	public PhoneContact(String name, String mobileNo, String imageUrl) {
		this.name = name;
		this.mobileNo = mobileNo;
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
