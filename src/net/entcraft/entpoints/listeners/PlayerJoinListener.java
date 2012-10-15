package net.entcraft.entpoints.listeners;

import java.util.Date;

import net.entcraft.entpoints.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends EntListener implements Listener {

	private int loginBonus = 0;
	
	public PlayerJoinListener(Main instance) {
		super(instance);
		loginBonus = plugin.config.get("Points.loginBonus", 5);
		plugin.config.saveAllData();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent event) {
		if (!plugin.pointGrabber.doesPlayerDataExist(event.getPlayer().getName())) {
			Date today = new Date();
			plugin.sql.standardQuery("INSERT INTO " + Main.tableName + " VALUES ('" + event.getPlayer().getName() + "', 0, 0, 0, " + today.getTime() + ", 0, 0);");
			giveLoginBonus(event.getPlayer());
		} else if (hasOneDayPassedSinceLastLogin(event.getPlayer())) {
			giveLoginBonus(event.getPlayer());
		}
		String pname = event.getPlayer().getName();
		if (!plugin.timePlayedWhenLogin.containsKey(pname)) {
			plugin.timePlayedWhenLogin.put(pname, plugin.pointGrabber.getTimePlayed(pname));
		}
	}
	
	private void giveLoginBonus(Player player) {
		plugin.pointGrabber.addEarnedPoints(player.getName(), loginBonus);
		plugin.pointGrabber.setLastLogin(player.getName());
		player.sendMessage(ChatColor.GOLD + "You earned " + loginBonus + " points for logging in today!");
	}
	
	private boolean hasOneDayPassedSinceLastLogin(Player player) {
		Date today = new Date();
		long longToday = today.getTime();
		long lastLogin = plugin.pointGrabber.getLastLogin(player.getName());
		long diff = longToday - lastLogin;
		if (diff >= (long)(1000 * 60 * 60 * 24)) {
			return true;
		}
		return false;
	}

}
