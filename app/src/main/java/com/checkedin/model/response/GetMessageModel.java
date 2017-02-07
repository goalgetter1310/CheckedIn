package com.checkedin.model.response;

import java.util.List;

public class GetMessageModel extends BaseModel {
	private Data data;

	public class Data {
		private List<GetMessage> messages;
		private int total;

		public class GetMessage {
			private String iMessageID, iMsgFrom, iMsgTo, eMessageType, tMessage, dCreated, tMessage_original_img;

			public String getiMessageID() {
				return iMessageID;
			}

			public void setiMessageID(String iMessageID) {
				this.iMessageID = iMessageID;
			}

			public String getiMsgFrom() {
				return iMsgFrom;
			}

			public void setiMsgFrom(String iMsgFrom) {
				this.iMsgFrom = iMsgFrom;
			}

			public String getiMsgTo() {
				return iMsgTo;
			}

			public void setiMsgTo(String iMsgTo) {
				this.iMsgTo = iMsgTo;
			}

			public String geteMessageType() {
				return eMessageType;
			}

			public void seteMessageType(String eMessageType) {
				this.eMessageType = eMessageType;
			}

			public String gettMessage() {
				return tMessage;
			}

			public void settMessage(String tMessage) {
				this.tMessage = tMessage;
			}

			public String getdCreated() {
				return dCreated;
			}

			public void setdCreated(String dCreated) {
				this.dCreated = dCreated;
			}

			public String gettMessage_original_img() {
				return tMessage_original_img;
			}

			public void settMessage_original_img(String tMessage_original_img) {
				this.tMessage_original_img = tMessage_original_img;
			}
		}

		public List<GetMessage> getMessages() {
			return messages;
		}

		public void setMessages(List<GetMessage> messages) {
			this.messages = messages;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}
