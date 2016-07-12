package com.walmart.assignment.ticketservice.business;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.walmart.assignment.ticketservice.constants.InitializerDataInterface;
import com.walmart.assignment.ticketservice.constants.LevelsEnum;

public class Venue {

	// holds level instances for the venue
	public static List<Level> levelList = new LinkedList<Level>();

	// has a customers mapped to their email ID's
	public static Map<String, Customer> customerMap = new ConcurrentHashMap<String, Customer>();

	// temporary map to hold seats for a customer, Key = SeatHoldID, Value =
	// SeatHold object
	public static Map<Integer, SeatHold> heldMap = new ConcurrentHashMap<Integer, SeatHold>();

	// thread pool to hold seats for 60 seconds
	public static ExecutorService threadPool = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

	/**
	 * constructor initializes customers and seats in a level
	 */
	public Venue() {
		// TODO Auto-generated constructor stub
		String custEmailId[] = InitializerDataInterface.CUSTOMEREMAILID.split(",");
		for (String string : custEmailId) {
			Customer cust = new Customer(string);
			customerMap.put(string, cust);
		}
		for (LevelsEnum eachLevel : LevelsEnum.values()) {
			Level level = new Level(eachLevel.getRows(), eachLevel.getSeatsPerRow(), eachLevel.getPrice(),
					eachLevel.getId());
			levelList.add(level);
		}
	}

	/**
	 * find all available seats in the venue
	 * 
	 * @return
	 */
	public static int findAllSeats() {
		int count = 0;
		for (Level level : levelList) {
			count += level.avaialbleSeats.size();
		}
		return count;
	}

	/**
	 * find available seats in a particular level
	 * 
	 * @param level
	 * @return
	 */
	public static int findSeatsByLevel(LevelsEnum level) {
		for (Level eachLevel : levelList) {
			if (eachLevel.getLevelId() == level.getId()) {
				return eachLevel.avaialbleSeats.size();
			}
		}
		return 0;
	}

	/**
	 * Find all level between minLevel and maxLevel
	 * 
	 * @param minLevel
	 *            lower level which means better level seat
	 * @param maxLevel
	 *            higher level which means cheaper level seat
	 * @return list of Levels
	 */
	public static List<Level> getLevels(Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		List<Level> levels = new ArrayList<Level>();

		Integer min = minLevel.isPresent() ? minLevel.get() : 1;
		Integer max = maxLevel.isPresent() ? maxLevel.get() : 4;

		if ((min == null && max == null) || max < min) {
			return null;
		}

		for (Level eachLevel : levelList) {
			if (min <= eachLevel.getLevelId() && eachLevel.getLevelId() <= max) {
				levels.add(eachLevel);
			}
		}
		return levels;
	}

	/**
	 * puts back seats held by the customer. this is called by the Seat Hold
	 * object after 60 seconds if the seats are not reserved
	 * 
	 * @param sh
	 *            seatHold instance containing seats held at every level
	 */
	public static void putBackSeats(SeatHold sh) {
		Map<Integer, List<Seat>> seatMap = sh.getSeatListMap();
		for (Integer levelId : seatMap.keySet()) {
			levelList.get(levelId).avaialbleSeats.addAll(seatMap.get(levelId));
		}
	}
}