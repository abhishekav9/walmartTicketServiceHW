package com.walmart.assignment.ticketservice.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.walmart.assignment.ticketservice.business.Customer;
import com.walmart.assignment.ticketservice.business.Level;
import com.walmart.assignment.ticketservice.business.Seat;
import com.walmart.assignment.ticketservice.business.SeatHold;
import com.walmart.assignment.ticketservice.business.Venue;
import com.walmart.assignment.ticketservice.constants.InitializerDataInterface;

/**
 * Class that provides service and implements TicketService
 * 
 * @author abhishekashwathnarayanvenkat
 *
 */
public class TicketServiceImpl implements TicketService {

	public static long seatHoldTime = 10000;
	private Object transactionLock = new Object();

	public TicketServiceImpl() {
		// TODO Auto-generated constructor stub
		seatHoldTime = InitializerDataInterface.holdTimeInSeconds * 1000;
		Venue v = new Venue();
	}

	/**
	 * returns available seats from one level OR if venueLevel is empty then
	 * across all levels return 0 if no such level
	 */
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		if (venueLevel.isPresent()) {
			try {
				int count = Venue.levelList.get(venueLevel.get() - 1).avaialbleSeats.size();
				System.out.println("Seats available = " + count + " @ level " + venueLevel.get());
				return count;
			} catch (Exception e) {
				System.out.println("No such level: Please retry");
				return 0;
			}
		} else {
			int count = Venue.findAllSeats();
			// If no venueLevel is given, search total available seat through
			// whole levels
			System.out.println("Seats available = " + count + " across all levels");
			return count;
		}
	}

	/**
	 * find best seats optionally by level and hold the seats for 10 seconds if
	 * there is no such level, search amongst all levels
	 */
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		synchronized (transactionLock) {
			int seatsToBeHeld = numSeats;
			List<Level> levelList = Venue.getLevels(minLevel, maxLevel);
			Map<Integer, List<Seat>> seatListMap = new HashMap<Integer, List<Seat>>();

			if (levelList == null || levelList.size() == 0) {
				levelList = Venue.levelList;
			} else {
				// check if seats available. Else return null and print an
				// informative message
				int count = 0;
				for (Level eachLevel : levelList) {
					count += eachLevel.avaialbleSeats.size();
				}
				if (count < seatsToBeHeld) {
					System.out.println("There are not enough seats between the levels that you requested");
					return null;
				}

				for (Level eachLevel : levelList) {
					if (numSeats > 0) {
						if (eachLevel.avaialbleSeats.size() == 0)
							continue;
						else {
							List<Seat> heldSeatList = new LinkedList<Seat>();
							int seatsAvailable = eachLevel.avaialbleSeats.size();
							if (seatsAvailable >= numSeats) {
								for (int i = 0; i < numSeats; i++) {
									heldSeatList.add(eachLevel.avaialbleSeats.poll());
								}
								seatListMap.put(eachLevel.getLevelId(), heldSeatList);
								break;
							} else {

								for (int i = 0; i < seatsAvailable; i++) {
									heldSeatList.add(eachLevel.avaialbleSeats.poll());
								}
								seatListMap.put(eachLevel.getLevelId(), heldSeatList);
								numSeats = numSeats - seatsAvailable;
							}
						}
					} else
						break;
				}
				SeatHold sh = new SeatHold(customerEmail, seatListMap);
				Venue.heldMap.put(sh.getId(), sh);
				Venue.threadPool.execute(sh);
				System.out.println(
						seatsToBeHeld + " seats held for " + customerEmail + "- Reference Code is:" + sh.getRefCode());
				return sh;
			}
			return null;
		}
	}

	/**
	 * reserve seats based on provided seatHoldID and customer email and remove
	 * from held map and put the seatHold instance into customer's reserved list
	 */
	public String reserveSeats(int seatHoldId, String customerEmail) {
		// TODO Auto-generated method stub
		Customer cust;
		if (Venue.heldMap.containsKey(seatHoldId)
				&& Venue.heldMap.get(seatHoldId).getCustomerEmail().equals(customerEmail)) {
			SeatHold sh = Venue.heldMap.remove(seatHoldId);
			if (sh == null) {
				System.out.println(
						"The held seats could not be retrieved due to timeout / wrong seatHoldID and/or emailId");
				return null;
			}
			if (Venue.customerMap.containsKey(customerEmail)) {
				cust = Venue.customerMap.get(customerEmail);
			} else {
				cust = new Customer(customerEmail);
				cust.reservedSeatList.add(sh);
				System.out.println("The held seats have been reserved for:" + customerEmail + ":Reference Code is:"
						+ sh.getRefCode());
				Venue.customerMap.put(customerEmail, cust);
			}
			return sh.getRefCode();

		} else {
			System.out
					.println("The held seats could not be retrieved due to timeout / wrong seatHoldID and/or emailId");
			return null;
		}

	}

}
