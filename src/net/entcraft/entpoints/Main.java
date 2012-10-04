package net.entcraft.entpoints;

import java.util.HashMap;
import java.util.logging.Logger;

import net.entcraft.entpoints.commands.*;
import net.entcraft.entpoints.listeners.*;
import net.entcraft.entpoints.tasks.*;
import net.entcraft.utils.*;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public SQL sql;
	public Config config;
	public PointGrabber pointGrabber;
	public EntCMD cmd;
	public HashMap<String, Long> timePlayedWhenLogin = new HashMap<String, Long>();
	
	private static Logger log;
	
	public static String tableName = "entPoints";
	
	public void onEnable() {
		log = this.getLogger();
		config = new Config(this);
		cmd = new EntCMD(this, "points");
		// Table = entPoints: pname - String, donatedPoints - INTEGER, earnedPoints - INTEGER, vouchedPoints - INTEGER, 
		String host = config.get("sql.host", "localhost");
		String database = config.get("sql.database", "syncdb");
		String username = config.get("sql.username", "admin");
		String password = config.get("sql.password", "12345");
		config.saveAllData();
		sql = new SQL(host, database, username, password);
		sql.init();
		if (!sql.checkTable(tableName)) {
			log.info("Points table does not exist! Creating table...");
			sql.standardQuery("CREATE TABLE " + tableName + " (pname varchar(255) PRIMARY KEY, donatedPoints int, earnedPoints int, vouchedPoints int, lastLogin bigint, timePlayed bigint);");
			log.info("Points table created!");
		}
		pointGrabber = new PointGrabber(sql);
		cmd.addSubCommand("pointCheckSelf", new PointCheck(this, false), "check");
		cmd.addSubCommand("pointCheckOther", new PointCheck(this, true), "check <player>", 1);
		createTasks();
		this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
		
	}
	
	public void onDisable() {
		sql.closeConnection();
	}
	
	private void createTasks() {
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TaskTime(this), 20 * 15, 20 * 15);
		
	}
	
	public static void log_info(String s) {
		log.info(s);
	}

}
