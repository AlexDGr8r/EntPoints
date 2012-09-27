package net.entcraft.entpoints;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.entcraft.utils.SQL;

public class PointGrabber {

	private SQL sql;
	
	public PointGrabber(SQL s) {
		this.sql = s;
	}
	
	public boolean doesPlayerDataExist(String name) {
		return sql.existenceQuery("SELECT * FROM " + Main.tableName + " WHERE pname='" + name + "';");
	}
	
	public int getTotalPoints(String player) {
		ResultSet rs = sql.sqlQuery("SELECT * FROM " + Main.tableName + " WHERE pname='" + player + "';");
		int totalPoints = 0;
		try {
			rs.first();
			totalPoints = rs.getInt("donatedPoints") + rs.getInt("earnedPoints") + rs.getInt("vouchedPoints");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalPoints;
	}
	
	public int getDonatedPoints(String player) {
		return getIntValue(player, "donatedPoints");
	}
	
	public int getEarnedPoints(String player) {
		return getIntValue(player, "earnedPoints");
	}
	
	public int getVouchedPoints(String player) {
		return getIntValue(player, "vouchedPoints");
	}
	
	private int getIntValue(String player, String column) {
		ResultSet rs = sql.sqlQuery("SELECT " + column + " FROM " + Main.tableName + " WHERE pname='" + player + "';");
		int p = 0;
		try {
			rs.first();
			p = rs.getInt(column);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public void addDonatedPoints(String player, int amount) {
		addPoints(player, amount, "donatedPoints");
	}
	
	public void addEarnedPoints(String player, int amount) {
		addPoints(player, amount, "earnedPoints");
	}
	
	public void addVouchedPoints(String player, int amount) {
		addPoints(player, amount, "vouchedPoints");
	}
	
	private void addPoints(String player, int amount, String column) {
		int oldPoints = getIntValue(player, column);
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + column + "=" + (oldPoints + amount) + " WHERE pname='" + player + "';");
	}
	
}
