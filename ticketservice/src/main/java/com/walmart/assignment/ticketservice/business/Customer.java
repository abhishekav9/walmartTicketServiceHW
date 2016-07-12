package com.walmart.assignment.ticketservice.business;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer {
	private String email;
	private static AtomicInteger count = new AtomicInteger(0);
	private int id;
	public List<SeatHold> reservedSeatList;

	public Customer(String email) {
		// TODO Auto-generated constructor stub
		this.email = email;
		this.id = count.getAndIncrement();
		reservedSeatList = Collections.synchronizedList(new LinkedList<SeatHold>());
	}

	public int getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
}
