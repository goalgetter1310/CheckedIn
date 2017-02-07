package com.checkedin.model;

public class WordAction {
	private String userId;
	private int action;
	private String word;

	public WordAction(int action, String word, String userId) {
		this.action = action;
		this.word = word;
		this.userId = userId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
