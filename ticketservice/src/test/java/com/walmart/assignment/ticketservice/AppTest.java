package com.walmart.assignment.ticketservice;

import java.util.Optional;

import com.walmart.assignment.ticketservice.business.SeatHold;
import com.walmart.assignment.ticketservice.business.Venue;
import com.walmart.assignment.ticketservice.service.TicketServiceImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		try {
			TicketServiceImpl ts = new TicketServiceImpl();
			String refCode = null;
			long waitTimeInSeconds = 5;
			System.out.println("customer wait time is " + waitTimeInSeconds + " seconds");
			System.out.println("seat hold time is " + TicketServiceImpl.seatHoldTime / 1000 + " seconds");
			Optional<Integer> searchVenueLevel = Optional.ofNullable(null);

			int count = ts.numSeatsAvailable(searchVenueLevel);
			assertEquals(6250, count);

			Optional<Integer> minLevel = Optional.ofNullable(1);
			Optional<Integer> maxLevel = Optional.ofNullable(3);
			int numSeats = 5;
			String customerEmail = "qwe@asd.com";
			// String wrongCustomerEmail = "aaa@aaa.com";
			SeatHold sh = ts.findAndHoldSeats(numSeats, minLevel, maxLevel, customerEmail);
			// assertEquals(numSeats, sh);
			System.out.println("customer starts waiting...");
			Thread.sleep(waitTimeInSeconds * 1000);
			ts.numSeatsAvailable(Optional.ofNullable(null));
			System.out.println("customer starts reservation");
			if (sh != null) {
				refCode = ts.reserveSeats(sh.getId(), customerEmail);

				if ((waitTimeInSeconds * 1000) >= TicketServiceImpl.seatHoldTime) {
					assertNull(refCode);
				} else {
					assertNotNull(refCode);
					assertEquals(sh.getRefCode(), refCode);
				}
			}
			if (sh == null)
				assertNull(refCode);

			Venue.threadPool.shutdown();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
