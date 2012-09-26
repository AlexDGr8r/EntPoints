package net.entcraft.entpoints;

import net.entcraft.utils.SQL;

public class PointGrabber {

	private SQL sql;
	
	public PointGrabber(SQL s) {
		this.sql = s;
	}
	
	public boolean doesPlayerDataExist(String name) {
		return sql.existenceQuery("SELECT * FROM " + Main.tableName + " WHERE pname='" + name + "'");
	}
	
	
	
}
