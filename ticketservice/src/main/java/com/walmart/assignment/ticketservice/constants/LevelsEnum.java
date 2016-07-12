package com.walmart.assignment.ticketservice.constants;

/**
 * LevelsEnum containing the levels of the venue Each level has name, ID, rows,
 * seats per row, price per seat
 * 
 * @author abhishekashwathnarayanvenkat
 *
 */
public enum LevelsEnum {
	ORCHESTRA("Orchestra", 1, 25, 50, 100), MAIN("Main", 2, 20, 100, 75), BALCONY1("Balcony 1", 3, 15, 100,
			50), BALCONY2("Balcony 2", 4, 15, 100, 40);

	private final Integer id;
	private final String name;
	private int rows;
	private int seatsPerRow;
	private float price;

	private LevelsEnum(String name, Integer id, int rows, int seatsPerRow, float price) {
		this.name = name;
		this.id = id;
		this.rows = rows;
		this.seatsPerRow = seatsPerRow;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public int getRows() {
		return rows;
	}

	public int getSeatsPerRow() {
		return seatsPerRow;
	}

	public Integer getId() {
		return id;
	}

	public String toString() {
		return name;
	}
}
