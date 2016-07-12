package com.walmart.assignment.ticketservice.constants;

public enum StatusEnum{
	AVAILABLE(1), HELD(2), RESERVED(3);
	private int statusId;

	private StatusEnum(int statusId) {
		this.statusId = statusId;
	}
	
	public int getStatusId() {
		return statusId;
	}
}
