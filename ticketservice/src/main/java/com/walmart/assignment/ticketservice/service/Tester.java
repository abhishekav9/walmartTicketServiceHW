package com.walmart.assignment.ticketservice.service;

import java.util.Optional;

import com.walmart.assignment.ticketservice.business.SeatHold;
import com.walmart.assignment.ticketservice.business.Venue;

public class Tester {
	public static void main(String[] args) {
		Tester t = new Tester();
		TicketServiceImpl ts = new TicketServiceImpl();
		try {
			switch (args[0]) {
			case "1":
				t.getSeatAvailability(ts);
				break;
			case "2":
				t.getSeatAvailability(ts, args[1]);
				break;
			case "3":
				t.findAndHoldSeats(ts, Integer.parseInt(args[1]), args[2]);
				break;
			case "4":
				t.findHoldAndReserveSeats(ts, Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]));
				break;
			case "5":
				t.findHoldAndReserveSeats(ts, Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]),
						Integer.parseInt(args[4]), Integer.parseInt(args[5]));
				break;
			default:
				System.out.println("enter proper method to execute");
				break;
			}
		} catch (Exception e) {
			System.out.println("enter valid arguments!!");
		}
	}

	private void getSeatAvailability(TicketServiceImpl ts) {
		Optional<Integer> venueLevel = Optional.ofNullable(null);
		ts.numSeatsAvailable(venueLevel);
	}

	private void getSeatAvailability(TicketServiceImpl ts, String s) {
		try {
			int i = Integer.parseInt(s);
			System.out.println();
			Optional<Integer> venueLevel = Optional.ofNullable(i);
			ts.numSeatsAvailable(venueLevel);
		} catch (Exception e) {
			System.out.println("Enter an integer number (between 1 to 4) to search!!");
		}
	}

	private void findAndHoldSeats(TicketServiceImpl ts, int numSeats, String customerEmail) {
		Optional<Integer> minLevel = Optional.ofNullable(null);
		Optional<Integer> maxLevel = Optional.ofNullable(null);
		SeatHold sh = ts.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
		Venue.threadPool.shutdown();
	}

	private void findHoldAndReserveSeats(TicketServiceImpl ts, int numSeats, String customerEmail, int waitTime) throws InterruptedException {
		Optional<Integer> minLevel = Optional.ofNullable(null);
		Optional<Integer> maxLevel = Optional.ofNullable(null);
		SeatHold sh = ts.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
		System.out.println("customer starts waiting...");
		Thread.sleep(waitTime * 1000);
		ts.numSeatsAvailable(Optional.ofNullable(null));
		System.out.println("customer starts reservation");
		if (sh != null) {
			ts.reserveSeats(sh.getId(), customerEmail);
		}
		Venue.threadPool.shutdown();
	}

	private void findHoldAndReserveSeats(TicketServiceImpl ts, int numSeats, String customerEmail, int minLevelNumber,
			int maxLevelNumber, int waitTime) throws InterruptedException {
		Optional<Integer> minLevel = Optional.ofNullable(minLevelNumber);
		Optional<Integer> maxLevel = Optional.ofNullable(maxLevelNumber);
		SeatHold sh = ts.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
		System.out.println("customer starts waiting...");
		Thread.sleep(waitTime * 1000);
		ts.numSeatsAvailable(Optional.ofNullable(null));
		System.out.println("customer starts reservation");
		if (sh != null) {
			ts.reserveSeats(sh.getId(), customerEmail);
		}
		Venue.threadPool.shutdown();
	}
}
