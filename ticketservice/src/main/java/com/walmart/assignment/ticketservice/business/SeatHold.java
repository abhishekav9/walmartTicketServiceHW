package com.walmart.assignment.ticketservice.business;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.walmart.assignment.ticketservice.service.TicketServiceImpl;

public class SeatHold implements Runnable {
	private AtomicInteger count = new AtomicInteger(0);
	private Map<Integer, List<Seat>> seatListMap;
	private String customerEmail;
	private String refCode;
	private int id;

	public SeatHold(String customerEmail, Map<Integer, List<Seat>> seatListMap) {
		// TODO Auto-generated constructor stub
		this.customerEmail = customerEmail;
		this.seatListMap = seatListMap;
		id = count.getAndIncrement();
		refCode = UUID.randomUUID().toString();
	}

	public int getId() {
		return id;
	}

	public String getRefCode() {
		return refCode;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public Map<Integer, List<Seat>> getSeatListMap() {
		return seatListMap;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("holding seats with seatholdID:"+ id);
			Thread.sleep(TicketServiceImpl.seatHoldTime);
			if (Venue.heldMap.containsKey(id)) {
				Venue.heldMap.remove(id);
				Venue.putBackSeats(this);
				System.out.println("Seats held with ID:" + id + " was putback due to timeout");
			} else {
				System.out.println("Seats held with ID:" + id + " was reserved with refcode:"+refCode);
			}
			System.out.println("completed holding");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
