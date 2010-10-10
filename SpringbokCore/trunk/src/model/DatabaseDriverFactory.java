package model;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDriverFactory {

	private List<DatabaseDriver> list = new ArrayList<DatabaseDriver>();

	public DatabaseDriverFactory() {
		list.add((new DatabaseDriver("org.apache.derby.jdbc.EmbeddedDriver",
				"derby")));
	}

	public List<DatabaseDriver> getAllDrivers() {
		return list;
	}
}
