package com.walmart.assignment.ticketservice.business;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Level {
	//available seats having a priority queue to store seats row-wise (front row is the best seat)  
	public Queue<Seat> avaialbleSeats;
	private int rows;
	private int seatsPerRow;
	private float price;
	private int levelId;

	public int getLevelId() {
		return levelId;
	}

	/**
	 * Constructor to initialize seats and insert them in the queue
	 * @param rows
	 * @param seatsPerRow
	 * @param price
	 * @param level
	 */
	public Level(int rows, int seatsPerRow, float price, int level) {
		this.rows = rows;
		this.seatsPerRow = seatsPerRow;
		this.price = price;
		this.levelId = level;
		avaialbleSeats = new PriorityBlockingQueue<Seat>(seatsPerRow * rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < seatsPerRow; j++) {
				Seat seat = new Seat(i);
				avaialbleSeats.add(seat);
			}
		}
	}

}
