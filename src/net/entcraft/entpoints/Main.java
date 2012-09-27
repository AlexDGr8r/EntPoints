package net.entcraft.entpoints;

import java.util.logging.Logger;

import net.entcraft.utils.Config;
import net.entcraft.utils.SQL;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public SQL sql;
	public Config config;
	public PointGrabber pointGrabber;
	
	private Logger log;
	
	public static String tableName = "entPoints";
	
	public void onEnable() {
		log = this.getLogger();
		config = new Config(this);
		// Table = entPoints: pname - String, donatedPoints - INTEGER, earnedPoints - INTEGER, vouchedPoints - INTEGER
		String host = config.get("sql.host", "localhost");
		String database = config.get("sql.database", "syncdb");
		String username = config.get("sql.username", "admin");
		String password = config.get("sql.password", "12345");
		config.saveAllData();
		sql = new SQL(host, database, username, password);
		sql.init();
		if (!sql.checkTable(tableName)) {
			log.info("Points table does not exist! Creating table...");
			sql.standardQuery("CREATE TABLE " + tableName + " (pname varchar(255) PRIMARY KEY, donatedPoints int, earnedPoints int, vouchedPoints int);");
			log.info("Points table created!");
		}
		pointGrabber = new PointGrabber(sql);
		
	}
	
	public void onDisable() {
		sql.closeConnection();
	}

}
