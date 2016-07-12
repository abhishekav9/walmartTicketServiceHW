package com.walmart.assignment.ticketservice.business;

import java.util.concurrent.atomic.AtomicInteger;

import com.walmart.assignment.ticketservice.constants.StatusEnum;

/**
 * Seats implement comparable since we are putting them in a priority queue
 * which will push the best seats (seats in front row) to the front of the queue
 * 
 * @author abhishekashwathnarayanvenkat
 *
 */
public class Seat implements Comparable<Seat> {
	private static AtomicInteger count = new AtomicInteger(0);
	private int id;
	private int row;
	// a status of the seat just to verify in future
	private StatusEnum status;

	public Seat(int row) {
		this.id = count.getAndIncrement();
		this.row = row;
		this.status = StatusEnum.AVAILABLE;
		// this.level = level;
	}

	public int compareTo(Seat o) {
		// TODO Auto-generated method stub
		return this.row - o.row;
	}
	
	public int getId() {
		return id;
	}
}
