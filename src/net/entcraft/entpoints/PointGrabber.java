package net.entcraft.entpoints;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import net.entcraft.utils.SQL;

public class PointGrabber {

	private enum PointColumn {
		
		Donated("donatedPoints"),
		Earned("earnedPoints"),
		LastLogin("lastLogin"),
		TimePlayed("timePlayed"),
		Vouched("vouchedPoints"),
		Donator("donator");
		
		private String columnName;
		
		PointColumn(String s) {
			columnName = s;
		}
		
		@Override
		public String toString() {
			return columnName;
		}
		
	}
	
	private SQL sql;
	
	public PointGrabber(SQL s) {
		this.sql = s;
	}
	
	public void addDonatedPoints(String player, int amount) {
		addPoints(player, amount, PointColumn.Donated);
	}
	
	public void addEarnedPoints(String player, int amount) {
		addPoints(player, amount, PointColumn.Earned);
	}
	
	private void addLong(String player, long amount, PointColumn column) {
		long oldPoints = getLongValue(player, column);
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + column.toString() + "=" + (oldPoints + amount) + " WHERE pname='" + player + "';");
	}
	
	private void addPoints(String player, int amount, PointColumn column) {
		int oldPoints = getIntValue(player, column);
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + column.toString() + "=" + (oldPoints + amount) + " WHERE pname='" + player + "';");
	}
	
	public void addTime(String player, long amount) {
		addLong(player, amount, PointColumn.TimePlayed);
	}
	
	public void addVouchedPoints(String player, int amount) {
		addPoints(player, amount, PointColumn.Vouched);
	}
	
	public void deductDonatedPoints(String player, int amount) {
		deductPoints(player, amount, PointColumn.Donated);
	}
	
	public void deductEarnedPoints(String player, int amount) {
		deductPoints(player, amount, PointColumn.Earned);
	}
	
	private void deductPoints(String player, int amount, PointColumn column) {
		int oldPoints = getIntValue(player, column);
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + column.toString() + "=" + (oldPoints - amount) + " WHERE pname='" + player + "';");
	}
	
	public void deductVouchedPoints(String player, int amount) {
		deductPoints(player, amount, PointColumn.Vouched);
	}
	
	public boolean doesPlayerDataExist(String name) {
		return sql.existenceQuery("SELECT * FROM " + Main.tableName + " WHERE pname='" + name + "';");
	}
	
	public int getDonatedPoints(String player) {
		return getIntValue(player, PointColumn.Donated);
	}
	
	public int getEarnedPoints(String player) {
		return getIntValue(player, PointColumn.Earned);
	}
	
	private int getIntValue(String player, PointColumn column) {
		ResultSet rs = sql.sqlQuery("SELECT " + column.toString() + " FROM " + Main.tableName + " WHERE pname='" + player + "';");
		int p = 0;
		try {
			rs.first();
			p = rs.getInt(column.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public long getLastLogin(String player) {
		return getLongValue(player, PointColumn.LastLogin);
	}
	
	private long getLongValue(String player, PointColumn column) {
		ResultSet rs = sql.sqlQuery("SELECT " + column.toString() + " FROM " + Main.tableName + " WHERE pname='" + player + "';");
		long p = 0;
		try {
			rs.first();
			p = rs.getLong(column.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public long getTimePlayed(String player) {
		return getLongValue(player, PointColumn.TimePlayed);
	}
	
	public int getTotalPoints(String player) {
		ResultSet rs = sql.sqlQuery("SELECT * FROM " + Main.tableName + " WHERE pname='" + player + "';");
		int totalPoints = 0;
		try {
			rs.first();
			totalPoints = rs.getInt(PointColumn.Donated.toString()) + rs.getInt(PointColumn.Earned.toString()) + rs.getInt(PointColumn.Vouched.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalPoints;
	}
	
	public int getVouchedPoints(String player) {
		return getIntValue(player, PointColumn.Vouched);
	}
	
	public void setLastLogin(String player) {
		Date today = new Date();
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + PointColumn.LastLogin.toString() + "=" + today.getTime() + "  WHERE pname='" + player + "';");
	}
	
	public boolean isDonator(String player) {
		ResultSet rs = sql.sqlQuery("SELECT " + PointColumn.Donator.toString() + " FROM " + Main.tableName + " WHERE pname='" + player + "';");
		boolean donated = false;
		try {
			rs.first();
			donated = rs.getBoolean(PointColumn.Donator.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return donated;
	}
	
	public void setDonator(String player, boolean b) {
		int i = b ? 1 : 0;
		sql.standardQuery("UPDATE " + Main.tableName + " SET " + PointColumn.Donator.toString() + "=" + i + "  WHERE pname='" + player + "';");
	}
	
}
